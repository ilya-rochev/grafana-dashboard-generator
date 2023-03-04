package i.r.grafana.json.starter;

import i.r.grafana.json.annotations.promethues.Metric;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class PrometheusMetricsRegistrar implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Map<String, Object> applicationBeans = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
        String basePackage = applicationBeans.isEmpty() ? null : applicationBeans.values().toArray()[0].getClass().getPackageName();
        buildMetrics(basePackage, registry);
    }

    private void buildMetrics(String basePackage, BeanDefinitionRegistry registry) {
        Reflections reflections1 = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(basePackage)).setScanners(Scanners.FieldsAnnotated));
        Set<Field> fields = reflections1.getFieldsAnnotatedWith(Metric.class);
        if (CollectionUtils.isEmpty(fields)) {
            log.debug("no fields with {} annotation presented. Skipping...", Metric.class.getCanonicalName());
            return;
        }

        fields.forEach(field -> this.prepareMetric(field, registry));
    }

    private void prepareMetric(Field field, BeanDefinitionRegistry registry) {
        Metric metric = field.getAnnotation(Metric.class);
        GenericBeanDefinition metricBeanDefinition = prepareBeanDefinition(metric, field.getType());
        if (metricBeanDefinition == null) {
            log.warn("Unable to recognize metric type to inject it ... filed = {}", field);
            return;
        }

        registry.registerBeanDefinition(field.getName(), metricBeanDefinition);
    }

    private GenericBeanDefinition prepareBeanDefinition(Metric metric, Class<?> type) {
        if (type == Counter.class) {
            return prepareMetricBeanDefinition(
                    MeterFactory.BEAN_NAME,
                    MeterFactory.BUILD_COUNTER_METHOD_NAME,
                    type,
                    metric);
        } else if (type == Timer.class) {
            return prepareMetricBeanDefinition(
                    MeterFactory.BEAN_NAME,
                    MeterFactory.BUILD_TIMER_METHOD_NAME,
                    type,
                    metric);
        } else {
            return null;
        }

    }

    private GenericBeanDefinition prepareMetricBeanDefinition(String factoryBeanName,
                                                              String factoryMethodName,
                                                              Class<?> type,
                                                              Metric metric) {
        GenericBeanDefinition metricBeanDefinition = new GenericBeanDefinition();
        metricBeanDefinition.setFactoryBeanName(factoryBeanName);
        metricBeanDefinition.setFactoryMethodName(factoryMethodName);
        metricBeanDefinition.setBeanClass(type);
        metricBeanDefinition.getConstructorArgumentValues()
                .addGenericArgumentValue(metric.value());
        metricBeanDefinition.getConstructorArgumentValues()
                .addGenericArgumentValue(metric.extraTags());
        return metricBeanDefinition;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
