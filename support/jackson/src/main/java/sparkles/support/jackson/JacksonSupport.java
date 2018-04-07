package sparkles.support.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JacksonSupport {

  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.registerModule(new JavaTimeModule());
  }

  public static ObjectMapper defaultObjectMapper() {
    return mapper;
  }

}
