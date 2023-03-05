package i.r.grafana.json.processor.test;

import i.r.grafana.json.annotations.Dashboard;
import i.r.grafana.json.annotations.Panel;
import i.r.grafana.json.annotations.promethues.Metric;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Dashboard(title = "Demo application metrics", panels = {
        @Panel(metric = "timer_as_field", title = "External service invocation time (FIELDS)"),
        @Panel(metric = "timer_as_annotation", title = "External service invocation time (ANNOTATIONS)"),
        @Panel(metric = "counter_as_field", title = "External objects counters (FIELDS)"),
        @Panel(metric = "counter_as_annotation", title = "Methods invocation counters")}
)
public class GrafanaDashboard {

    @Metric(value = "counter_as_field", extraTags = {"type", "saved"}, description = "Saved object counter")
    private Counter savedObjectsCounter;

    @Metric(value = "counter_as_field", extraTags = {"type", "deleted"}, description = "Deleted objects counter")
    private Counter deletedObjectsCounter;

    @Metric(value = "counter_as_field", extraTags = {"type", "fetched"}, description = "Fetched objects counter")
    private Counter fechedObjectsCounter;
    @Metric(value = "timer_as_field", extraTags = {"type", "save"}, description = "Method 'save' execution time (sec)")
    private Timer savedObjectsTimer;

    @Metric(value = "timer_as_field", extraTags = {"type", "delete"}, description = "Method 'delete' execution time (sec)")
    private Timer deletedObjectsTimer;

    @Metric(value = "timer_as_field", extraTags = {"type", "get"}, description = "Method 'fetch' execution time (sec)")
    private Timer fetchObjectsTimer;

    @Timed(value = "timer_as_annotation", extraTags = {"operation", "save"}, description = "Method 'save' execution time (sec) - annotation")
    @Counted(value = "counter_as_annotation",  extraTags = {"operation", "save"}, description = "method 'save' invocation counter")
    public void save() {}

    @Timed(value = "timer_as_annotation", extraTags = {"operation", "delete"}, description = "Method 'delete' execution time (sec) - annotation")
    @Counted(value = "counter_as_annotation",  extraTags = {"operation", "delete"}, description = "method 'delete' invocation counter")
    public void delete() {}

    @Timed(value = "timer_as_annotation", extraTags = {"operation", "fetch"}, description = "Method 'fetch' execution time (sec) - annotation")
    @Counted(value = "counter_as_annotation",  extraTags = {"operation", "fetch"}, description = "method 'fetch' invocation counter")
    public void fetch() {}

}
