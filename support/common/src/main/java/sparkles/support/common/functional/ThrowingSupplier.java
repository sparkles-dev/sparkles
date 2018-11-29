package sparkles.support.common.functional;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T> {

  T get() throws Exception;
}
