package i.r.grafana.json.annotations.promethues;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Metric {

    String value();

    String[] extraTags() default {};

    String description() default "";

    boolean rate() default true;

    boolean sum() default true;
}
