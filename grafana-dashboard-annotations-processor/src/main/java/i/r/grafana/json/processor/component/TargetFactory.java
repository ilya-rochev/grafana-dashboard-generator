package i.r.grafana.json.processor.component;

import i.r.grafana.json.annotations.promethues.Metric;
import i.r.grafana.json.processor.model.Target;
import i.r.grafana.json.processor.util.ExpressionBuilder;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class TargetFactory {

    private static final String INTERVAL_DEFAULT = "1m";

    public Target buildCounterTarget(Metric metric) {
        String rateInterval = metric.rate() ? INTERVAL_DEFAULT : StringUtils.EMPTY;
        boolean sum = metric.sum();

        Target target = new Target();
        target.setMetric(metric.value());
        target.setExpr(ExpressionBuilder.generateCounterExpression(metric.value(), metric.extraTags(), rateInterval, sum));
        target.setLegendFormat(metric.description());
        return target;
    }

    public Target buildCounterTarget(Counted metric) {
        Target target = new Target();
        target.setMetric(metric.value());
        target.setExpr(ExpressionBuilder.generateCounterExpression(metric.value(), metric.extraTags(), INTERVAL_DEFAULT, true));
        target.setLegendFormat(metric.description());
        return target;
    }

    public Target buildTimerTarget(Metric metric) {
        Target target = new Target();
        target.setMetric(metric.value());
        target.setExpr(ExpressionBuilder.generateTimedExpression(metric.value(), metric.extraTags(), INTERVAL_DEFAULT));
        target.setLegendFormat(metric.description() + " [AVG]");
        return target;
    }

    public Target buildTimerTarget(Timed metric) {
        Target target = new Target();
        target.setMetric(metric.value());
        target.setExpr(ExpressionBuilder.generateTimedExpression(metric.value(), metric.extraTags(), INTERVAL_DEFAULT));
        target.setLegendFormat(metric.description() + " [AVG]");
        return target;
    }

    public Target buildMaxTarget(Metric metric) {
        Target target = new Target();
        target.setMetric(metric.value());
        target.setExpr(ExpressionBuilder.generateMaxExpression(metric.value(), metric.extraTags()));
        target.setLegendFormat(metric.description() + " [MAX]");
        return target;
    }

    public Target buildMaxTarget(Timed metric) {
        Target target = new Target();
        target.setMetric(metric.value());
        target.setExpr(ExpressionBuilder.generateMaxExpression(metric.value(), metric.extraTags()));
        target.setLegendFormat(metric.description() + " [MAX]");
        return target;
    }
}
