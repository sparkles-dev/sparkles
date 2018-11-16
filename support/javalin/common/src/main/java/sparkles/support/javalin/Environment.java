package sparkles.support.javalin;

public enum Environment {
  PRODUCTION,
  DEVELOP;

  public static String value(String name, String defaultValue) {
    final String systemValue = System.getProperty(name, System.getenv(name));

    return systemValue != null ? systemValue : defaultValue;
  }

  public static Environment environment() {
    final String env = value("ENVIRONMENT", "production");
    if ("production".equalsIgnoreCase(env)) {
      return PRODUCTION;
    } else if ("develop".equalsIgnoreCase(env)) {
      return DEVELOP;
    }

    return PRODUCTION;
  }

  public static boolean isDevelop() {
    return environment() == DEVELOP;
  }

  public static boolean isProduction() {
    return environment() == PRODUCTION;
  }

  public static String logLevel() {
    return environment() == DEVELOP ? "debug" : "info";
  }

}
