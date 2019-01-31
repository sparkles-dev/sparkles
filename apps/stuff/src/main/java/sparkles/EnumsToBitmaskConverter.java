package sparkles;

import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeConverter;

import sparkles.support.common.enums.EnumBitmask;

/**
 * Representation of the Enum values as a bitmask.
 *
 * Caution: the ordinal value of an Enum is used to store it to the database, thus the order of the
 * Enum declaration is persisted to the database!
 * You must not change the order of declaration or your production code will stop to work.
 *
 * #### How To Use
 *
 * You should have an entity with a list of enums as attribute.
 * The column should be stored in a long or number in the SQL database.
 * Create a `@Converter()` subclassing from `EnumsToBitmaskConverter` and apply it to the list
 * of enums like so:
 *
 * ```java
 * @Entity
 * public class FooEntity {
 *
 *   @Column(name = "type") // Should be a NUMBER(38) or similar in the SQL db
 *   @Convert(converter = TypesConverter.class)
 *   private List<Type> types;
 *
 *   public static enum Type {
 *     FOO,
 *     BAR,
 *     FOOBAR
 *   }
 *
 *   @Converter
 *   public static class TypesConverter extends EnumsToBitmaskConverter<Type> {
 *
 *     public TypesConverter() {
 *       super(Type.class);
 *     }
 *   }
 * }
 * ```
 *
 * #### How It Works
 *
 * Internally, we store a NUMBER in the SQL database. The number represents a bitmask for the list
 * of Enm values.
 * It calculates a bitmask value for each enum from its ordinal value.
 *
 * Examples for an Enum of this declaration:
 *
 * ```java
 * public enum Type {
 *     FOO,
 *     BAR,
 *     FOOBAR
 * }
 * ```
 *
 * Will result it bitmask values:
 *
 * ```
 * BITMASK | NUMBER | TYPES
 * 001     | 1      | (FOO)
 * 010     | 2      | (BAR)
 * 011     | 3      | (FOO, BAR)
 * 100     | 4      | (FOOBAR)
 * 101     | 5      | (FOOBAR, FOO)
 * 110     | 6      | (FOOBAR, BAR)
 * 111     | 7      | (FOOBAR, BAR, FOO)
 * ```
 */
public abstract class EnumsToBitmaskConverter<E extends Enum> extends EnumBitmask<E> implements AttributeConverter<List<E>, Long> {

  public EnumsToBitmaskConverter(Class<E> enumClz) {
    super(enumClz, (E anEnum) -> (long) anEnum.ordinal());
  }

  @Override
  public Long convertToDatabaseColumn(List<E> attribute) {
    return toBitmask(attribute);
  }

  @Override
  public List<E> convertToEntityAttribute(Long dbData) {
    if (dbData == null) {
      return Collections.emptyList();
    }

    return fromBitmask(dbData);
  }

}
