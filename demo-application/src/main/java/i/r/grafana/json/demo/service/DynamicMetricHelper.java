package i.r.grafana.json.demo.service;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Note: this example informs that dynamic metrics unable to parse during
 */

@Component
@RequiredArgsConstructor
public class DynamicMetricHelper {

    private final MeterRegistry meterRegistry;

    public void dynamicCounter(String operation) {
        meterRegistry.counter("dynamic_counter", "operation", operation)
                .increment();
    }

}
