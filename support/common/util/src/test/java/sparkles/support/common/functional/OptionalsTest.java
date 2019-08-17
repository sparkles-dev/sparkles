package sparkles.support.common.functional;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionalsTest {

  @Test
  public void fromThrowing_shouldReturnEmptyWhenExceptionThrown() {
    final Optional<?> optional = Optionals.fromThrowing(() -> { throw new Exception(); });

    assertThat(optional.isPresent()).isFalse();
  }

  @Test
  public void fromThrowing_shouldReturnValueWhenNoException() {
    final Optional<?> optional = Optionals.fromThrowing(() -> 123);

    assertThat(optional.get()).isEqualTo(123);
  }

  @Test
  public void fromThrowing_shouldHandleNullables() {
    final Optional<?> optional = Optionals.fromThrowing(() -> null);

    assertThat(optional.isPresent()).isFalse();
  }

}
