package sparkles;

import com.google.common.io.Resources;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sparkles.support.jwt.SimplePublicKeyProvider;
import static spark.Spark.*;
import static sparkles.support.jwt.JwtSupport.filterAuthenticatedRequest;
import static sparkles.support.moshi.MoshiResponseTransformer.moshiTransformer;

public class SparklesApp {
  private static final Logger LOG = LoggerFactory.getLogger(SparklesApp.class);

  public static void main(String[] args) throws IOException {
    LOG.info("SparklesApp running in {} environment.", Environment.environment());

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
