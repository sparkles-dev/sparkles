package sparkles.support.okhttp;

import org.slf4j.Logger;
import okhttp3.logging.HttpLoggingInterceptor;

public final class LoggingInterceptor {

  public static HttpLoggingInterceptor slf4j(Logger logger) {
    return create(logger::info);
  }

  public static HttpLoggingInterceptor create(HttpLoggingInterceptor.Logger logger) {
    return create(logger, HttpLoggingInterceptor.Level.BASIC);
  }

  public static HttpLoggingInterceptor create(HttpLoggingInterceptor.Logger logger, HttpLoggingInterceptor.Level level) {
    return new HttpLoggingInterceptor(logger).setLevel(level);
  }

}
