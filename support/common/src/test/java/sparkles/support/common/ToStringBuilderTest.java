package sparkles.support.common;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringBuilderTest {

  private static class Foo {
    public String id;
    public int order;

    @Override
    public String toString() {
      return new ToStringBuilder(Foo.class)
        .append("id", id)
        .append("order", order)
        .toString();
    }
  }

  @Test
  public void itShouldPrintClassName() {
    Foo foo = new Foo();
    assertThat(foo.toString()).isEqualTo("Foo[order=0]");
  }

  @Test
  public void itShouldPrintEmptyObjectWithDoubleSquareBracket() {
    Foo foo = new Foo();
    assertThat(foo.toString()).isEqualTo("Foo[order=0]");
  }

  @Test
  public void itShouldPrintNonNullProperty() {
    Foo foo = new Foo();
    foo.id = "a";
    assertThat(foo.toString()).isEqualTo("Foo[id=a,order=0]");
  }

  @Test
  public void itShouldPrintPrimitiveProperty() {
    Foo foo = new Foo();
    foo.order = 1;
    assertThat(foo.toString()).isEqualTo("Foo[order=1]");
  }

}
