package sparkles.support.json.resources.internal;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import sparkles.support.json.resources.Embedded;
import sparkles.support.json.resources.Links;

/**
 * Modelling reserved HAL properties namely <code>_links</code> and <code>_embedded</code>.
 */
public enum ReservedProperty {

  LINKS("_links", Links.class), EMBEDDED("_embedded", Embedded.class);

  private final Class<? extends Annotation> annotation;
  private final String propertyName;
  private final UUID prefix = UUID.randomUUID();

  ReservedProperty(String name, Class<? extends Annotation> annotation) {
    this.propertyName = name;
    this.annotation = annotation;
  }

  public String getPropertyName() {
    return propertyName;
  }

  /**
   * Assign an alternate name to POJO property that is unique outside of the the reserved
   * object - if it is a link or embedded property.
   *
   * @param bpd The property to process.
   * @param map Curie map applying to properties of <code>_links</code> section.
   * @return An alternate name if relevant otherwise the original name is maintained.
   */
  public String alternateName(BeanPropertyDefinition bpd, CurieMap map) {
    String originalName = bpd.getName();

    AnnotatedMember annotatedMember = firstNonNull(bpd.getField(), bpd.getSetter(), bpd.getGetter());
    if (annotatedMember == null) {
      return originalName;
    }

    Annotation o = annotatedMember.getAnnotation(annotation);
    if (o == null) {
      return originalName;
    }

    String name = null;
    if (annotation == Embedded.class) {
      try {
        Method valueMethod = annotation.getDeclaredMethod("value");
        String alternateName = (String) valueMethod.invoke(o);
        name = alternateName.isEmpty() ? originalName : alternateName;
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }

    if (annotation == Links.class) {
      return alternateName(originalName);
      /*
      TODO: curies
      String curiePrefix = ((Link) o).curie();
      if (!curiePrefix.isEmpty()) {
        Optional<URI> resolved = map.resolve(curiePrefix + ":" + name);
        return alternateName(resolved.map(URI::toString).orElse(name));
      }
      */
    }

    return alternateName(name);
  }

  /**
   * Given a property that should belong to this reserved object instance a name is assigned which will
   * be unique outside of the reserved property.
   *
   * @param originalName Property name within this reserved object that should be unique outside.
   * @return Alternate name for the given original name.
   */
  public String alternateName(String originalName) {
    return prefix.toString() + ":" + originalName;
  }

  @SafeVarargs
  static <T> T firstNonNull(T... vals) {
    for (T v : vals) {
      if (v != null)
        return v;
    }

    return null;
  }
}
