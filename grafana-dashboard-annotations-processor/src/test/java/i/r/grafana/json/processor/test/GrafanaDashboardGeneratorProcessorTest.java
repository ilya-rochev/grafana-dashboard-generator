package i.r.grafana.json.processor.test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import i.r.grafana.json.processor.GrafanaDashboardGeneratorProcessor;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.testing.compile.Compiler.javac;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class GrafanaDashboardGeneratorProcessorTest {

    private static final String GENERATED_JSON_PATTERN = "^(.*)dashboard_(.*)\\.json$";

    @Test
    public void test_dashboard_generate() throws Exception {
        Compilation compilation = javac()
                .withProcessors(new GrafanaDashboardGeneratorProcessor())
                .compile(JavaFileObjects.forResource("GrafanaDashboard.java"));
        com.google.testing.compile.CompilationSubject.assertThat(compilation).succeededWithoutWarnings();

        List<JavaFileObject> generatedJsonFiles = compilation.generatedFiles()
                .stream()
                .filter(javaFileObject -> javaFileObject.getName().matches(GENERATED_JSON_PATTERN))
                .collect(Collectors.toList());

        assertThat(generatedJsonFiles, hasSize(1));
        JavaFileObject file = generatedJsonFiles.get(0);


        String dashboard = file.getCharContent(true)
                .toString();

        assertThat(dashboard, hasJsonPath("$.id", equalTo(1)));
        assertThat(dashboard, hasJsonPath("$.title", equalTo("Demo application metrics")));
        assertThat(dashboard, hasJsonPath("$.uid", not(isEmptyString())));
        assertThat(dashboard, hasJsonPath("$.editable", equalTo(true)));
        assertThat(dashboard, hasJsonPath("$.fiscalYearStartMonth", equalTo(0)));
        assertThat(dashboard, hasJsonPath("$.liveNow", equalTo(false)));
        assertThat(dashboard, hasJsonPath("$.refresh", equalTo("5s")));
        assertThat(dashboard, hasJsonPath("$.time.from", equalTo("now-30m")));
        assertThat(dashboard, hasJsonPath("$.time.to", equalTo("now")));

        assertThat(dashboard, hasJsonPath("$.panels", hasSize(4)));

        /**
         * Check panel 0
         */
        assertThat(dashboard, hasJsonPath("$.panels[0].id", equalTo(2)));
        assertThat(dashboard, hasJsonPath("$.panels[0].title", equalTo("External service invocation time (FIELDS)")));
        assertThat(dashboard, hasJsonPath("$.panels[0].type", equalTo("timeseries")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets", hasSize(6)));

        assertThat(dashboard, hasJsonPath("$.panels[0].targets[0].expr", equalTo("sum(rate(timer_as_field_seconds_sum{type=\"save\"}[1m]))/sum(rate(timer_as_field_seconds_count{type=\"save\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[0].legendFormat", equalTo("Method 'save' execution time (sec) [AVG]")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[0].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[0].targets[1].expr", equalTo("max(timer_as_field_seconds_max{type=\"save\"})")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[1].legendFormat", equalTo("Method 'save' execution time (sec) [MAX]")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[1].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[0].targets[2].expr", equalTo("sum(rate(timer_as_field_seconds_sum{type=\"delete\"}[1m]))/sum(rate(timer_as_field_seconds_count{type=\"delete\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[2].legendFormat", equalTo("Method 'delete' execution time (sec) [AVG]")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[2].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[0].targets[3].expr", equalTo("max(timer_as_field_seconds_max{type=\"delete\"})")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[3].legendFormat", equalTo("Method 'delete' execution time (sec) [MAX]")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[3].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[0].targets[4].expr", equalTo("sum(rate(timer_as_field_seconds_sum{type=\"get\"}[1m]))/sum(rate(timer_as_field_seconds_count{type=\"get\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[4].legendFormat", equalTo("Method 'fetch' execution time (sec) [AVG]")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[4].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[0].targets[5].expr", equalTo("max(timer_as_field_seconds_max{type=\"get\"})")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[5].legendFormat", equalTo("Method 'fetch' execution time (sec) [MAX]")));
        assertThat(dashboard, hasJsonPath("$.panels[0].targets[5].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[0].gridPos.h", equalTo(8)));
        assertThat(dashboard, hasJsonPath("$.panels[0].gridPos.w", equalTo(12)));
        assertThat(dashboard, hasJsonPath("$.panels[0].gridPos.x", equalTo(0)));
        assertThat(dashboard, hasJsonPath("$.panels[0].gridPos.y", equalTo(-8)));

        /**
         * Check panel 1
         */
        assertThat(dashboard, hasJsonPath("$.panels[1].id", equalTo(3)));
        assertThat(dashboard, hasJsonPath("$.panels[1].title", equalTo("External service invocation time (ANNOTATIONS)")));
        assertThat(dashboard, hasJsonPath("$.panels[1].type", equalTo("timeseries")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets", hasSize(6)));

        assertThat(dashboard, hasJsonPath("$.panels[1].targets[0].expr", equalTo("sum(rate(timer_as_annotation_seconds_sum{operation=\"save\"}[1m]))/sum(rate(timer_as_annotation_seconds_count{operation=\"save\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[0].legendFormat", equalTo("Method 'save' execution time (sec) - annotation [AVG]")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[0].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[1].targets[1].expr", equalTo("max(timer_as_annotation_seconds_max{operation=\"save\"})")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[1].legendFormat", equalTo("Method 'save' execution time (sec) - annotation [MAX]")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[1].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[1].targets[2].expr", equalTo("sum(rate(timer_as_annotation_seconds_sum{operation=\"delete\"}[1m]))/sum(rate(timer_as_annotation_seconds_count{operation=\"delete\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[2].legendFormat", equalTo("Method 'delete' execution time (sec) - annotation [AVG]")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[2].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[1].targets[3].expr", equalTo("max(timer_as_annotation_seconds_max{operation=\"delete\"})")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[3].legendFormat", equalTo("Method 'delete' execution time (sec) - annotation [MAX]")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[3].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[1].targets[4].expr", equalTo("sum(rate(timer_as_annotation_seconds_sum{operation=\"fetch\"}[1m]))/sum(rate(timer_as_annotation_seconds_count{operation=\"fetch\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[4].legendFormat", equalTo("Method 'fetch' execution time (sec) - annotation [AVG]")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[4].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[1].targets[5].expr", equalTo("max(timer_as_annotation_seconds_max{operation=\"fetch\"})")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[5].legendFormat", equalTo("Method 'fetch' execution time (sec) - annotation [MAX]")));
        assertThat(dashboard, hasJsonPath("$.panels[1].targets[5].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[1].gridPos.h", equalTo(8)));
        assertThat(dashboard, hasJsonPath("$.panels[1].gridPos.w", equalTo(12)));
        assertThat(dashboard, hasJsonPath("$.panels[1].gridPos.x", equalTo(12)));
        assertThat(dashboard, hasJsonPath("$.panels[1].gridPos.y", equalTo(-8)));

        /**
         * Check panel 2
         */
        assertThat(dashboard, hasJsonPath("$.panels[2].id", equalTo(4)));
        assertThat(dashboard, hasJsonPath("$.panels[2].title", equalTo("External objects counters (FIELDS)")));
        assertThat(dashboard, hasJsonPath("$.panels[2].type", equalTo("timeseries")));
        assertThat(dashboard, hasJsonPath("$.panels[2].targets", hasSize(3)));

        assertThat(dashboard, hasJsonPath("$.panels[2].targets[0].expr", equalTo("sum(rate(counter_as_field_total{type=\"saved\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[2].targets[0].legendFormat", equalTo("Saved object counter")));
        assertThat(dashboard, hasJsonPath("$.panels[2].targets[0].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[2].targets[1].expr", equalTo("sum(rate(counter_as_field_total{type=\"deleted\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[2].targets[1].legendFormat", equalTo("Deleted objects counter")));
        assertThat(dashboard, hasJsonPath("$.panels[2].targets[1].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[2].targets[2].expr", equalTo("sum(rate(counter_as_field_total{type=\"fetched\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[2].targets[2].legendFormat", equalTo("Fetched objects counter")));
        assertThat(dashboard, hasJsonPath("$.panels[2].targets[2].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[2].gridPos.h", equalTo(8)));
        assertThat(dashboard, hasJsonPath("$.panels[2].gridPos.w", equalTo(12)));
        assertThat(dashboard, hasJsonPath("$.panels[2].gridPos.x", equalTo(0)));
        assertThat(dashboard, hasJsonPath("$.panels[2].gridPos.y", equalTo(0)));

        /**
         * Check panel 3
         */
        assertThat(dashboard, hasJsonPath("$.panels[3].id", equalTo(5)));
        assertThat(dashboard, hasJsonPath("$.panels[3].title", equalTo("Methods invocation counters")));
        assertThat(dashboard, hasJsonPath("$.panels[3].type", equalTo("timeseries")));
        assertThat(dashboard, hasJsonPath("$.panels[3].targets", hasSize(3)));

        assertThat(dashboard, hasJsonPath("$.panels[3].targets[0].expr", equalTo("sum(rate(counter_as_annotation_total{operation=\"save\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[3].targets[0].legendFormat", equalTo("method 'save' invocation counter")));
        assertThat(dashboard, hasJsonPath("$.panels[3].targets[0].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[3].targets[1].expr", equalTo("sum(rate(counter_as_annotation_total{operation=\"delete\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[3].targets[1].legendFormat", equalTo("method 'delete' invocation counter")));
        assertThat(dashboard, hasJsonPath("$.panels[3].targets[1].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[3].targets[2].expr", equalTo("sum(rate(counter_as_annotation_total{operation=\"fetch\"}[1m]))")));
        assertThat(dashboard, hasJsonPath("$.panels[3].targets[2].legendFormat", equalTo("method 'fetch' invocation counter")));
        assertThat(dashboard, hasJsonPath("$.panels[3].targets[2].range", equalTo(false)));

        assertThat(dashboard, hasJsonPath("$.panels[3].gridPos.h", equalTo(8)));
        assertThat(dashboard, hasJsonPath("$.panels[3].gridPos.w", equalTo(12)));
        assertThat(dashboard, hasJsonPath("$.panels[3].gridPos.x", equalTo(12)));
        assertThat(dashboard, hasJsonPath("$.panels[3].gridPos.y", equalTo(0)));

    }

}