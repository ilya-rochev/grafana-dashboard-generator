package i.r.grafana.json.demo.service;

import i.r.grafana.json.annotations.promethues.Metric;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Stub-service which emulates any external service client;
 * Every method of this service emulates execution time by invoking {@link Thread#sleep(long)} method
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class ExternalTimerServiceStub {

    private static final long SAVE_TIMEOUT_MS = 100;
    private static final long DELETE_TIMEOUT_MS = 50;
    private static final long FETCH_TIMEOUT_MS = 200;


    @Metric(value = "timer_as_field", extraTags = {"type", "saved"}, description = "Method 'save' execution time (sec)")
    private final Timer savedObjectsTimer;

    @Metric(value = "timer_as_field", extraTags = {"type", "deleted"}, description = "Method 'delete' execution time (sec)")
    private final Timer deletedObjectsTimer;

    @Metric(value = "timer_as_field", extraTags = {"type", "get"}, description = "Method 'fetch' execution time (sec)")
    private final Timer fetchObjectsTimer;

    @Timed(value = "timer_as_annotation", extraTags = {"operation", "save"}, description = "Method 'save' execution time (sec)")
    public void save() {
        savedObjectsTimer.record(SAVE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        sleep(SAVE_TIMEOUT_MS);
    }

    @Timed(value = "timer_as_annotation", extraTags = {"operation", "delete"}, description = "Method 'delete' execution time (sec)")
    public void delete() {
        deletedObjectsTimer.record(DELETE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        sleep(DELETE_TIMEOUT_MS);
    }

    @Timed(value = "timer_as_annotation", extraTags = {"operation", "fetch"}, description = "Method 'fetch' execution time (sec)")
    public void fetch() {
        fetchObjectsTimer.record(FETCH_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        sleep(FETCH_TIMEOUT_MS);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            log.error("Error during timeout");
        }
    }
}
