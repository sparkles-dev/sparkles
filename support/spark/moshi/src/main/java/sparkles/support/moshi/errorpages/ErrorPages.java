package sparkles.support.moshi.errorpages;

import com.squareup.moshi.JsonAdapter;
import spark.Request;
import spark.Response;

import static sparkles.support.moshi.DefaultMoshi.defaultMoshi;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;

public final class ErrorPages {
  private static final JsonAdapter<Error> jsonAdapter = defaultMoshi().adapter(Error.class);

  public static void addErrorPages() {

    notFound((req, res) -> renderErrorPage(404, "NOT FOUND", req, res));
    internalServerError((req, res) -> renderErrorPage(500, "INTERNAL SERVER ERROR", req, res));
  }

  public static String renderErrorPage(int code, String message, Request request, Response response) {
    final String headerValue = request.headers("Accept");

    if (headerValue != null && headerValue.startsWith("application/json")) {
      final String responseValue = jsonAdapter.toJson(new Error().withCode(code).withMessage(message));
      response.header("Content-Type", "application/json");
      response.header("Content-Length", "" + responseValue.length());

      return responseValue;
    }

    throw new RuntimeException(ErrorPages.class.getSimpleName() + ": Cannot render error page for non-json response");
  }

}
