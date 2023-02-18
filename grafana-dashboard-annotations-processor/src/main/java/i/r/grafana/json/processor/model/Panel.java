package i.r.grafana.json.processor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Panel {

    private int id;

    private String title;
    @JsonIgnore
    private String metric;

    private String type = "timeseries";
    private Datasource datasource;

    private String description;

    private List<Target> targets = new ArrayList<>();

    private GridPos gridPos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GridPos getGridPos() {
        return gridPos;
    }

    public void setGridPos(GridPos gridPos) {
        this.gridPos = gridPos;
    }
}
