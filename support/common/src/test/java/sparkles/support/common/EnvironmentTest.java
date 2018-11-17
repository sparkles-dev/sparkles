package sparkles.support.common;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentTest {

  @Test
  public void value_shouldReturnValueFromSystemProperty() {
    System.setProperty("foo", "bar");
    assertThat(Environment.value("foo", null)).isEqualTo("bar");
    System.clearProperty("foo");
  }

  @Test
  public void value_shouldReturnDefaultValue() {
    assertThat(Environment.value("foo", "def")).isEqualTo("def");
  }

  @Test
  public void environment_shouldReturnFromEnvironmentSetting() {
    System.setProperty("ENVIRONMENT", "develop");
    assertThat(Environment.environment()).isEqualTo(Environment.DEVELOP);
    System.clearProperty("ENVIRONMENT");

    System.setProperty("ENVIRONMENT", "production");
    assertThat(Environment.environment()).isEqualTo(Environment.PRODUCTION);
    System.clearProperty("ENVIRONMENT");
  }

  @Test
  public void environment_shouldReturnProductionAsDefault() {
    System.clearProperty("ENVIRONMENT");
    assertThat(Environment.environment()).isEqualTo(Environment.PRODUCTION);
  }

  @Test
  public void isDevelop_shouldReturnBoolean() {
    System.setProperty("ENVIRONMENT", "develop");
    assertThat(Environment.isDevelop()).isTrue();
    System.clearProperty("ENVIRONMENT");
    assertThat(Environment.isDevelop()).isFalse();
  }

  @Test
  public void isProduction_shouldReturnBoolean() {
    System.setProperty("ENVIRONMENT", "production");
    assertThat(Environment.isProduction()).isTrue();
    System.clearProperty("ENVIRONMENT");
    assertThat(Environment.isProduction()).isTrue();
  }

}
