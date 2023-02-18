package i.r.grafana.json.processor;

import i.r.grafana.json.annotations.Dashboard;
import i.r.grafana.json.annotations.promethues.Metric;
import i.r.grafana.json.processor.converter.DashboardFactory;
import i.r.grafana.json.processor.converter.ObjectMapperSerializer;
import i.r.grafana.json.processor.model.Panel;
import i.r.grafana.json.processor.model.Target;
import i.r.grafana.json.processor.util.ExpressionBuilder;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({
        "i.r.grafana.json.annotations.Dashboard",
        "i.r.grafana.json.annotations.Metric",
        "i.r.grafana.json.annotations.Panel"
})
public class GrafanaDashboardGeneratorProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger("DashboardGeneratorProcessor");

    private DashboardFactory dashboardFactory = new DashboardFactory();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        log.info("annotation processor invoked");
        Set<? extends Element> dashboardAnnotations = roundEnvironment.getElementsAnnotatedWith(Dashboard.class);
        List<i.r.grafana.json.processor.model.Dashboard> preparedDashboards = new ArrayList<>();
        for (Element annotatedElement : dashboardAnnotations) {
            Dashboard dashboardAnnotation = annotatedElement.getAnnotation(Dashboard.class);
            log.info("Creating dashboard with name ----> {}", dashboardAnnotation.title());

            i.r.grafana.json.processor.model.Dashboard preparedDashboard = dashboardFactory.build(dashboardAnnotation);
            preparedDashboards.add(preparedDashboard);

            log.info("prepared dashboard {}", ObjectMapperSerializer.serialize(preparedDashboard));

            TypeElement typeElement = (TypeElement) annotatedElement;

            System.out.println(typeElement.getQualifiedName());
        }

        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Timed.class)) {
            Timed timedMetric = annotatedElement.getAnnotation(Timed.class);
            if (timedMetric != null) {
                log.info("timed metric found ----> " + timedMetric);
                registerTimedMetricToDashboard(preparedDashboards, timedMetric);
            } else {
                log.info("timed metric NOT found ----> ");
            }

            System.out.println("Trying to register metric with name ----> " + timedMetric.value() + annotatedElement);
        }

        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Counted.class)) {
            Counted counted = annotatedElement.getAnnotation(Counted.class);
            if (counted != null) {
                log.info("timed metric found ----> " + counted);
                registerCounterMetricToDashboard(preparedDashboards, counted);
            } else {
                log.info("timed metric NOT found ----> ");
            }

            System.out.println("Trying to register metric with name ----> " + counted.value() + annotatedElement);
        }

        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Metric.class)) {
            Metric metric = annotatedElement.getAnnotation(Metric.class);
            if (!annotatedElement.getKind().isField()) {
                log.warn("{} annotation should be field-level", Metric.class);
            } else {
                String typeName = annotatedElement.asType().toString();
                if (typeName.equals(Counter.class.getCanonicalName())) {
                    log.info("registering counter metric {}", metric);
                    registerCounterMetricToDashboard(preparedDashboards, metric);
                } else if (typeName.equals(Timer.class.getCanonicalName())) {
                    log.info("registering timer metric {}", metric);
                    registerTimedMetricToDashboard(preparedDashboards, metric);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(preparedDashboards)) {
            saveDashboardsToFile(preparedDashboards);
        }

        System.out.println("PROCESS END");
        return true;
    }

    private void registerTimedMetricToDashboard(List<i.r.grafana.json.processor.model.Dashboard> preparedDashboards, Metric metric) {
        if (CollectionUtils.isEmpty(preparedDashboards)) {
            return;
        }

        List<Panel> panels = preparedDashboards.stream()
                .map(i.r.grafana.json.processor.model.Dashboard::getPanels)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(p -> p.stream())
                .filter(panel -> panel.getMetric().equals(metric.value()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(panels)) {
            log.warn("Unable to register metric = {}. Panels for this metric not found in configuration", metric.value());
            return;
        }

        panels.forEach(
                panel -> panel.getTargets().add(buildTimedTarget(metric))
        );
    }

    private void registerCounterMetricToDashboard(List<i.r.grafana.json.processor.model.Dashboard> preparedDashboards, Metric metric) {
        if (CollectionUtils.isEmpty(preparedDashboards)) {
            return;
        }

        List<Panel> panels = preparedDashboards.stream()
                .map(i.r.grafana.json.processor.model.Dashboard::getPanels)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(p -> p.stream())
                .filter(panel -> panel.getMetric().equals(metric.value()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(panels)) {
            log.warn("Unable to register metric = {}. Panels for this metric not found in configuration", metric.value());
            return;
        }

        panels.forEach(
                panel -> panel.getTargets().add(buildCounterTarget(metric))
        );
    }

    private void registerTimedMetricToDashboard(List<i.r.grafana.json.processor.model.Dashboard> preparedDashboards, Timed timedMetric) {
        if (CollectionUtils.isEmpty(preparedDashboards)) {
            return;
        }

        List<Panel> panels = preparedDashboards.stream()
                .map(i.r.grafana.json.processor.model.Dashboard::getPanels)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(p -> p.stream())
                .filter(panel -> panel.getMetric().equals(timedMetric.value()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(panels)) {
            log.warn("Unable to register metric = {}. Panels for this metric not found in configuration", timedMetric.value());
            return;
        }

        panels.forEach(
                panel -> panel.getTargets().add(buildTarget(timedMetric))
        );
    }

    private void registerCounterMetricToDashboard(List<i.r.grafana.json.processor.model.Dashboard> preparedDashboards, Counted counted) {
        if (CollectionUtils.isEmpty(preparedDashboards)) {
            return;
        }

        List<Panel> panels = preparedDashboards.stream()
                .map(i.r.grafana.json.processor.model.Dashboard::getPanels)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(p -> p.stream())
                .filter(panel -> panel.getMetric().equals(counted.value()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(panels)) {
            log.warn("Unable to register metric = {}. Panels for this metric not found in configuration", counted.value());
            return;
        }

        panels.forEach(
                panel -> panel.getTargets().add(buildCounterTarget(counted))
        );

    }

    private Target buildTimedTarget(Metric metric) {
        Target target = new Target();
        target.setExpr(ExpressionBuilder.generateTimedExpression(metric.value(), metric.extraTags(), "1m"));
        target.setLegendFormat(metric.description());
        return target;
    }

    private Target buildTarget(Timed metric) {
        Target target = new Target();
        target.setExpr(ExpressionBuilder.generateTimedExpression(metric.value(), metric.extraTags(), "1m"));
        target.setLegendFormat(metric.description());
        return target;
    }

    private Target buildCounterTarget(Counted metric) {
        Target target = new Target();
        target.setExpr(ExpressionBuilder.generateCounterExpression(metric.value(), metric.extraTags()));
        target.setLegendFormat(metric.description());
        return target;
    }

    private Target buildCounterTarget(Metric metric) {
        Target target = new Target();
        target.setExpr(ExpressionBuilder.generateCounterExpression(metric.value(), metric.extraTags()));
        target.setLegendFormat(metric.description());
        return target;
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_17;
    }

    private void saveDashboardsToFile(List<i.r.grafana.json.processor.model.Dashboard> dashboards) {
        dashboards.forEach(this::saveDashboardToFile);
    }

    private void saveDashboardToFile(i.r.grafana.json.processor.model.Dashboard dashboard) {
        String dashboardOutput = ObjectMapperSerializer.serialize(dashboard);
        if (StringUtils.isEmpty(dashboardOutput)) {
            log.warn("Unable to serialize dashboard = {}", dashboard);
            return;
        }

        String dashboardFileName = "dashboard_" + dashboard.getUid() + ".json";
        log.debug("Saving dashboard {} to file {}", dashboard, dashboardFileName);
        try {
            FileObject dashboardOutputFile = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT,
                    "", dashboardFileName);
            Writer writer = dashboardOutputFile.openWriter();
            writer.write(dashboardOutput);
            writer.close();
        } catch (Exception e) {
            log.error("Unable to save dashboard = {}", dashboard, e);
        }

    }
}
