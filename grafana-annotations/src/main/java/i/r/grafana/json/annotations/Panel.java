package i.r.grafana.json.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface Panel {

    String metric();

    String title() default "";

    String description() default "";

}
