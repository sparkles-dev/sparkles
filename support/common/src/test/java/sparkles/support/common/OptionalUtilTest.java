package sparkles.support.common;

import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionalUtilTest {

  @Test
  public void fromThrowing_shouldReturnEmptyWhenExceptionThrown() {
    final Optional<?> optional = OptionalUtil.fromThrowing(() -> { throw new Exception(); });

    assertThat(optional.isPresent()).isFalse();
  }

  @Test
  public void fromThrowing_shouldReturnValueWhenNoException() {
    final Optional<?> optional = OptionalUtil.fromThrowing(() -> 123);

    assertThat(optional.get()).isEqualTo(123);
  }

  @Test
  public void fromThrowing_shouldHandleNullables() {
    final Optional<?> optional = OptionalUtil.fromThrowing(() -> null);

    assertThat(optional.isPresent()).isFalse();
  }

}
