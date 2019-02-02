package sparkles.support.jpa;

import java.util.function.Function;

import javax.persistence.AttributeConverter;

/**
 * An AttributeConverter that converts enum constants to a number SQL column.
 *
 * **CAUTION**: the enum's ordinal value is used to persist it to the database.
 * Thus, changing the order of declaration in code breaks your production database!
 *
 * #### How To Use
 *
 * You have an enum declaration like this:
 *
 * ```java
 * public enum Foo {
 *   FOO,
 *   BAR,
 *   FOOBAR
 * }
 * ```
 *
 * Subclass from `EnumToLongConverter` and either auto-apply or add a `@Convert()` annotation to
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
 *   public static class FooConverter extends EnumToLongConverter<Foo> {
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
public abstract class EnumToLongConverter<E extends Enum> implements AttributeConverter<E, Long> {

  private final Class<E> enumClz;
  private final Function<E, Long> persistentValueProvider;

  public EnumToLongConverter(Class<E> enumClz) {
    this(enumClz, (E anEnum) -> (long) anEnum.ordinal());
  }

  public EnumToLongConverter(Class<E> enumClz, Function<E, Long> persistentValueProvider) {
    this.enumClz = enumClz;
    this.persistentValueProvider = persistentValueProvider;
  }

  @Override
  public Long convertToDatabaseColumn(E attribute) {
    return persistentValueProvider.apply(attribute);
  }

  @Override
  public E convertToEntityAttribute(Long dbData) {
    if (dbData == null) {
      return null;
    }

    final E[] enumConstants = enumClz.getEnumConstants();
    for (E anEnum : enumConstants) {
      long persistentValue = persistentValueProvider.apply(anEnum);

      if (dbData.equals(persistentValue)) {
        return anEnum;
      }
    }

    return null;
  }

}
