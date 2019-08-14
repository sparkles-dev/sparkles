package sparkles.support.javalin.version;

public class VersionPlugin {}

/*
import com.google.common.io.Resources;
import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class VersionPlugin implements Plugin {
  private static String VERSION;

  @Override
  public void apply(Javalin app) {

    try {
      final InputStream inputStream = Resources.getResource("build.properties").openStream();
      final Properties properties = new Properties();
      properties.load(inputStream);

      VERSION = properties.getProperty("build.version", "<unknown>");
    } catch (IOException e) {
      log.error("Cannot read build.properties", e);
    }

    app.get("/version", ctx -> {

      ctx.status(200);
      ctx.contentType("application/json");
      ctx.result("{\"version\":\"" + VERSION + "\"}");
    });

	}

}
*/
