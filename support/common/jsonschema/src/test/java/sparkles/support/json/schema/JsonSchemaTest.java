package sparkles.support.json.schema;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonSchemaTest {

  @Test
  public void testSomething() throws Exception {
    String value = new JsonSchema().doSomething();

    assertThat(value).isNotEmpty();
    System.out.println(value);
  }
}
