# Grafana Dashboard Generator
Annotation processor which automatically generates Grafana Dashboard JSON model based on metrics declared in application.

## How to use library

1. Add next dependencies to pom.xml
```
<dependency>
    <groupId>i.r.grafana-json-generator</groupId>
    <artifactId>grafana-dashboard-annotations-processor</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>i.r.grafana-json-generator</groupId>
    <artifactId>prometheus-metric-spring-boot-starter</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

2. Migrate creating `Counter` and `Timer` beans to processed by `prometheus-metric-spring-boot-starter` annotation `i.r.grafana.json.annotations.promethues.@Metric`.

If you create metrics like this
```
@Configuration
@RequiredArgsConstructor
public class MetricConfig {

    private final MeterRegistry meterRegistry;

    @Bean
    public Counter savedObjectsCounter() {
        return meterRegistry.counter("counter_as_field",  "type", "saved");
    }
    
    ...  
}
```
Change creating metrics declarative way using `@Metric` annotation
```
@Service
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class ExternalCounterServiceStub {

    @Metric(value = "counter_as_field", extraTags = {"type", "saved"}, description = "Saved object counter")
    private final Counter savedObjectsCounter;
    
    ...
}   
```

Notice: `prometheus-metric-spring-boot-starter` processes annotation `@Metric` during starting application. 
It creates `Timer` or `Counter` bean with name and tags you provided as annotation parameters.

3. Declare you Grafana dashboard using annotations from package `i.r.grafana.json.annotations`

```
@Dashboard(title = "Demo application metrics", panels = {
        @Panel(metric = "timer_as_field", title = "External service invocation time (FIELDS)"),
        @Panel(metric = "timer_as_annotation", title = "External service invocation time (ANNOTATIONS)"),
        @Panel(metric = "counter_as_field", title = "External objects counters (FIELDS)"),
        @Panel(metric = "counter_as_annotation", title = "External objects counters (ANNOTATION)")}
)
public class GrafanaDashboardConfiguration { }
```

Notice: `grafana-dashboard-annotations-processor` processes annotations from package `i.r.grafana.json.annotations` during compile. 
It generates dashboard model and injects metrics you declared using `@Metric` annotation;

4. Build your project. Created grafana json model will be stored in `target\classes\dasboard_{uuid}.json` file, for example - `dashboard_221a4f4c-2d60-4ec0-9fa5-7c828fee81eb.json` 

5. Run your project and import generated json file to grafana. Enjoy to view you custom application metrics.


# Demo application

Requires:

    - "docker" runned at your machine;


docker build --platform linux/amd64 -t grafana-test-application .


docker build --platform linux/amd64 -t grafana-test-application .


How to test demo application:


1. Start locally grafana and prometheus in docker:
```
docker-compose up -d
```

2. Build project:
```
./mvnw clean install
```

3. Start demo application;

4. Run several invocation next rest api methods with various parameters `objects`

- method save:
```
POST http://localhost:8080/api/external/save?objects=object1,object2,object3
```
- method delete:
```
POST http://localhost:8080/api/external/delete?objects=object1,object2,object3
```
- method fetch:
```
POST http://localhost:8080/api/external/fetch?objects=object1,object2,object3
```