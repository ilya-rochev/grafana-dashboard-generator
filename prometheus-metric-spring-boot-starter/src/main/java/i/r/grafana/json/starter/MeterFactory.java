package i.r.grafana.json.starter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("meterFactory")
@RequiredArgsConstructor
public class MeterFactory {

    public static final String BEAN_NAME = "meterFactory";
    public static final String BUILD_COUNTER_METHOD_NAME = "counter";
    public static final String BUILD_TIMER_METHOD_NAME = "timer";

    private final MeterRegistry meterRegistry;

    public Counter counter(String name, String... tags) {
        return meterRegistry.counter(name, tags);
    }

    public Timer timer(String name, String... tags) {
        return meterRegistry.timer(name, tags);
    }
}
