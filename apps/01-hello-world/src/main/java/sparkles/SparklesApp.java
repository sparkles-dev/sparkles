package sparkles;

import static spark.Spark.*;

public class SparklesApp {

  public static void main(String[] args) {
    get("/", (req, res) -> {
      return "hello from sparkjava.com";
    });
  }
}
