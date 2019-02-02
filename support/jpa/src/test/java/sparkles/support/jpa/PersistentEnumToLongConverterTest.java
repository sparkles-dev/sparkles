package sparkles.support.jpa;

import org.junit.Test;

import sparkles.support.common.enums.PersistentEnum;

import static org.assertj.core.api.Assertions.assertThat;

public class PersistentEnumToLongConverterTest {

  public enum Foo implements PersistentEnum {
    FOO {
      @Override
      public long persistedValue() {
        return 1;
      }
    },
    BAR {
      @Override
      public long persistedValue() {
        return 2;
      }
    },
    FOOBAR {
      @Override
      public long persistedValue() {
        return 7;
      }
    }
  }

  public static class FooConverter extends PersistentEnumToLongConverter<Foo> {

    public FooConverter() {
      super(Foo.class);
    }
  }

  @Test
  public void convertToDatabaseColumn_shouldConvertToPersistedValue() {
    final FooConverter converter = new FooConverter();

    assertThat(converter.convertToDatabaseColumn(Foo.FOO)).isEqualTo(Foo.FOO.persistedValue());
    assertThat(converter.convertToDatabaseColumn(Foo.BAR)).isEqualTo(2);
    assertThat(converter.convertToDatabaseColumn(Foo.FOOBAR)).isEqualTo(7);
  }

  @Test
  public void convertToEntityAttribute_shouldHandleNullValues() {
    final FooConverter converter = new FooConverter();

    assertThat(converter.convertToEntityAttribute(null)).isNull();
  }

  @Test
  public void convertToEntityAttribute_shouldConvertToEnumConstantByPersistedValue() {
    final FooConverter converter = new FooConverter();

    assertThat(converter.convertToEntityAttribute(Foo.FOO.persistedValue())).isSameAs(Foo.FOO);
    assertThat(converter.convertToEntityAttribute(2L)).isSameAs(Foo.BAR);
    assertThat(converter.convertToEntityAttribute(7L)).isSameAs(Foo.FOOBAR);
  }

  @Test
  public void convertToEntityAttribute_shouldReturnNullForUnknownPersistedValues() {
    final FooConverter converter = new FooConverter();

    assertThat(converter.convertToEntityAttribute(-1L)).isNull();
  }

}
