/*
 * JacksonResourcesModule is a modified version of jackson-dataformat-hal
 *
 * Original sources obtained from https://github.com/openapi-tools/jackson-dataformat-hal/blob/master/LICENSE
 * and released under the following license:
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Open API Tools
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package sparkles.support.json.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Jackson module for serializing and deserializing {@link Resource} annotated classes.
 *
 * This is a modified version of openapi-tools/jackson-dataformat-hal
 *
 * @link https://github.com/openapi-tools/jackson-dataformat-hal
 */
public class JacksonResourcesModule extends SimpleModule {

  public JacksonResourcesModule() {
    super("JacksonResourcesModule", Version.unknownVersion());
  }

  @Override
  public void setupModule(SetupContext context) {
    context.addBeanSerializerModifier(new SerializerModifier());
    context.addBeanDeserializerModifier(new DeserializerModifier());
  }

  public static class SerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {

      Resource ann = beanDesc.getClassAnnotations().get(Resource.class);
      if (ann != null) {
        return new ResourceSerializer((BeanSerializer) serializer, beanDesc);
      } else {
        return serializer;
      }
    }
  }

  /** Serializer for @Resource annotated classes */
  public static class ResourceSerializer extends BeanSerializerBase {

    private final BeanDescription beanDescription;
    public ResourceSerializer(BeanSerializer serializer, BeanDescription beanDesc) {
      super(serializer);
      beanDescription = beanDesc;
    }

    @Override
    public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
      return this;
    }

    @Override
    protected BeanSerializerBase withIgnorals(Set<String> toIgnore) {
      return this;
    }

    @Override
    protected BeanSerializerBase asArraySerializer() {
      return this;
    }

    @Override
    public BeanSerializerBase withFilterId(Object filterId) {
      return this;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

      final Map<String, BeanPropertyWriter> embeddedProps = new TreeMap<>();
      final Map<String, BeanPropertyWriter> linksProps = new TreeMap<>();
      final List<BeanPropertyWriter> otherProps = new ArrayList<>();

      // Categorize properties
      for (BeanPropertyWriter prop : _props) {
        try {
          if (prop.getAnnotation(Embedded.class) != null) {
            Object object = prop.get(value);
            if (object != null) {
              Embedded embedded = prop.getAnnotation(Embedded.class);
              String rel = "".equals(embedded.value()) ? prop.getName() : embedded.value();

              embeddedProps.put(rel, prop);
            }
          } else if (prop.getAnnotation(Linked.class) != null) {
            Object object = prop.get(value);

            if (object != null) {
              if (object instanceof Collection) {
                for (Link link : ((List<Link>) object)) {
                  String rel = "".equals(link.rel()) ? prop.getName() : link.rel();
                  linksProps.put(rel, prop);
                }
              } else if (object instanceof Link) {
                Link link = (Link) object;
                String rel = "".equals(link.rel()) ? prop.getName() : link.rel();
                linksProps.put(rel, prop);
              } else if (object instanceof Links) {
                Links links = (Links) object;
                for (Link link : links.links()) {
                  String rel = "".equals(link.rel()) ? prop.getName() : link.rel();
                  linksProps.put(rel, prop);
                }
              }
            }
          } else {
            otherProps.add(prop);
          }
        } catch (Exception e) {
          wrapAndThrow(serializers, e, value, prop.getName());
        }
      }

      // Write JSON representation
      gen.writeStartObject();

      if (!linksProps.isEmpty()) {
        gen.writeFieldName("_links");
        gen.writeStartObject();
        for (String rel : linksProps.keySet()) {
          try {
            gen.writeFieldName(rel);
            Object v = linksProps.get(rel).get(value);
            if (v instanceof Links || v instanceof Collection) {
              List<Link> links = v instanceof Links ? ((Links) v).links() : ((List<Link>) v);
              if (links.size() > 1) {
                gen.writeStartArray();
              }
              for (Link link : links) {
                gen.writeObject(link);
              }
              if (links.size() > 1) {
                gen.writeEndArray();
              }
            } else {
              gen.writeObject(v);
            }
          } catch (Exception e) {
            wrapAndThrow(serializers, e, value, rel);
          }
        }
        gen.writeEndObject();
      }

      // Write other properties
      for (BeanPropertyWriter prop : otherProps) {
        try {
          prop.serializeAsField(value, gen, serializers);
        } catch (Exception e) {
          wrapAndThrow(serializers, e, gen, prop.getName());
        }
      }

      // Write the _embedded properties
      if (!embeddedProps.isEmpty()) {
        gen.writeFieldName("_embedded");
        gen.writeStartObject();
        for (String rel : embeddedProps.keySet()) {
          try {
            gen.writeFieldName(rel);
            gen.writeObject(embeddedProps.get(rel).get(value));
          } catch (Exception e) {
            wrapAndThrow(serializers, e, value, rel);
          }
        }
        gen.writeEndObject();
      }

      gen.writeEndObject();
    }

  }

  public static class DeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
      Resource ann = beanDesc.getClassAnnotations().get(Resource.class);
      if (ann != null) {
        return new Deserializer(deserializer);
      } else if (Links.class.isAssignableFrom(beanDesc.getBeanClass())) {
        return new LinkDeserializer(deserializer);
      } else {
        return deserializer;
      }
    }

    @Override
    public List<BeanPropertyDefinition> updateProperties(DeserializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> propDefs) {
      Resource ann = beanDesc.getClassAnnotations().get(Resource.class);
      if (ann != null) {
        // CurieMap map = createCurieMap(beanDesc);
        List<BeanPropertyDefinition> modified = new ArrayList<>();
        Iterator<BeanPropertyDefinition> properties = propDefs.iterator();
        while (properties.hasNext()) {
          BeanPropertyDefinition property = properties.next();

          AnnotatedMember member = annotatedMember(property);

          if (member.getAnnotation(Linked.class) != null) {
            String propName = "_links:" + member.getName();
            modified.add(property.withName(new PropertyName(propName)));
            properties.remove();

            System.out.println("_links property");
            System.out.println(property);
            System.out.println(propName);
          } else if (member.getAnnotation(Embedded.class) != null) {
            // Add bean property for the _embedded rel
            String rel = member.getAnnotation(Embedded.class).value();
            if ("".equals(rel)) {
              rel = member.getName();
            }

            modified.add(property.withName(new PropertyName("_embedded:" + rel)));
            properties.remove();

            System.out.println("_embedded property set to: " + "_embedded:" + rel);
          }

          /*
          for (ReservedProperty rp : ReservedProperty.values()) {
            String alternateName = rp.alternateName(property/*, map);
            if (!property.getName().equals(alternateName)) {
              modified.add(property.withName(new PropertyName(alternateName)));
              properties.remove();
            }
          }
          */

        }
        propDefs.addAll(modified);
      }

      return propDefs;
    }

  }

  public static class LinkDeserializer extends DelegatingDeserializer {

    public LinkDeserializer(JsonDeserializer<?> d) {
      super(d);
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
      return new LinkDeserializer(newDelegatee);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      System.out.println("deserialize Links class");

      return null;
    }
  }

  public static class Deserializer extends DelegatingDeserializer {

    public Deserializer(JsonDeserializer<?> delegate) {
      super(delegate);
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
      return new Deserializer(newDelegatee);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      TreeNode tn = p.getCodec().readTree(p);
      if (tn.isObject()) {
        ObjectNode root = (ObjectNode) tn;

        // Deserialize _links properties
        ObjectNode linksNode = (ObjectNode) tn.get("_links");
        if (linksNode != null) {
          Iterator<Map.Entry<String, JsonNode>> jsonProps = linksNode.fields();
          while(jsonProps.hasNext()) {
            Map.Entry<String, JsonNode> jsonProp = jsonProps.next();
            String rel = jsonProp.getKey();

            JsonNode value = jsonProp.getValue();
            if (value.isArray()) {
              System.out.println("links array " + value.toString());
              //Iterator<Link> links = p.readValuesAs(Link.class);

              /*
              ArrayNode arrayNode = (ArrayNode) value;
              arrayNode.forEach(node -> {
                p.readValuesAs()
              });
              */
            } else {

              System.out.println("link single " + value.toString());
              //Link link = p.readValueAs(Link.class);
              //System.out.println(link);
            }

            jsonProp.getValue();
          }

          System.out.println("_links ...");
          System.out.println(linksNode);
          root.remove("_links");
        }

        // Deserialize _embedded properties
        ObjectNode embeddedNode = (ObjectNode) tn.get("_embedded");
        if (embeddedNode != null) {
          Iterator<Map.Entry<String, JsonNode>> jsonProps = embeddedNode.fields();
          while(jsonProps.hasNext()) {
            Map.Entry<String, JsonNode> jsonProp = jsonProps.next();
            String rel = jsonProp.getKey();
            root.set("_embedded:" + rel, jsonProp.getValue());

            System.out.println("_embedded:" + rel);
            System.out.println(jsonProp.getValue());
          }
          root.remove("_embedded");
        }

        /*
        for (ReservedProperty rp : ReservedProperty.values()) {
          ObjectNode on = (ObjectNode) tn.get(rp.getPropertyName());
          if (on != null) {
            //CurieMap curieMap = createCurieMap(rp, on);
            //on.remove("curies");

            Iterator<Map.Entry<String, JsonNode>> it = on.fields();
            while (it.hasNext()) {
              Map.Entry<String, JsonNode> jn = it.next();
              //String propertyName = curieMap.resolve(jn.getKey()).map(URI::toString).orElse(jn.getKey());
              String propertyName = jn.getKey();
              root.set(rp.alternateName(propertyName), jn.getValue());
            }

            root.remove(rp.getPropertyName());
          }
        }
        */
      }

      final JsonParser modifiedParser = tn.traverse(p.getCodec());
      modifiedParser.nextToken();
      return _delegatee.deserialize(modifiedParser, ctxt);
    }
  }

  public enum ReservedProperty {

    LINKS("_links", Linked.class), EMBEDDED("_embedded", Embedded.class);

    private final String name;
    private final UUID prefix = UUID.randomUUID();
    private final Class<? extends Annotation> annotation;
    private final Method valueMethod;

    ReservedProperty(String name, Class<? extends Annotation> annotation) {
      this.name = name;
      this.annotation = annotation;

      if (annotation == Embedded.class) {
        try {
          valueMethod = annotation.getDeclaredMethod("value");
        } catch (NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
      } else {
        valueMethod = null;
      }
    }

    public String getPropertyName() {
      return name;
    }

    /**
     * Assign an alternate name to POJO property that is unique outside of the the reserved
     * object - if it is a link or embedded property.
     *
     * @param bpd The property to process.
     * @param map Curie map applying to properties of <code>_links</code> section.
     * @return An alternate name if relevant otherwise the original name is maintained.
     */
    public String alternateName(BeanPropertyDefinition bpd/*, CurieMap map*/) {
      String originalName = bpd.getName();

      AnnotatedMember annotatedMember = firstNonNull(bpd.getField(), bpd.getSetter(), bpd.getGetter());
      if (annotatedMember == null) {
        return originalName;
      }

      Annotation o = annotatedMember.getAnnotation(annotation);
      if (o == null) {
        return originalName;
      }

      if (valueMethod != null) {
        String name;
        try {
          String alternateName = (String) valueMethod.invoke(o);
          name = alternateName.isEmpty() ? originalName : alternateName;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
      }

      if (o.annotationType() == Linked.class) {
        /*
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

  public static AnnotatedMember annotatedMember(BeanPropertyDefinition beanPropertyDefinition) {
    return firstNonNull(beanPropertyDefinition.getField(), beanPropertyDefinition.getGetter(), beanPropertyDefinition.getSetter());
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
