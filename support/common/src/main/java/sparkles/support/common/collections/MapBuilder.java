package sparkles.support.common.collections;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class MapBuilder<K, V> {
  private Map<K, V> map = new HashMap<K, V>();

  public MapBuilder<K, V> put(K key, V value) {
    map.put(key, value);

    return this;
  }

  public Map<K, V> build() {
    return Collections.unmodifiableMap(map);
  }

  public <T extends Map<K, V>> T buildAs(Class<T> mapClz) {
    try {
      T map = mapClz.newInstance();
      map.putAll(this.map);

      return map;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
