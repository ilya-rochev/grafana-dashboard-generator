package i.r.grafana.json.processor.model;

public class Target {

    private Datasource datasource;

    private String editorMode;

    private String expr;

    private String legendFormat;

    private boolean range;

    private String refId;

    public Datasource getDatasource() {
        return datasource;
    }

    public void setDatasource(Datasource datasource) {
        this.datasource = datasource;
    }

    public String getEditorMode() {
        return editorMode;
    }

    public void setEditorMode(String editorMode) {
        this.editorMode = editorMode;
    }

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public String getLegendFormat() {
        return legendFormat;
    }

    public void setLegendFormat(String legendFormat) {
        this.legendFormat = legendFormat;
    }

    public boolean isRange() {
        return range;
    }

    public void setRange(boolean range) {
        this.range = range;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }
}
