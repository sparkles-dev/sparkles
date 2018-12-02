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
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Jackson module for serializing and deserializing {@link Resource} annotated classes.
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
        return new Deserializer((BeanDeserializer) deserializer);
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
          /* TODO: find _embedded property
          for (ReservedProperty rp : ReservedProperty.values()) {
            String alternateName = rp.alternateName(property, map);
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
}
