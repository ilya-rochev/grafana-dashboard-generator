package i.r.grafana.json.starter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrometheusMetricsRegistrarTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void test_counter() {
        Object bean = context.getBean("testCounter");
        assertTrue(bean instanceof Counter);
        Counter testCounter = (Counter) bean;

        assertNotNull(testCounter);
        assertNotNull(testCounter.getId());
        assertEquals(testCounter.getId().getName(), "counter_test");
        assertNotNull(testCounter.getId().getTags());
        assertTrue(testCounter.getId().getTags().size() == 2);

        assertEquals(testCounter.getId().getTags().get(0).getKey(), "counter_tag_key_1");
        assertEquals(testCounter.getId().getTags().get(0).getValue(), "counter_tag_value_1");

        assertEquals(testCounter.getId().getTags().get(1).getKey(), "counter_tag_key_2");
        assertEquals(testCounter.getId().getTags().get(1).getValue(), "counter_tag_value_2");
    }

    @Test
    public void test_counter_without_tags() {
        Object bean = context.getBean("testCounterWithoutTags");
        assertTrue(bean instanceof Counter);
        Counter testCounter = (Counter) bean;

        assertNotNull(testCounter);
        assertNotNull(testCounter.getId());
        assertEquals(testCounter.getId().getName(), "counter_test_without_tags");
        assertNotNull(testCounter.getId().getTags());
        assertTrue(testCounter.getId().getTags().size() == 0);
    }

    @Test
    public void test_timer() {
        Object bean = context.getBean("testTimer");
        assertTrue(bean instanceof Timer);
        Timer testTimer = (Timer) bean;

        assertNotNull(testTimer);
        assertNotNull(testTimer.getId());
        assertEquals(testTimer.getId().getName(), "timer_test");
        assertNotNull(testTimer.getId().getTags());
        assertTrue(testTimer.getId().getTags().size() == 2);

        assertEquals(testTimer.getId().getTags().get(0).getKey(), "timer_tag_key_1");
        assertEquals(testTimer.getId().getTags().get(0).getValue(), "timer_tag_value_1");

        assertEquals(testTimer.getId().getTags().get(1).getKey(), "timer_tag_key_2");
        assertEquals(testTimer.getId().getTags().get(1).getValue(), "timer_tag_value_2");
    }

    @Test
    public void test_timer_without_tags() {
        Object bean = context.getBean("testTimerWithoutTags");
        assertTrue(bean instanceof Timer);
        Timer testTimer = (Timer) bean;

        assertNotNull(testTimer);
        assertNotNull(testTimer.getId());
        assertEquals(testTimer.getId().getName(), "timer_test_without_tags");
        assertNotNull(testTimer.getId().getTags());
        assertTrue(testTimer.getId().getTags().size() == 0);
    }
}