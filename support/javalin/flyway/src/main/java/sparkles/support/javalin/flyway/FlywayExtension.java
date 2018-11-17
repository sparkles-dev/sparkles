package sparkles.support.javalin.flyway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

import sparkles.support.flyway.FlywaySupport;
import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;

public class FlywayExtension implements Extension {
  private static final Logger LOG = LoggerFactory.getLogger(FlywayExtension.class);

  private final String migrationScriptPath;

  private FlywayExtension(String migrationScriptPath) {
    this.migrationScriptPath = migrationScriptPath;
  }

  @Override
  public void addToJavalin(JavalinApp app) {

    final DataSource dataSource = app.attribute(DataSource.class);

    LOG.debug("Running Flyway database migrations...");
    FlywaySupport.create(dataSource, migrationScriptPath).migrate();
  }

  public static FlywayExtension create(String migrationScriptPath) {
    return new FlywayExtension(migrationScriptPath);
  }

}
