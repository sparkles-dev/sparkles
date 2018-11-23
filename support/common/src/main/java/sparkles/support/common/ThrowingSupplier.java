package sparkles.support.common;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T> {

  T get() throws Exception;
}
