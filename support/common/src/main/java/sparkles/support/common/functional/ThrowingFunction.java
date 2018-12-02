package sparkles.support.common.functional;

import java.util.function.Function;

/**
 * A {@link Function} that may throw a non-runtime exception.
 *
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {

  R apply(T input) throws Exception;

  /**
   * Wrap a throwing function in a function that wraps the exception in a runtime exception.
   *
   * Useful where you have a stream that can't handle exceptions but you want to translate the
   * exception.
   *
   * @param function
   * @param exceptionMapper
   * @param <T>
   * @param <R>
   * @return
   */
  static <T, R> Function<T, R> wrap(ThrowingFunction<T, R> function, Function<Exception, RuntimeException> exceptionMapper) {

    return input -> {
      try {
        return function.apply(input);
      } catch (Exception e) {
        throw exceptionMapper.apply(e);
      }
    };
  }

  static <T, R> Function<T, R> wrap(ThrowingFunction<T, R> function) {
    return wrap(function, RuntimeException::new);
  }

}
