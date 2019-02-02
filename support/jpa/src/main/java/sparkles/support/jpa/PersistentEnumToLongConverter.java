package sparkles.support.jpa;

import javax.persistence.AttributeConverter;

import sparkles.support.common.enums.PersistentEnum;

/**
 * An AttributeConverter that converts enum constants to a number SQL column.
 * The enum must implement the `PersistentEnum` interface.
 *
 * **CAUTION**: the enum's `persistedValue()` is used to read/write it to/from the database.
 * Thus, changing those magic values breaks your production database!
 *
 * #### How To Use
 *
 * You have an enum declaration implementing `PersistenEnum` like this:
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
 *
 * Subclass from `PersistentEnumToLongConverter` and either auto-apply or add a `@Convert()` annotation to
 * the entity's member:
 *
 * ```java
 * @Entity
 * public class MyEntity {
 *
 *   @Column
 *   @Convert(converter = FooConverter.class)
 *   private Foo foo;
 *
 *   @Converter
 *   public static class FooConverter extends PersistentEnumToLongConverter<Foo> {
 *
 *     public FooConverter() {
 *       super(Foo.class);
 *     }
 *   }
 * }
 * ```
 *
 * @param <E>
 */
public abstract class PersistentEnumToLongConverter<E extends Enum & PersistentEnum>
  extends EnumToLongConverter<E>
  implements AttributeConverter<E, Long> {

  protected PersistentEnumToLongConverter(Class<E> enumClz) {
    // We must pass a lambda expression and not a method reference to the super constructor
    // https://stackoverflow.com/a/34160332
    super(enumClz, (E anEnum) -> anEnum.persistedValue());
  }

}
