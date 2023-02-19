package i.r.grafana.json.processor.component;

import i.r.grafana.json.processor.converter.ObjectMapperSerializer;
import i.r.grafana.json.processor.model.Dashboard;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.Writer;
import java.util.List;

import static i.r.grafana.json.processor.component.ProcessorConfiguration.LOG;
@Component
public class DashboardResourceHelper {

    public void saveDashboardsToFile(List<Dashboard> dashboards, ProcessingEnvironment processingEnv) {
        dashboards.forEach(dashboard ->
                this.saveDashboardToFile(dashboard, processingEnv));
    }

    public void saveDashboardToFile(i.r.grafana.json.processor.model.Dashboard dashboard, ProcessingEnvironment processingEnv) {
        String dashboardOutput = ObjectMapperSerializer.serialize(dashboard);
        if (StringUtils.isEmpty(dashboardOutput)) {
            LOG.warn("Unable to serialize dashboard = {}", dashboard);
            return;
        }

        String dashboardFileName = "dashboard_" + dashboard.getUid() + ".json";
        LOG.debug("Saving dashboard {} to file {}", dashboard, dashboardFileName);
        try {
            FileObject dashboardOutputFile = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT,
                    "", dashboardFileName);
            Writer writer = dashboardOutputFile.openWriter();
            writer.write(dashboardOutput);
            writer.close();
        } catch (Exception e) {
            LOG.error("Unable to save dashboard = {}", dashboard, e);
        }

    }

}
