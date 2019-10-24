package sparkles.support.javalin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import io.javalin.http.Context;

public final class Extend {

  public static <T> T inject(Class<T> clz, Context ctx) {
    try {
      Constructor<T> ctr = clz.getConstructor(Context.class);

      return ctr.newInstance(ctx);
    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

}
