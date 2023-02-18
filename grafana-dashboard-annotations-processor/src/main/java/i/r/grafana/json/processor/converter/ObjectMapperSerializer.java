package i.r.grafana.json.processor.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectMapperSerializer {

    private static final Logger log = LoggerFactory.getLogger(ObjectMapperSerializer.class);
    private static final ObjectMapper objectMapper = get();

    public static final String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Unable to serialize object = {}", object);
            return null;
        }
    }

    public static ObjectMapper get() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }


}
