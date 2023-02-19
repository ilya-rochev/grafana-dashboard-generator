package i.r.grafana.json.processor.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Import;

@Import({
        TargetFactory.class,
        DashboardResourceHelper.class,
        DashboardGeneratorService.class,
        DashboardFactory.class
})
public class ProcessorConfiguration {

    public static final Logger LOG = LoggerFactory.getLogger("DashboardGeneratorProcessor");

}
