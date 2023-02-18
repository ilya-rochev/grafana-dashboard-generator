package i.r.grafana.json.demo.service;

import i.r.grafana.json.annotations.promethues.Metric;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class ExternalCounterServiceStub {

    @Metric(value = "counter_as_field", extraTags = {"type", "saved"}, description = "Saved object counter")
    private final Counter savedObjectsCounter;

    @Metric(value = "counter_as_field", extraTags = {"type", "deleted"}, description = "Deleted objects counter")
    private final Counter deletedObjectsCounter;

    @Metric(value = "counter_as_field", extraTags = {"type", "fetched"}, description = "Fetched objects counter")
    private final Counter fechedObjectsCounter;




    @Counted(value = "counter_as_annotation",  extraTags = {"operation", "save"})
    public void save(List<String> objects) {
        savedObjectsCounter.increment(CollectionUtils.size(objects));
    }

    @Counted(value = "counter_as_annotation",  extraTags = {"operation", "delete"})
    public void delete(List<String> objects) {
        deletedObjectsCounter.increment(CollectionUtils.size(objects));
    }

    @Counted(value = "counter_as_annotation",  extraTags = {"operation", "fetch"})
    public void fetch(List<String> objects) {
        fechedObjectsCounter.increment(CollectionUtils.size(objects));
    }
}
