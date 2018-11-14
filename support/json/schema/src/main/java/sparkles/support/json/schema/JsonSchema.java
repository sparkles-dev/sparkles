package sparkles.support.json.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

public class JsonSchema {

  public String foo;

  public String doSomething() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(objectMapper);

    return objectMapper.writeValueAsString(jsonSchemaGenerator.generateJsonSchema(JsonSchema.class));
  }
}
