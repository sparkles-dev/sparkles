package sparkles.support.common.functional;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ThrowingFunctionTest {
  // This function throws an Exception
  private final ThrowingFunction<Integer, String> throwing = input -> {
    if (input > 0) {
      throw new Exception();
    } else {
      return "123";
    }
  };

  @Test
  public void wrap_itShouldInvokeTheExceptionMapper() {
    // Wrap it in a function that maps the Exception to a RuntimeException
    Function<Integer, String> runtimeExceptionThrowing = ThrowingFunction.wrap(throwing, IllegalArgumentException::new);

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> runtimeExceptionThrowing.apply(1));
  }

  @Test
  public void wrap_itShouldReturnTheValue() {
    // Wrap it in a function that maps the Exception to a RuntimeException
    Function<Integer, String> runtimeExceptionThrowing = ThrowingFunction.wrap(throwing, IllegalArgumentException::new);

    assertThat(runtimeExceptionThrowing.apply(0)).isEqualTo("123");
  }

  @Test
  public void wrap_itShouldMapToRuntimeExceptionByDefault() {
    // Wrap it in a function that maps the Exception to a RuntimeException
    Function<Integer, String> runtimeExceptionThrowing = ThrowingFunction.wrap(throwing);

    assertThatExceptionOfType(RuntimeException.class)
      .isThrownBy(() -> runtimeExceptionThrowing.apply(1));
  }

}
