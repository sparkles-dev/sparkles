package sparkles.support.jpa;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FooTest {

  @Test
  public void itShouldRun() {
    final Foo foo = new Foo();
    assertThat(foo).isNotNull();
  }
}
