package sparkles;

import com.squareup.moshi.Moshi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.servlet.SparkApplication;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;
import static sparkles.auth.Authentication.initAuth;
import static sparkles.support.moshi.MoshiResponseTransformer.moshiTransformer;

public class SparklesApp implements SparkApplication {
  static {
    System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, Environment.logLevel());
  }

  private static final Logger LOG = LoggerFactory.getLogger(SparklesApp.class);

  public static void main(String[] args) {
    new SparklesApp().init();
  }

  @Override
  public void init() {
    LOG.info("SparklesApp running in {} environment.", Environment.environment());
    initAuth();

    LOG.debug("Initializing routes...");
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

    if (Environment.environment() == Environment.DEVELOP) {
      enableDebugScreen();
    }
  }

}
