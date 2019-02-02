package sparkles.support.common.enums;

/**
 * Marker interface for enum declarations that provide an explicit long value for persisting
 * to a database.
 *
 * #### How To Use
 *
 * Declare an enum implementing the `persistedValue()` method like so:
 *
 * ```java
 * public enum Foo {
 *   FOO(1),
 *   BAR(4),
 *   FOOBAR(12);
 *
 *   private final long value;
 *
 *   Foo(long value) {
 *     this.value = value;
 *   }
 *
 *   @Override
 *   public long persistedValue() }
 *     return value;
 *   }
 * }
 * ```
 */
public interface PersistentEnum {

  long persistedValue();
}
