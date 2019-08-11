package sparkles.support.common;

public enum Environment {
  PRODUCTION,
  DEVELOP;

  private static String value(String name) {
    return System.getProperty(name, System.getenv(name));
  }

  public static boolean hasValue(String name) {
    return value(name) != null;
  }

  public static String value(String name, String defaultValue) {
    return hasValue(name) ? value(name) : defaultValue;
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
    if (hasValue("LOG_LEVEL")) {
      return value("LOG_LEVEL");
    } else {
      return environment() == DEVELOP ? "debug" : "info";
    }
  }

}
