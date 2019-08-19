package sparkles.support.javalin.flyway;

import io.javalin.core.event.EventHandler;
import io.javalin.core.plugin.Plugin;

import java.util.function.Supplier;

import javax.sql.DataSource;

import io.javalin.Javalin;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import org.flywaydb.core.Flyway;

@RequiredArgsConstructor
@Slf4j
public class FlywayPlugin implements Plugin {
  private final Supplier<DataSource> dataSource;
  private final String migrationScriptPath;

  @Accessors(fluent = true)
  @Setter
  private boolean runBeforeServerStart = false;

  @Override
  public void apply(Javalin app) {

    final EventHandler runMigrations = () -> {
      final DataSource ds = dataSource.get();
      app.attribute(DataSource.class, ds);

      log.debug("Running Flyway database migrations...");
      final int numMigrations = Flyway.configure()
        .dataSource(ds)
        .baselineOnMigrate(true)
        .locations(String.format("classpath:%1$s", migrationScriptPath))
        .load()
        .migrate();

      log.info("Applied Flyway database migrations. Number of migrations: {}", numMigrations);
    };

    if (runBeforeServerStart) {
      try {
        runMigrations.handleEvent();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      app.events(evt -> {
        evt.serverStarting(runMigrations);
      });
    }

  }

  public static FlywayPlugin create(Supplier<DataSource> dataSource, String migrationScriptPath) {
    return new FlywayPlugin(dataSource, migrationScriptPath);
  }

  public static FlywayPlugin create(DataSource dataSource, String migrationScriptPath) {
    return create(() -> dataSource, migrationScriptPath);
  }

  public static FlywayPlugin create(Supplier<DataSource> dataSource, String migrationScriptPath, boolean runBeforeServerStart) {
    final FlywayPlugin plugin = create(dataSource, migrationScriptPath);
    plugin.runBeforeServerStart = runBeforeServerStart;

    return plugin;
  }

  public static FlywayPlugin create(DataSource dataSource, String migrationScriptPath, boolean runBeforeServerStart) {
    final FlywayPlugin plugin = create(dataSource, migrationScriptPath);
    plugin.runBeforeServerStart = runBeforeServerStart;

    return plugin;
  }

}
