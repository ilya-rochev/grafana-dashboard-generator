# Grafana Dashboard Generator
Annotation processor which automatically generates Grafana Dashboard JSON model based on metrics declared in application.

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