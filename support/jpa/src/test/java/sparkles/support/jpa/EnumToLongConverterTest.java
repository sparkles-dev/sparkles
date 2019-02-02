package sparkles.support.jpa;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumToLongConverterTest {

  public enum Foo {
    FOO,
    BAR,
    FOOBAR
  }

  public static class FooConverter extends EnumToLongConverter<Foo> {

    public FooConverter() {
      super(Foo.class);
    }
  }

  @Test
  public void convertToDatabaseColumn_shouldConvertToOrdinalLongValue() {
    final FooConverter converter = new FooConverter();

    assertThat(converter.convertToDatabaseColumn(Foo.FOO)).isEqualTo(0);
    assertThat(converter.convertToDatabaseColumn(Foo.BAR)).isEqualTo(1);
    assertThat(converter.convertToDatabaseColumn(Foo.FOOBAR)).isEqualTo(2);
  }

  @Test
  public void convertToEntityAttribute_shouldHandleNullValues() {
    final FooConverter converter = new FooConverter();

    assertThat(converter.convertToEntityAttribute(null)).isNull();
  }

  @Test
  public void convertToEntityAttribute_shouldConvertToEnumConstantByOrdinalLongValue() {
    final FooConverter converter = new FooConverter();

    assertThat(converter.convertToEntityAttribute(0L)).isSameAs(Foo.FOO);
    assertThat(converter.convertToEntityAttribute(1L)).isSameAs(Foo.BAR);
    assertThat(converter.convertToEntityAttribute(2L)).isSameAs(Foo.FOOBAR);
  }

  @Test
  public void convertToEntityAttribute_shouldReturnNullForUnknownOrdinals() {
    final FooConverter converter = new FooConverter();

    assertThat(converter.convertToEntityAttribute(-1L)).isNull();
  }

}
