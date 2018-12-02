package sparkles.support.json.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Links {

  private final List<Link> links = new ArrayList<>();

  public Links add(Link... link) {
    Collections.addAll(links, link);

    return this;
  }

  public Links clear() {
    links.clear();

    return this;
  }

  public List<Link> links() {
    return links;
  }

  public List<Link> findByRel(String rel) {
    return find(l -> rel.equals(l.rel()));
  }

  public Optional<Link> findFirstByRel(String rel) {
    return findFirst(l -> rel.equals(l.rel()));
  }

  public List<Link> find(Predicate<Link> predicate) {
    return links.stream().filter(predicate).collect(Collectors.toList());
  }

  public Optional<Link> findFirst(Predicate<Link> predicate) {
    return links.stream().filter(predicate).findFirst();
  }

}
