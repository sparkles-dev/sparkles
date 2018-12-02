package sparkles.support.json.resources;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LinksTest {

  @Test
  public void itShouldCreate() {
    Links l = new Links();
    assertThat(l).isNotNull();
  }

  @Test
  public void add_shouldAddToBag() {
    Links l = new Links();
    final Link one = new Link().href("/foo").rel("foo");
    final Link two = new Link().href("/bar").rel("bar");
    l.add(one);
    l.add(two);

    assertThat(l.links()).hasSize(2);
    assertThat(l.links()).contains(one, two);
  }

  @Test
  public void clear_shouldClearTheBag() {
    Links l = new Links()
      .add(
        new Link().href("/foo").rel("foo"),
        new Link().href("/bar").rel("bar")
      );
    l.clear();

    assertThat(l.links()).hasSize(0);
  }

  @Test
  public void findByRel_shouldReturnList() {
    Links l = new Links()
      .add(
        new Link().href("/foo").rel("foo"),
        new Link().href("/bar").rel("bar")
      );

    assertThat(l.findByRel("foo")).hasSize(1);
  }

  @Test
  public void findFirstByRel_shouldReturnOptional() {
    Links l = new Links()
      .add(
        new Link().href("/foo").rel("foo"),
        new Link().href("/bar").rel("bar")
      );

    assertThat(l.findFirstByRel("foo").isPresent()).isTrue();
    assertThat(l.findFirstByRel("foo").get().href()).isEqualTo("/foo");
  }

  @Test
  public void find_shouldReturnList() {
    Links links = new Links()
      .add(
        new Link().href("/foo").rel("foo").title("1234"),
        new Link().href("/bar").rel("bar")
      );

    assertThat(links.find(l -> "1234".equals(l.title()))).hasSize(1);
  }

  @Test
  public void findFirst_shouldReturnOptional() {
    Links links = new Links()
      .add(
        new Link().href("/foo").rel("foo"),
        new Link().href("/bar").rel("bar").name("foob")
      );

    Optional<Link> foob = links.findFirst(l -> "foob".equals(l.name()));
    assertThat(foob.isPresent()).isTrue();
    assertThat(foob.get().name()).isEqualTo("foob");
  }

}
