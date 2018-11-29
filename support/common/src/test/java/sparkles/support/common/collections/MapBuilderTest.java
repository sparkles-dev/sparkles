package sparkles.support.common.collections;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MapBuilderTest {

  @Test
  public void fluentApi() {
    Map<String, Long> map = new MapBuilder<String, Long>()
      .put("foo", 2L)
      .put("bar", 3L)
      .build();

    assertThat(map).hasSize(2);
    assertThat(map.get("foo")).isEqualTo(2L);
    assertThat(map.get("bar")).isEqualTo(3L);
  }

  @Test
  public void buildAs() {
    Map<String, Long> map = new MapBuilder<String, Long>()
      .buildAs(ConcurrentHashMap.class);

    assertThat(map instanceof ConcurrentHashMap).isTrue();
  }

}
