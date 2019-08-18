package sparkles.support.javalin;

import io.javalin.http.Handler;

public final class ClassHandler {

  /**
   * ```java
   * app.get("/foo", ClassHandler.handle(GetFooHandler.class));
   * ```
   */
  public static Handler handle(Class<? extends Handler> clz) throws Exception {

    return ctx -> {
      Handler handler;
      try {
        handler = clz.newInstance();
      } catch (Exception e) {
        throw new RuntimeException("System is errornous. Request handler mis-configured?", e);
      }

      handler.handle(ctx);
    };
  }

  /**
   * ```java
   * import static sparkles.support.javalin.ClassHandler.clz;
   *
   * app.get("/foo", clz(GetFooHandler.class));
   * ```
   */
  public static Handler clz(Class<? extends Handler> clz) throws Exception {
    return handle(clz);
  }

}
