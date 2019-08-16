package sparkles.support.flyway;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;

public final class FlywaySupport {

  public static Flyway create(DataSource dataSource, String locationOnClassPath) {

    return Flyway.configure()
      .dataSource(dataSource)
      .baselineOnMigrate(true)
      .locations(String.format("classpath:%1$s", locationOnClassPath))
      .load();
  }

  public static void runMigrations(DataSource dataSource, String migrationScriptsLocation) {
    create(dataSource, migrationScriptsLocation).migrate();
  }

}
