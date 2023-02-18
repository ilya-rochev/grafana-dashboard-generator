package i.r.grafana.json.processor.model;

import java.util.List;
import java.util.UUID;

public class Dashboard {

    private int id;

    private String title;

    private String uid = UUID.randomUUID().toString();

    private List<Panel> panels;

    private boolean editable = true;

    private int fiscalYearStartMonth = 0;

    private int graphTooltip = 0;

    private List<String> links = List.of();

    private boolean liveNow = false;

    private String refresh = "5s";

    private Time time = new Time();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Panel> getPanels() {
        return panels;
    }

    public void setPanels(List<Panel> panels) {
        this.panels = panels;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getFiscalYearStartMonth() {
        return fiscalYearStartMonth;
    }

    public void setFiscalYearStartMonth(int fiscalYearStartMonth) {
        this.fiscalYearStartMonth = fiscalYearStartMonth;
    }

    public int getGraphTooltip() {
        return graphTooltip;
    }

    public void setGraphTooltip(int graphTooltip) {
        this.graphTooltip = graphTooltip;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public boolean isLiveNow() {
        return liveNow;
    }

    public void setLiveNow(boolean liveNow) {
        this.liveNow = liveNow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
