package i.r.grafana.json.processor.model;

public class Time {

    private String from = "now-30m";

    private String to = "now";

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
