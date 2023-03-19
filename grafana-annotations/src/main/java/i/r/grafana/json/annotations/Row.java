package i.r.grafana.json.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface Row {

    Panel[] panels();

    String title();

}
