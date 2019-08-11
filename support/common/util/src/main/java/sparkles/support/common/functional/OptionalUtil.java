package sparkles.support.common.functional;

import java.util.Optional;

public final class OptionalUtil {

  public static <T> Optional<T> fromThrowing(ThrowingSupplier<T> supplier) {
    try {
      return Optional.ofNullable(supplier.get());
    } catch (Exception e) {
      return Optional.empty();
    }
  }

}
