package sparkles.support.javalin.security;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Predicate;

import io.javalin.Context;
import io.javalin.UnauthorizedResponse;

import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;

public class XsrfExtension implements Extension {

  private Supplier<UUID> tokenSupplier = () -> UUID.randomUUID();

  private Consumer<Context> tokenGenerator = ctx -> {
    // Generate token
    ctx.cookie("XSRF-TOKEN", tokenSupplier.get().toString());
  };

  private Predicate<Context> tokenVerifier = ctx -> {
    // Verify token
    final String headerValue = ctx.header("X-XSRF-TOKEN");
    final String cookieValue = ctx.cookie("XSRF-TOKEN");

    try {
      final UUID headerUUID = UUID.fromString(headerValue);
      final UUID cookieUUID = UUID.fromString(cookieValue);

      return headerUUID.equals(cookieUUID);
    } catch (Exception e) {
      return false;
    }
  };

  private Predicate<Context> isModifyingOperation = ctx -> {
    return "POST".equalsIgnoreCase(ctx.method())
      || "PUT".equalsIgnoreCase(ctx.method())
      || "DELETE".equalsIgnoreCase(ctx.method());
  };

  private Predicate<Context> isSafeOperation = ctx -> {
    return "GET".equalsIgnoreCase(ctx.method());
  };

  private Function<Context, RuntimeException> exceptionProvider = ctx -> new UnauthorizedResponse();

  private XsrfExtension() {}

  @Override
  public void addToJavalin(JavalinApp app) {

    app.before(ctx -> {

      if (isSafeOperation.test(ctx)) {
        tokenGenerator.accept(ctx);
      } else if (isModifyingOperation.and(tokenVerifier.negate()).test(ctx)) {
        throw exceptionProvider.apply(ctx);
      }

    });

  }

  public static XsrfExtension create() {
    return new XsrfExtension();
  }

}
