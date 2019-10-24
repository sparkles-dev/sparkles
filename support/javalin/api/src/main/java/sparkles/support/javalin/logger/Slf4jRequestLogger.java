package sparkles.support.javalin.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.http.Context;
import io.javalin.http.RequestLogger;

public class Slf4jRequestLogger implements RequestLogger {
  private final Logger log;

  public Slf4jRequestLogger(Class<?> targetClz) {
    this.log = LoggerFactory.getLogger(targetClz);
  }

  @Override
  public void handle(Context ctx, Float executionTimeMs) throws Exception {
    log.info("{} {} served in {} msec", ctx.method(), ctx.path(), executionTimeMs);
	}

}
