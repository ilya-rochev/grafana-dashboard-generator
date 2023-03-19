package i.r.grafana.json.processor.util;

import org.apache.commons.lang3.StringUtils;

import static i.r.grafana.json.processor.component.ProcessorConfiguration.LOG;

public class ExpressionBuilder {

    private static final String TIMED_EXPRESSION_TEMPLATE = "sum(rate(%METRIC_NAME%_seconds_sum%TAGS%[%INTERVAL%]))/sum(rate(%METRIC_NAME%_seconds_count%TAGS%[%INTERVAL%]))";
    private static final String TIMED_EXPRESSION_MAX_TEMPLATE = "max(%METRIC_NAME%_seconds_max%TAGS%)";
    private static final String COUNTER_EXPRESSION_TEMPLATE = "%METRIC_NAME%_total%TAGS%";

    private static final String RATE = "rate(%EXPRESSION%[%INTERVAL%])";

    private static final String SUM = "sum(%EXPRESSION%)";
    public static final String generateCounterExpression(String metricName, String[] tags, String rateInterval, boolean sum) {
        String expression = COUNTER_EXPRESSION_TEMPLATE
                .replace("%METRIC_NAME%", metricName)
                .replace("%TAGS%", tagsPart(tags));

        if (StringUtils.isNotEmpty(rateInterval)) {
            expression = RATE
                    .replace("%EXPRESSION%", expression)
                    .replace("%INTERVAL%", rateInterval);
        }

        if (sum) {
            expression = SUM.replace("%EXPRESSION%", expression);
        }

        return expression;
    }

    public static final String generateTimedExpression(String metricName, String[] tags, String interval) {
        return TIMED_EXPRESSION_TEMPLATE
                .replace("%METRIC_NAME%", metricName)
                .replace("%TAGS%", tagsPart(tags))
                .replace("%INTERVAL%", interval);
    }

    public static final String generateMaxExpression(String metricName, String[] tags) {
        return TIMED_EXPRESSION_MAX_TEMPLATE
                .replace("%METRIC_NAME%", metricName)
                .replace("%TAGS%", tagsPart(tags));
    }

    private static String tagsPart(String[] tags) {
        if (tags == null || tags.length == 0) {
            return "";
        }

        if (tags.length % 2 != 0) {
            LOG.warn("tags has odd count. unable to process. tags = {} ", tags);
        }

        StringBuilder tagsStringBuilder = new StringBuilder()
                .append("{");

        int i = 0;
        while (i < tags.length) {
            if (i % 2 == 0) {
                tagsStringBuilder
                        .append(i == 0 ? "" : ",")
                        .append(tags[i]);
            } else {
                tagsStringBuilder
                        .append("=\"")
                        .append(tags[i])
                        .append("\"");
            }
            i++;
        }

        return tagsStringBuilder.append("}").toString();
    }
}
