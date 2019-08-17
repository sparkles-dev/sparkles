package sparkles.support.json;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaxJsonTest {

  @Test
  public void readJsonObject() {
    assertThat(JavaxJson.readJsonObject("{\"name\":\"John Doe\"}")).isNotNull();
  }

}
