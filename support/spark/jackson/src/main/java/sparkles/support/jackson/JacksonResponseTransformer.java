package sparkles.support.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

public class JacksonResponseTransformer<T> implements ResponseTransformer  {

  private final ObjectMapper om;

  private JacksonResponseTransformer(ObjectMapper om) {
    this.om = om;
  }

  @Override
  public String render(Object model) {
    try {
      return om.writeValueAsString(model);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Json serialization failed", e);
    }
  }

  public static <T> JacksonResponseTransformer<T> jacksonTransformer() {
    return jacksonTransformer(JacksonSupport.defaultObjectMapper());
  }

  public static <T> JacksonResponseTransformer<T> jacksonTransformer(ObjectMapper om) {
    return new JacksonResponseTransformer<T>(om);
  }

}
