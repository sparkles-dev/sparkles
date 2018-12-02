package sparkles.support.json.resources;

import java.util.ArrayList;
import java.util.List;

public class Links {

  private final List<Link> links = new ArrayList<>();

  public void add(Link link) {
    links.add(link);
  }

  public List<Link> links() {
    return links;
  }

  /*
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

    json.forEach((rel, value) -> {
      if (value instanceof Collection) {
        // Link collection
        ((Collection) value).forEach(l -> {

        });
      } else {
        // Single link
      }
    });

  }
  */

}
