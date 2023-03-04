package i.r.grafana.json.demo.config;

import i.r.grafana.json.annotations.Dashboard;
import i.r.grafana.json.annotations.Panel;

@Dashboard(title = "Demo application metrics", panels = {
        @Panel(metric = "timer_as_field", title = "External service invocation time (FIELDS)"),
        @Panel(metric = "timer_as_annotation", title = "External service invocation time (ANNOTATIONS)"),
        @Panel(metric = "counter_as_field", title = "External objects counters (FIELDS)"),
        @Panel(metric = "counter_as_annotation", title = "Methods invocation counters"),
        @Panel(metric = "dynamic_counter", title = "Dynamic counters (invalid use-case)")}
)
public class GrafanaDashboardConfiguration { }
