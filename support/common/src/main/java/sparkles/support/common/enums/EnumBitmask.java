package sparkles.support.common.enums;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * An utility class that converts a collection of enum values to a bitmask.
 * It's useful for storing a list of enums to a number SQL column in a database.
 *
 * #### How To Use
 *
 * You have an enum declaration like so:
 *
 * ```java
 * public enum Foo {
 *   FOO,
 *   BAR,
 *   FOOBAR
 * }
 * ```
 *
 * Subclass from `EnumBitmask` and provide a lambda expression for mapping an enum constant to a
 * distinct value for the bitmask.
 * For example, you could use the enum's ordinal value (order of declaration in code) for
 * representation in the bitmask:
 *
 * ```java
 * public static class FooBitmask extends EnumBitmask<Foo> {
 *
 *   public FooBitmask() {
 *     super(Foo.class, (Foo foo) -> (long) foo.ordinal());
 *   }
 * }
 * ```
 *
 * @param <E>
 */
public class EnumBitmask<E extends Enum> {

  @FunctionalInterface
  public interface BitmaskValueProvider<E extends Enum> extends Function<E, Long> {
  }

  private final Class<E> enumClz;
  private final BitmaskValueProvider<E> maskValue;

  public EnumBitmask(Class<E> enumClz, BitmaskValueProvider<E> maskValue) {
    this.enumClz = enumClz;
    this.maskValue = maskValue;
  }

  private long maskValue(E anEnum) {
    long simpleValue = maskValue.apply(anEnum);

    if (simpleValue < 0) {
      throw new RuntimeException("The bitmask value for an enum constant must be greater or equal to zero, enum=" + anEnum);
    }

    long powerOfTwo = 1;
    for (long i = 0; i < simpleValue; i++) {
      powerOfTwo *= 2;
    }

    return powerOfTwo;
  }

  public long toBitmask(Collection<E> enumConstants) {
    long bitmask = 0;

    for (E anEnum : enumConstants) {
      bitmask = bitmask | maskValue(anEnum);
    }

    return bitmask;
  }

  public List<E> fromBitmask(long bitmask) {
    final E[] enumConstants = enumClz.getEnumConstants();
    final List<E> list = new ArrayList<>(enumConstants.length);

    if (bitmask < 0) {
      return list;
    }

    for (E anEnum : enumConstants) {
      boolean isInMask = (bitmask & maskValue(anEnum)) > 0;

      if (isInMask) {
        list.add(anEnum);
      }
    }

    return list;
  }

}
