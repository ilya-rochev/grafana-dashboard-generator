package i.r.grafana.json.demo.counter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("counterFactory")
public class CounterFactory {

    @Autowired
    private MeterRegistry meterRegistry;

    public Counter counter(String name, Iterable<Tag> tags) {
        return meterRegistry.counter(name, tags);
    }

    public Counter counter(String name, String... tags) {
        return meterRegistry.counter(name, tags);
    }
}
