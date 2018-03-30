package sparkles.support.persistence.migrations;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;

public final class MigrationSupport {

  public static Flyway createFlyway(DataSource dataSource, String locationOnClassPath) {
    Flyway flyway = new Flyway();
    flyway.setDataSource(dataSource);
    flyway.setBaselineOnMigrate(true);
    flyway.setLocations(
        String.format("classpath:%1$s", locationOnClassPath)
    );

    return flyway;
  }

  public static void runMigrations(DataSource dataSource, String migrationScriptsLocation) {
    createFlyway(dataSource, migrationScriptsLocation).migrate();
  }

}
