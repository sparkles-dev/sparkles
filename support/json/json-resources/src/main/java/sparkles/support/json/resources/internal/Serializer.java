package sparkles.support.json.resources.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sparkles.support.json.resources.Curie;
import sparkles.support.json.resources.Curies;
import sparkles.support.json.resources.Embedded;
import sparkles.support.json.resources.Link;
import sparkles.support.json.resources.LinkCollection;
import sparkles.support.json.resources.Links;

/**
 * Serializer to handle {@link Resource} beans ensuring they are serialized according to the HAL
 * specification. This implies placing links inside the <code>_links</code> property and embedded objects inside the <code>_embedded</code>
 * property.
 */
public class Serializer extends BeanSerializerBase {

  private static final Logger LOG = LoggerFactory.getLogger(Serializer.class);
  private final BeanDescription beanDescription;

  public Serializer(BeanSerializerBase src, BeanDescription beanDescription) {
    super(src);
    this.beanDescription = beanDescription;
  }

  @Override
  public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
    return this;
  }

  @Override
  public BeanSerializerBase withFilterId(Object o) {
    return this;
  }

  @Override
  protected BeanSerializerBase asArraySerializer() {
    return this;
  }

  @Override
  protected BeanSerializerBase withIgnorals(Set<String> set) {
    return this;
  }

  @Override
  public void serialize(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    FilteredProperties filtered = new FilteredProperties(bean, provider, beanDescription);
    filtered.serialize(bean, jgen, provider);
  }

  /**
   * Modelling the properties of the bean segmented into HAL categories: links, embedded resources, and state
   */
  private class FilteredProperties {

    private List<BeanPropertyWriter> state = new ArrayList<>();
    private Map<String, LinkProperty> links = new TreeMap<>();
    private Map<String, BeanPropertyWriter> embedded = new TreeMap<>();

    // All of the possible curies that COULD be used (provided via Curie/Curies annotations)
    private Map<String, String> curieMap = new TreeMap<>();
    // All of the curies that actually ARE being used (provided via Link annotations)
    private Set<String> curiesInUse = new TreeSet<>();

    public FilteredProperties(Object bean, SerializerProvider provider,
                              BeanDescription beanDescription) throws IOException {

      populateCurieMap(beanDescription);

      for (BeanPropertyWriter prop : _props) {
        try {
          if (prop.getAnnotation(Embedded.class) != null) {
            Object object = prop.get(bean);
            if (object != null) {
              Embedded er = prop.getAnnotation(Embedded.class);
              String val = "".equals(er.value()) ? prop.getName() : er.value();
              addEmbeddedProperty(val, prop);
            }

          } else if (prop.getAnnotation(Links.class) != null) {
            Links l = prop.getAnnotation(Links.class);
                        /*
                        TODO
                        String relation = "".equals(l.value()) ? prop.getName() : l.value();
                        String curie = "".equals(l.curie()) ? null : l.curie();
                        if (!"".equals(l.curie())) {
                            curiesInUse.add(l.curie());
                        }
                        Object value = prop.get(bean);
                        if (value instanceof Collection) {
                            addLinks(relation, (Collection<HALLink>) prop.get(bean), curie);
                        } else if (value instanceof HALLink) {
                            addLink(relation, (HALLink) prop.get(bean), curie);
                        }
                         */
          } else {
            state.add(prop);
          }
        } catch (Exception e) {
          wrapAndThrow(provider, e, bean, prop.getName());
        }
      }

      if (!curiesInUse.isEmpty()) {
        addCurieLinks();
      }
    }

    private void addCurieLinks() {
          /*
          TODO
            Collection<HALLink> curieLinks = new ArrayList<>();
            for (String curie: curiesInUse) {
                if (curieMap.containsKey(curie)) {
                    curieLinks.add(new HALLink.Builder(curieMap.get(curie))
                            .name(curie)
                            .build());
                } else {
                    LOG.warn("No Curie/Curies annotation provided for [{}]", curie);
                }
            }
            addLinks("curies", curieLinks, null);
           */
    }

    private void populateCurieMap(BeanDescription beanDescription) {

      // Curies should only be shown if they are being used by some other link.
      // Populate CurieMap now so that it can be referred to later during link
      // serialisation.  Note - either a single Curie annotation can be used by
      // itself or a collection can be wrapped using Curies.

      List<Curie> curieAnnotations = new ArrayList<>();
      if (null != beanDescription.getClassAnnotations().get(Curie.class)) {
        curieAnnotations.add(beanDescription.getClassAnnotations().get(Curie.class));
      }
      if (null != beanDescription.getClassAnnotations().get(Curies.class)) {
        curieAnnotations.addAll(Arrays.asList(beanDescription.getClassAnnotations().get(Curies.class).value()));
      }

      for (Curie curie : curieAnnotations) {
        if (curieMap.containsKey(curie.prefix())) {
          LOG.warn("Curie annotation already exists [{}]", curie.prefix());
        }
        curieMap.put(curie.prefix(), curie.href());
      }
    }

    public void serialize(Object bean, JsonGenerator jgen, SerializerProvider provider) throws IOException {
      jgen.writeStartObject();

      if (!links.isEmpty()) {
        jgen.writeFieldName("_links");
        jgen.writeStartObject();
        for (String rel : links.keySet()) {
          jgen.writeFieldName(rel);
          links.get(rel).serialize(jgen);
        }
        jgen.writeEndObject();
      }

      if (!embedded.isEmpty()) {
        jgen.writeFieldName("_embedded");
        jgen.writeStartObject();
        for (String rel : embedded.keySet()) {
          try {
            jgen.writeFieldName(rel);
            jgen.writeObject(embedded.get(rel).get(bean));
          } catch (Exception e) {
            wrapAndThrow(provider, e, bean, rel);
          }
        }
        jgen.writeEndObject();
      }

      for (BeanPropertyWriter prop : state) {
        try {
          prop.serializeAsField(bean, jgen, provider);
        } catch (Exception e) {
          wrapAndThrow(provider, e, bean, prop.getName());
        }
      }

      jgen.writeEndObject();
    }

    private void addEmbeddedProperty(String rel, BeanPropertyWriter property) {
      if (embedded.put(rel, property) != null) {
        LOG.warn("Embedded resource already existed with rel [{}] in class [{}]", rel, _handledType);
      }
    }

    private void addLink(String rel, Link link, String curie) {
      if (links.put(applyCurieToRel(rel, curie), new LinkProperty(link)) != null) {
        LOG.warn("Link resource already existed with rel [{}] in class [{}]", rel, _handledType);
      }
    }

    private void addLinks(String rel, LinkCollection links, String curie) {
      if (this.links.put(applyCurieToRel(rel, curie), new LinkProperty(links)) != null) {
        LOG.warn("Link resource already existed with rel [{}] in class [{}]", rel, _handledType);
      }
    }

    private String applyCurieToRel(String rel, String curie) {
      return (null == curie) ? rel : curie + ":" + rel;
    }

  }

  /**
   * Representing either a single link (one-to-one relation) or a collection of links.
   */
  private static class LinkProperty {

    private Link link;
    private LinkCollection links;

    public LinkProperty(Link link) {
      this.link = link;
    }

    public LinkProperty(LinkCollection links) {
      this.links = links == null ? new LinkCollection() : links;
    }

    public void serialize(JsonGenerator jgen) throws IOException {
      if (link != null) {
        writeLinkObject(jgen, link);
      } else if (links != null) {
        jgen.writeStartArray();
        for (Link curLink : links.links()) {
          writeLinkObject(jgen, curLink);
        }
        jgen.writeEndArray();
      }
    }

    private void writeLinkObject(JsonGenerator jgen, Link link) throws IOException {
      jgen.writeObject(link);
    }

  }

}
