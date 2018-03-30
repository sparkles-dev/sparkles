package sparkles;

import com.google.common.io.Resources;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.nio.charset.Charset;
import sparkles.support.jwt.SimplePublicKeyProvider;
import static spark.Spark.*;
import static sparkles.support.jwt.JwtSupport.filterAuthenticatedRequest;
import static sparkles.support.moshi.MoshiResponseTransformer.moshiTransformer;

public class SparklesApp {

  public static void main(String[] args) throws IOException {
    String publicKey = Resources.toString(Resources.getResource("jwt/public.key"), Charset.forName("UTF-8"));
    filterAuthenticatedRequest(new SimplePublicKeyProvider(publicKey));

    get("/", (req, res) -> {
      return "hello from sparkjava.com";
    });

    get("/hello-moshi", (req, res) -> {
      return "Hello from Moshi!";
    }, moshiTransformer(String.class));

    get("/hello-moshi-exception", (req, res) -> {
      return 123; // Cannot return an Integer for a moshi adapter that only knows String
    }, moshiTransformer(String.class));

    get("/hello-moshi-multiple-types", (req, res) -> {
      if (req.queryParams("foo") != null) {
        return "Oh, a string";
      } else {
        return 9876;
      }
    }, moshiTransformer(new Moshi.Builder().build(), String.class, Integer.class));

  }

}
