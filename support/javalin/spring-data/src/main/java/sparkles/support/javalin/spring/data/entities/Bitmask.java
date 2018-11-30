package sparkles.support.javalin.spring.data.entities;

@Embeddable
public abstract class Bitmask {

  @Column("BITMASK")
  private Long bitmask;

  @JsonCreator
  public Bitmask(List<String> jsonStrings) {

  }

  public getBitmask() {
    return bitmask;
  }

  public void setBitmask(long value) {
    bitmask = value;
  }

  public void clearMask() {
    bitmask = 0;
  }

  public void addToMask(long value) {
    bitmask = bitmask | value;
  }

  @JsonValue
  public List<String> getJson() {

  }

  public abstract Map<String, Long> mappedValues();

  private static Foo extends Bitmask {

    public Map<String, Long> mappedValues() {
      return
    }
  }

  public static class MapBuilder<K, V> {
    private Map<K, V> map = new HashMap<K, V>();

    public MapBuilder<K, V> put(K key, V value) {
      map.put(key, value);

      return this;
    }

    public Map<K, V> build() {
      return map;
    }

    public <T extends Map<K, V>> T buildAs(Class<T> mapClz) {
      T map = mapClz.newInstance();
      map.putAll(this.map);

      return map;
    }
  }
}
