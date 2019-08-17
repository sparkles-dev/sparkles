package sparkles.support.javalin;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.core.plugin.Plugin;
import io.javalin.core.security.Role;
import sparkles.support.common.Environment;
import sparkles.support.common.collections.CollectionUtil;
import sparkles.support.javalin.flyway.FlywayPlugin;
import sparkles.support.javalin.springdata.SpringDataPlugin;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public final class BaseApp {
  static {
    System.setProperty(org.slf4j.impl.SimpleLogger.LOG_KEY_PREFIX + "sparkles", Environment.logLevel());
  }

  private final String appName;
  private Consumer<JavalinConfig> customizations;

  private BaseApp(String appName) {
    this.appName = appName;
  }

  /** Creates Javalin instance. */
  public Javalin create(Plugin... appPlugins) {
    final DataSource dataSource = DevOps.createDataSource(appName);
    final Map<String, Object> hibernateProperties = DevOps.createHibernateProperties(dataSource);

    return Javalin.create(cfg -> {
      // Register base plugins
      cfg.registerPlugin(FlywayPlugin.create(dataSource, DevOps.FLYWAY_SCRIPT_PATH));
      cfg.registerPlugin(SpringDataPlugin.create(appName, hibernateProperties));

      if (customizations != null) {
        customizations.accept(cfg);
      }

      Arrays.stream(appPlugins).forEach(cfg::registerPlugin);
    });
  }

  public BaseApp with(Consumer<JavalinConfig> customizations) {
    this.customizations = customizations;
    return this;
  }

  /**
   * Builds a new Javalin app instance for further customization.
   *
   * @param appName
   * @return
   */
  public static BaseApp customize(String appName) {
    return new BaseApp(appName);
  }

  /**
   * Builds and creates a new Javalin app instance with pre-configured defaults.
   *
   * @param appName
   * @return
   */
  public static Javalin build(String appName, Plugin... appPlugin) {
    return new BaseApp(appName).create(appPlugin);
  }

  /** Pre-configured defaults for a BaseApp. */
  private static final class DevOps {

    /** Location of Flyway scripts on class path. */
    private static final String FLYWAY_SCRIPT_PATH = "persistence/migrations/flyway";

    /** Defaults for data source. */
    private static DataSource createDataSource(String databaseName) {
      final DriverManagerDataSource ds = new DriverManagerDataSource();
      ds.setDriverClassName(Environment.value("JDBC_DRIVER", "org.hsqldb.jdbc.JDBCDataSource"));
      ds.setUrl(Environment.value("JDBC_URL", "jdbc:hsqldb:mem:standalone"));
      ds.setUsername(Environment.value("JDBC_USER", "sa"));
      ds.setPassword(Environment.value("JDBC_PASSWORD", ""));

      return ds;
    }

    /** Defaults for hibernate. */
    private static Map<String, Object> createHibernateProperties(DataSource dataSource) {
      return CollectionUtil.<String, Object> newMap()
        .put("javax.persistence.nonJtaDataSource", dataSource)
        .put("javax.persistence.transactionType", "RESOURCE_LOCAL")
        .put("hibernate.show_sql", Environment.isDevelop())
        .put("hibernate.format_sql", false)
        .put("hibernate.hbm2ddl.auto", "validate")
        .put("hibernate.dialect", Environment.value("HIBERNATE_DIALECT", "org.hibernate.dialect.HSQLDialect"))
        .build();
    }

  }

  public static Set<Role> requires(Role... roles) {
    return Arrays.stream(roles).collect(Collectors.toSet());
  }

}
