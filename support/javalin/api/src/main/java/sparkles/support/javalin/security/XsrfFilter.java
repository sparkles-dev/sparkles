package sparkles.support.javalin.security;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.Predicate;

import io.javalin.Context;
import io.javalin.Handler;
import io.javalin.UnauthorizedResponse;

public class XsrfFilter implements Handler {

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


  @Override
  public void handle(Context ctx) throws Exception {
    if (isSafeOperation.test(ctx)) {
      tokenGenerator.accept(ctx);
    } else if (isModifyingOperation.and(tokenVerifier.negate()).test(ctx)) {
      throw exceptionProvider.apply(ctx);
    }
  }

  public XsrfFilter withTokenSupplier(Supplier<UUID> tokenSupplier) {
    this.tokenSupplier = tokenSupplier;

    return this;
  }

  public XsrfFilter withTokenGenerator(Consumer<Context> tokenGenerator) {
    this.tokenGenerator = tokenGenerator;

    return this;
  }

  public XsrfFilter withTokenVerifier(Predicate<Context> tokenVerifier) {
    this.tokenVerifier = tokenVerifier;

    return this;
  }

  public XsrfFilter withExceptionProvider(Function<Context, RuntimeException> exceptionProvider) {
    this.exceptionProvider = exceptionProvider;

    return this;
  }

  public XsrfFilter whereModifyingOperation(Predicate<Context> isModifyingOperation) {
    this.isModifyingOperation = isModifyingOperation;

    return this;
  }

  public XsrfFilter whereSafeOperation(Predicate<Context> isSafeOperation) {
    this.isSafeOperation = isSafeOperation;

    return this;
  }

}
