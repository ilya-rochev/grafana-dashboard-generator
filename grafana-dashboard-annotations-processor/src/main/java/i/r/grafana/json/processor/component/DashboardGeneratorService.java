package i.r.grafana.json.processor.component;

import i.r.grafana.json.annotations.promethues.Metric;
import i.r.grafana.json.processor.model.Dashboard;
import i.r.grafana.json.processor.model.Panel;
import i.r.grafana.json.processor.model.Target;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static i.r.grafana.json.processor.component.ProcessorConfiguration.LOG;

@Component
public class DashboardGeneratorService {

    @Autowired
    private DashboardFactory dashboardFactory;

    @Autowired
    private TargetFactory targetFactory;

    @Autowired
    private DashboardResourceHelper dashboardResourceHelper;

    public boolean generate(RoundEnvironment roundEnvironment, ProcessingEnvironment processingEnv) {
        List<Dashboard> dashboards = dashboardFactory.getDashboardsFromEnvironment(roundEnvironment);
        if (CollectionUtils.isEmpty(dashboards)) {
            LOG.info("No dashboards annotation found. Skipping ...");
            return true;
        }

        List<Target> timedAnnotationTargets = getTimedAnnotationTargets(roundEnvironment);
        registerTargetsInDashboards(dashboards, timedAnnotationTargets);

        List<Target> countedAnnotationTargets = getCountedAnnotationTargets(roundEnvironment);
        registerTargetsInDashboards(dashboards, countedAnnotationTargets);

        List<Target> metricAnnotationTargets = getMetricAnnotationTargets(roundEnvironment);
        registerTargetsInDashboards(dashboards, metricAnnotationTargets);

        dashboardResourceHelper.saveDashboardsToFile(dashboards, processingEnv);
        LOG.info("PROCESS END");
        return true;
    }

    private void registerTargetsInDashboards(List<i.r.grafana.json.processor.model.Dashboard> dashboards, List<Target> targets) {
        if (CollectionUtils.isEmpty(dashboards) || CollectionUtils.isEmpty(targets)) {
            return;
        }

        targets.forEach(target -> this.registerTargetInDashboards(dashboards, target));
    }

    private void registerTargetInDashboards(List<i.r.grafana.json.processor.model.Dashboard> dashboards, Target target) {
        List<Panel> panels = dashboards.stream()
                .map(i.r.grafana.json.processor.model.Dashboard::getPanels)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(p -> p.stream())
                .filter(panel -> panel.getMetric().equals(target.getMetric()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(panels)) {
            LOG.warn("Unable to register target = {}. Panels for this metric not found in configuration", target);
            return;
        }

        panels.forEach(panel ->
                panel.getTargets().add(target)
        );
    }

    private List<Target> getMetricAnnotationTargets(RoundEnvironment roundEnvironment) {
        List<Target> targetList = new ArrayList<>();

        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Metric.class)) {
            Target target = null;

            Metric metric = annotatedElement.getAnnotation(Metric.class);
            if (!annotatedElement.getKind().isField()) {
                LOG.warn("{} annotation should be field-level", Metric.class);
            } else {
                String typeName = annotatedElement.asType().toString();
                if (typeName.equals(Counter.class.getCanonicalName())) {
                    LOG.info("registering counter metric {}", metric);
                    target = targetFactory.buildCounterTarget(metric);
                } else if (typeName.equals(Timer.class.getCanonicalName())) {
                    LOG.info("registering timer metric {}", metric);
                    target = targetFactory.buildTimerTarget(metric);
                } else {
                    LOG.warn("Annotation {} should be presented only on next classes fields {}", Metric.class, List.of(Timer.class, Counter.class));
                }
            }

            if (target != null) {
                targetList.add(target);
            }
        }

        return targetList;
    }

    private List<Target> getCountedAnnotationTargets(RoundEnvironment roundEnvironment) {
        List<Target> targets = new ArrayList<>();
        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Counted.class)) {
            Counted counted = annotatedElement.getAnnotation(Counted.class);
            LOG.debug("Counter metric found {} ", counted);
            Target target = targetFactory.buildCounterTarget(counted);
            LOG.debug("Counter metric = {}. Its target = {} ", counted, target);
            targets.add(target);
        }

        return targets;
    }

    private List<Target> getTimedAnnotationTargets(RoundEnvironment roundEnvironment) {
        List<Target> targets = new ArrayList<>();
        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Timed.class)) {
            Timed timed = annotatedElement.getAnnotation(Timed.class);
            LOG.debug("Timed metric found {} ", timed);
            Target target = targetFactory.buildTimerTarget(timed);
            LOG.debug("Timed metric = {}. Its target = {} ", timed, target);
            targets.add(target);
        }

        return targets;
    }

}
