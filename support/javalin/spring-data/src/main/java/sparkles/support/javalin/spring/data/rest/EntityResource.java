package sparkles.support.javalin.spring.data.rest;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.addAll;

/**
 * JSON resource representation for an entity.
 *
 * @param <Entity>
 */
public class EntityResource<Entity> {

  private final Links links = new Links();

  @JsonUnwrapped
  public Entity entity;

  @JsonProperty("_links")
  public Links getLinks() {
    return links;
  }

  public void withSelfRel(String href) {
    links.add(new Link().rel("self").href(href));
  }

  // TODO: better to use the custom (de-)serializer?
  @JsonProperty("_embedded")
  public EmbeddedResources getEmbeddedResources() {
    return new EmbeddedResources(this);
  }

  /** utilities class that handles the serialization / deserialization of @Embedded fields */
  public static class EmbeddedResources {

    private final EntityResource<?> resource;
    EmbeddedResources(EntityResource<?> resource) {
      this.resource = resource;
    }

    @JsonAnyGetter
    public Map<String, Object> toJson() {

      final Map<String, Object> props = findAllFieldsAnnotatedWith(resource.getClass(), Embedded.class)
        .stream()
        .filter(field -> field.getAnnotation(Embedded.class) != null)
        .map(field -> new AnnotatedField(field.getAnnotation(Embedded.class), field))
        .collect(Collectors.toMap(
          p -> p.annotation.value(),
          p -> getter(resource, p.field)
        ));

      return props;
    }

    private static class AnnotatedField {
      Embedded annotation;
      Field field;

      public AnnotatedField(Embedded annotation, Field field) {
        this.annotation = annotation;
        this.field = field;
      }
    }

    private static List<Field> findAllFields(Class<?> type) {
      List<Field> fields = new LinkedList<>();
      if (type != null) {
        addAll(fields, type.getDeclaredFields());

        if (type.getSuperclass() != null) {
          fields.addAll(findAllFields(type.getSuperclass()));
        }
      }

      return fields;
    }

    private static List<Field> findAllFieldsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotationType) {
      return findAllFields(type).stream()
        .filter(field -> Arrays.stream(field.getAnnotations())
          .anyMatch(annotation -> annotation.annotationType().equals(annotationType)))
        .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <T> T getter(Object target, Field field) {
      boolean forceAccess = false;

      try {
        if (!field.isAccessible()) {
          field.setAccessible(true);
          forceAccess = true;
        }

        return (T) field.get(target);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      } finally {
        if (forceAccess) {
          field.setAccessible(false);
        }
      }
    }
  }
}
