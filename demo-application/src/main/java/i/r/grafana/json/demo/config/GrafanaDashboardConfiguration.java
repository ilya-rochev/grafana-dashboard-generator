package i.r.grafana.json.demo.config;

import i.r.grafana.json.annotations.Dashboard;
import i.r.grafana.json.annotations.Panel;

@Dashboard(title = "Demo application metrics", panels = {
        @Panel(metric = "external_stub_parameters", title = "External objects counters"),
        @Panel(metric = "external_stub_timer", title = "External service invocation time")}
)
public class GrafanaDashboardConfiguration { }
