package i.r.grafana.json.processor.component;

import i.r.grafana.json.annotations.Dashboard;
import i.r.grafana.json.processor.converter.ObjectMapperSerializer;
import i.r.grafana.json.processor.model.Panel;
import i.r.grafana.json.processor.util.GridPosFactory;
import org.springframework.stereotype.Component;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

import static i.r.grafana.json.processor.component.ProcessorConfiguration.LOG;
@Component
public class DashboardFactory {

    public List<i.r.grafana.json.processor.model.Dashboard> getDashboardsFromEnvironment(RoundEnvironment roundEnvironment) {
        List<i.r.grafana.json.processor.model.Dashboard> dashboards = new ArrayList<>();
        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Dashboard.class)) {
            Dashboard dashboardAnnotation = annotatedElement.getAnnotation(Dashboard.class);
            LOG.info("Creating dashboard with name ----> {}", dashboardAnnotation.title());

            i.r.grafana.json.processor.model.Dashboard dashboard = build(dashboardAnnotation);
            LOG.debug("Prepared dashboard {}", ObjectMapperSerializer.serialize(dashboard));
            dashboards.add(dashboard);
        }

        return dashboards;
    }

    public i.r.grafana.json.processor.model.Dashboard build(Dashboard source) {
        int id = 1;

        i.r.grafana.json.processor.model.Dashboard target = new i.r.grafana.json.processor.model.Dashboard();
        target.setId(id++);
        target.setTitle(source.title());

        target.setPanels(toPanels(source.panels(), id));

        return target;
    }

    private List<Panel> toPanels(i.r.grafana.json.annotations.Panel[] sourceList, int id) {
        if (sourceList == null || sourceList.length == 0) {
            return List.of();
        }

        List<Panel> panels = new ArrayList<>();
        int panelIndex = 1;
        for (i.r.grafana.json.annotations.Panel source : sourceList) {
            panels.add(toPanel(source, id++, panelIndex++));
        }

        return panels;
    }

    private Panel toPanel(i.r.grafana.json.annotations.Panel source, int id, int panelIndex) {
        Panel panel = new Panel();
        panel.setId(id);
        panel.setMetric(source.metric());
        panel.setDescription(source.description());
        panel.setTitle(source.title());
        panel.setGridPos(GridPosFactory.build(8, 12, panelIndex));
        return panel;
    }


}
