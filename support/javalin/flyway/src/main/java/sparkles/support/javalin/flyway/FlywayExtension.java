package sparkles.support.javalin.flyway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import javax.sql.DataSource;

import io.javalin.JavalinEvent;

import org.flywaydb.core.Flyway;

import sparkles.support.flyway.FlywaySupport;
import sparkles.support.javalin.Extension;
import sparkles.support.javalin.JavalinApp;

public class FlywayExtension implements Extension {
  private static final Logger LOG = LoggerFactory.getLogger(FlywayExtension.class);

  private final String migrationScriptPath;
  private final Supplier<DataSource> dataSource;

  private FlywayExtension(Supplier<DataSource> dataSource, String migrationScriptPath) {
    this.dataSource = dataSource;
    this.migrationScriptPath = migrationScriptPath;
  }

  @Override
  public void addToJavalin(JavalinApp app) {

    app.event(JavalinEvent.SERVER_STARTING, () -> {
      final DataSource ds = dataSource.get();
      app.attribute(DataSource.class, ds);

      LOG.debug("Running Flyway database migrations...");
      Flyway flyway = new Flyway();
      flyway.setDataSource(ds);
      flyway.setBaselineOnMigrate(true);
      flyway.setLocations(
          String.format("classpath:%1$s", migrationScriptPath)
      );

      flyway.migrate();
    });
  }

  public static FlywayExtension create(Supplier<DataSource> dataSource, String migrationScriptPath) {
    return new FlywayExtension(dataSource, migrationScriptPath);
  }

}
