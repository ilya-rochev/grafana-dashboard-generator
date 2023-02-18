package i.r.grafana.json.processor.converter;

import i.r.grafana.json.annotations.Dashboard;
import i.r.grafana.json.processor.model.Panel;
import i.r.grafana.json.processor.util.GridPosFactory;

import java.util.ArrayList;
import java.util.List;

public class DashboardFactory {

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
