package i.r.grafana.json.starter;

import i.r.grafana.json.annotations.promethues.Metric;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestMetrics {

    @Metric(value = "counter_test", extraTags = {"counter_tag_key_1", "counter_tag_value_1", "counter_tag_key_2", "counter_tag_value_2"}, description = "Counter test description")
    private final Counter testCounter;

    @Metric(value = "counter_test_without_tags", description = "Counter test description")
    private final Counter testCounterWithoutTags;

    @Metric(value = "timer_test", extraTags = {"timer_tag_key_1", "timer_tag_value_1", "timer_tag_key_2", "timer_tag_value_2"}, description = "Timer test description")
    private final Timer testTimer;

    @Metric(value = "timer_test_without_tags", description = "Timer test description")
    private final Timer testTimerWithoutTags;

}
