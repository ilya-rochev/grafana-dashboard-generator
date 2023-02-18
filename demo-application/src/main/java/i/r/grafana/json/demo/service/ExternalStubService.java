package i.r.grafana.json.demo.service;

import i.r.grafana.json.annotations.promethues.Metric;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Stub-service which emulates any external service client;
 * Every method of this service emulates execution time by invoking {@link Thread#sleep(long)} method
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class ExternalStubService {

    private static final long SAVE_TIMEOUT_MS = 100;
    private static final long DELETE_TIMEOUT_MS = 50;
    private static final long GET_TIMEOUT_MS = 200;


    @Metric(value = "external_stub_parameters", extraTags = {"type", "saved"}, description = "Saved objects count")
    private final Counter savedObjectsCounter;

    @Metric(value = "external_stub_parameters", extraTags = {"type", "deleted"}, description = "Deleted objects count")
    private final Counter deletedObjectsCounter;

    @Metric(value = "external_stub_parameters", extraTags = {"type", "get"}, description = "Fetched objects count")
    private final Counter fetchObjectsCounter;

    @Timed(value = "external_stub_timer", extraTags = {"operation", "save"}, description = "Method 'save' execution time (sec)")
    public void save(List<String> objects) {
        savedObjectsCounter.increment(CollectionUtils.size(objects));
        sleep(SAVE_TIMEOUT_MS);
    }

    @Timed(value = "external_stub_timer", extraTags = {"operation", "delete"}, description = "Method 'delete' execution time (sec)")
    public void delete(List<String> objects) {
        deletedObjectsCounter.increment(CollectionUtils.size(objects));
        sleep(DELETE_TIMEOUT_MS);
    }

    @Timed(value = "external_stub_timer", extraTags = {"operation", "fetch"}, description = "Method 'fetch' execution time (sec)")
    public void fetch(List<String> objects) {
        fetchObjectsCounter.increment(CollectionUtils.size(objects));
        sleep(GET_TIMEOUT_MS);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            log.error("Error during timeout");
        }
    }
}
