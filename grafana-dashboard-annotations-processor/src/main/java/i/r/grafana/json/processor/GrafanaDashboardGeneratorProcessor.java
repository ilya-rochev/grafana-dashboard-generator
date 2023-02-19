package i.r.grafana.json.processor;

import i.r.grafana.json.processor.component.DashboardGeneratorService;
import i.r.grafana.json.processor.component.ProcessorConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static i.r.grafana.json.processor.component.ProcessorConfiguration.LOG;

@SupportedAnnotationTypes({
        "i.r.grafana.json.annotations.Dashboard",
        "i.r.grafana.json.annotations.Panel"
})
public class GrafanaDashboardGeneratorProcessor extends AbstractProcessor {

    private DashboardGeneratorService dashboardGeneratorService;

    private void init() {
        LOG.info("Initializing annotation context ...");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ProcessorConfiguration.class);
        this.dashboardGeneratorService = ctx.getBean(DashboardGeneratorService.class);
        LOG.info("Annotation processor context initialized successfully ...");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        LOG.info("annotation processor invoked");
        init();
        return dashboardGeneratorService.generate(roundEnvironment, processingEnv);
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_17;
    }

}
