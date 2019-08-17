package sparkles.support.common.collections;

import com.google.common.collect.Lists;

import java.util.List;

public final class Collections {

  public static <T> List<T> toList(Iterable<T> iterable) {
    return Lists.newArrayList(iterable);
  }

  public static <K, V> MapBuilder<K, V> newMap() {
    return new MapBuilder<K, V>();
  }
}
