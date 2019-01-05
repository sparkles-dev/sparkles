package sparkles.support.common;

/**
 * Utility class to implement `toString()` methods.
 */
public final class ToStringBuilder {

  private final StringBuilder sb;

  public ToStringBuilder(Class<?> clz) {
    sb = new StringBuilder(clz.getSimpleName()).append("[");
  }

  public ToStringBuilder append(String name, Object value) {
    if (value != null) {
      propertySeparator();
      sb.append(name).append("=").append(value);
    }

    return this;
  }

  public ToStringBuilder append(String name, String value) {
    if (value != null && value.length() > 0) {
      propertySeparator();
      sb.append(name).append("=").append(value);
    }

    return this;
  }

  private void propertySeparator() {
    if (sb.lastIndexOf("[") < sb.length() - 1) {
      sb.append(",");
    }
  }

  @Override
  public String toString() {
    sb.append("]");

    return sb.toString();
  }
}
