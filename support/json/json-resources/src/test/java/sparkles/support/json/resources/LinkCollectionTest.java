package sparkles.support.json.resources;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkCollectionTest {

  @Test
  public void itShouldCreate() {
    LinkCollection l = new LinkCollection();
    assertThat(l).isNotNull();
  }

  @Test
  public void add_shouldAddToBag() {
    LinkCollection l = new LinkCollection();
    final Link one = new Link().href("/foo").rel("foo");
    final Link two = new Link().href("/bar").rel("bar");
    l.add(one);
    l.add(two);

    assertThat(l.list()).hasSize(2);
    assertThat(l.list()).contains(one, two);
  }

  @Test
  public void clear_shouldClearTheBag() {
    LinkCollection l = new LinkCollection()
      .add(
        new Link().href("/foo").rel("foo"),
        new Link().href("/bar").rel("bar")
      );
    l.clear();

    assertThat(l.list()).hasSize(0);
  }

  @Test
  public void findByRel_shouldReturnList() {
    LinkCollection l = new LinkCollection()
      .add(
        new Link().href("/foo").rel("foo"),
        new Link().href("/bar").rel("bar")
      );

    assertThat(l.findByRel("foo")).hasSize(1);
  }

  @Test
  public void findFirstByRel_shouldReturnOptional() {
    LinkCollection l = new LinkCollection()
      .add(
        new Link().href("/foo").rel("foo"),
        new Link().href("/bar").rel("bar")
      );

    assertThat(l.findFirstByRel("foo").isPresent()).isTrue();
    assertThat(l.findFirstByRel("foo").get().href()).isEqualTo("/foo");
  }

  @Test
  public void find_shouldReturnList() {
    LinkCollection links = new LinkCollection()
      .add(
        new Link().href("/foo").rel("foo").title("1234"),
        new Link().href("/bar").rel("bar")
      );

    assertThat(links.find(l -> "1234".equals(l.title()))).hasSize(1);
  }

  @Test
  public void findFirst_shouldReturnOptional() {
    LinkCollection links = new LinkCollection()
      .add(
        new Link().href("/foo").rel("foo"),
        new Link().href("/bar").rel("bar").name("foob")
      );

    Optional<Link> foob = links.findFirst(l -> "foob".equals(l.name()));
    assertThat(foob.isPresent()).isTrue();
    assertThat(foob.get().name()).isEqualTo("foob");
  }

}
