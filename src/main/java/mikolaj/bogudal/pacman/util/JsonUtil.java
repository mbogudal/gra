package mikolaj.bogudal.pacman.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import com.fasterxml.jackson.core.type.TypeReference;

public class JsonUtil {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    @SneakyThrows
    public static <T> T deserialize(String input, Class<T> dtoClass){
        return objectMapper.readValue(input, dtoClass);
    }

    @SneakyThrows
    public static <T> T deserialize(String input, TypeReference typeReference){
        return (T) objectMapper.readValue(input, typeReference);
    }
}
