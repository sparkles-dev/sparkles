package sparkles.support.javalin.spring.data.rest;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Links {

  private final List<Link> links = new ArrayList<>();

  public void add(Link link) {
    links.add(link);
  }

  @JsonAnyGetter
  public Map<String, Object> toJson() {
    final Map<String, Object> json = new HashMap<>();

    links.forEach(link -> {
      final String rel = link.rel();
      if (json.containsKey(rel)) {
        // Link collection
        Object multi = json.get(rel);
        if (multi instanceof Collection) {
          List<Link> existing = (List) multi;
          existing.add(link);
        } else {
          Link existing = (Link) multi;
          json.put(rel, Lists.newArrayList(existing, link));
        }

      } else {
        // Single link
        json.put(link.rel(), link);
      }
    });

    return json;
  }

  @JsonAnySetter
  public void fromJson(Map<String, Object> json) {

  }

}
