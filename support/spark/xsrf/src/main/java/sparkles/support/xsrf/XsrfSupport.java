package sparkles.support.xsrf;

import java.util.UUID;

import static spark.Spark.before;
import static spark.Spark.halt;

public class XsrfSupport {

  public static void enableXsrf() {

    before((request, response) -> {
      if ("GET".equalsIgnoreCase(request.requestMethod())) {

        // Generate token
        response.cookie("XSRF-TOKEN", UUID.randomUUID().toString());

      } else if (
        "POST".equalsIgnoreCase(request.requestMethod())
        || "PUT".equalsIgnoreCase(request.requestMethod())
        || "DELETE".equalsIgnoreCase(request.requestMethod())
      ) {

        // Verify token
        final String headerValue = request.headers("X-XSRF-TOKEN");
        final String cookieValue = request.cookie("XSRF-TOKEN");

        try {
          final UUID headerUUID = UUID.fromString(headerValue);
          final UUID cookieUUID = UUID.fromString(cookieValue);

          if (!headerUUID.equals(cookieUUID)) {
            halt(401);
          }
        } catch (Exception e) {
          halt(401);
        }

      }
    });
  }

}
