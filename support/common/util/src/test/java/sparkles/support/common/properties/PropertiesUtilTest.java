package sparkles.support.common.properties;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertiesUtilTest {

  @Test
  public void readPropertiesResource_shouldLoadFromClassPath() throws Exception {
    final PropertiesUtil props = PropertiesUtil.readPropertiesResource("foo.properties");

    assertThat(props).isNotNull();
  }

  @Test
  public void getProperty_shouldReturnValue() throws Exception {
    final PropertiesUtil props = PropertiesUtil.readPropertiesResource("foo.properties");

    assertThat(props).isNotNull();
    assertThat(props.getProperty("foo")).isEqualTo("bar");
  }

  @Test
  public void getProperty_shouldReturnDefaultValue() throws Exception {
    final PropertiesUtil props = PropertiesUtil.readPropertiesResource("foo.properties");

    assertThat(props).isNotNull();
    assertThat(props.getProperty("foobar", "default")).isEqualTo("default");
  }

  @Test
  public void merge_shouldOverrideExisting() throws Exception {
    final PropertiesUtil props = PropertiesUtil.readPropertiesResource("foo.properties")
      .merge(PropertiesUtil.readPropertiesResource("bar.properties"));

    assertThat(props.getProperty("foo")).isEqualTo("123");
    assertThat(props.getProperty("bar")).isEqualTo("789");
  }

}
