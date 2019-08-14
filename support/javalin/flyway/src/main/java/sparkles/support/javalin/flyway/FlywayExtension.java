package sparkles.support.javalin.flyway;

import io.javalin.core.plugin.Plugin;

import java.util.function.Supplier;

import javax.sql.DataSource;

import io.javalin.Javalin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.flywaydb.core.Flyway;

@RequiredArgsConstructor
@Slf4j
public class FlywayExtension implements Plugin {
  private final Supplier<DataSource> dataSource;
  private final String migrationScriptPath;

  @Override
  public void apply(Javalin app) {

    app.events(evt -> {

      evt.serverStarting(() -> {
        final DataSource ds = dataSource.get();
        app.attribute(DataSource.class, ds);

        log.debug("Running Flyway database migrations...");
        Flyway flyway = new Flyway();
        flyway.setDataSource(ds);
        flyway.setBaselineOnMigrate(true);
        flyway.setLocations(
          String.format("classpath:%1$s", migrationScriptPath)
        );

        flyway.migrate();
        log.info("Applied Flyway database migrations.");
      });
    });

  }

  public static FlywayExtension create(Supplier<DataSource> dataSource, String migrationScriptPath) {
    return new FlywayExtension(dataSource, migrationScriptPath);
  }

  public static FlywayExtension create(DataSource dataSource, String migrationScriptPath) {
    return create(() -> dataSource, migrationScriptPath);
  }

}
