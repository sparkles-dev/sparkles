package sparkles.support.common.collections;

public final class CollectionUtil {

  public static <K, V> MapBuilder<K, V> newMap() {
    return new MapBuilder();
  }
}
