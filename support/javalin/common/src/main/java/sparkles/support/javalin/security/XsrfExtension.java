package sparkles.support.javalin.security;

import java.util.UUID;

import io.javalin.UnauthorizedResponse;

import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;

public class XsrfExtension implements Extension {

  private XsrfExtension() {}

  @Override
  public void addToJavalin(JavalinApp app) {

    app.before(ctx -> {

      if ("GET".equalsIgnoreCase(ctx.method())) {

        // Generate token
        ctx.cookie("XSRF-TOKEN", UUID.randomUUID().toString());

      } else if (
        "POST".equalsIgnoreCase(ctx.method())
        || "PUT".equalsIgnoreCase(ctx.method())
        || "DELETE".equalsIgnoreCase(ctx.method())
      ) {

        // Verify token
        final String headerValue = ctx.header("X-XSRF-TOKEN");
        final String cookieValue = ctx.cookie("XSRF-TOKEN");

        try {
          final UUID headerUUID = UUID.fromString(headerValue);
          final UUID cookieUUID = UUID.fromString(cookieValue);

          if (!headerUUID.equals(cookieUUID)) {
            throw new UnauthorizedResponse();
          }
        } catch (Exception e) {
          throw new UnauthorizedResponse();
        }

      }

    });

  }

  public static XsrfExtension create() {
    return new XsrfExtension();
  }

}
