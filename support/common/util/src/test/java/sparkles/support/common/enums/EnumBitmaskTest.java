package sparkles.support.common.enums;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumBitmaskTest {

  public enum Foo {
    FOO,
    BAR,
    FOOBAR
  }

  public static class FooBitmask extends EnumBitmask<Foo> {

    public FooBitmask() {
      super(Foo.class, (Foo foo) -> (long) foo.ordinal());
    }
  }

  @Test
  public void toBitmask() {
    final FooBitmask bitmask = new FooBitmask();

    long value0 = bitmask.toBitmask(Collections.emptyList());
    assertThat(value0).isEqualTo(0);

    long value1 = bitmask.toBitmask(Collections.singletonList(Foo.FOO));
    assertThat(value1).isEqualTo(1);

    long value2 = bitmask.toBitmask(Arrays.asList(Foo.FOO, Foo.FOOBAR ));
    assertThat(value2).isEqualTo(5);

    long value3 = bitmask.toBitmask(Arrays.asList(Foo.BAR, Foo.FOO, Foo.FOOBAR ));
    assertThat(value3).isEqualTo(7);
  }

  @Test
  public void fromBitmask() {
    final FooBitmask bitmask = new FooBitmask();

    final List<Foo> list0 = bitmask.fromBitmask(0);
    assertThat(list0).isEmpty();

    final List<Foo> list1 = bitmask.fromBitmask(-1);
    assertThat(list1).isEmpty();

    final List<Foo> list3 = bitmask.fromBitmask(1);
    assertThat(list3).hasSize(1);
    assertThat(list3).contains(Foo.FOO);

    final List<Foo> list4 = bitmask.fromBitmask(5);
    assertThat(list4).containsExactly(Foo.FOO, Foo.FOOBAR);

    final List<Foo> list5 = bitmask.fromBitmask(7);
    assertThat(list5).containsExactly(Foo.FOO, Foo.BAR, Foo.FOOBAR);
  }

}
