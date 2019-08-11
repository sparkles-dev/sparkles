package sparkles.support.javalin;

import io.javalin.Javalin;
import io.javalin.security.Role;
import sparkles.support.common.Environment;
import sparkles.support.common.collections.CollectionUtil;
import sparkles.support.javalin.flyway.FlywayExtension;
import sparkles.support.javalin.spring.data.SpringDataExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public final class BaseApp {
  static {
    System.setProperty(org.slf4j.impl.SimpleLogger.LOG_KEY_PREFIX + "sparkles", Environment.logLevel());
  }

  private final String appName;
  private DataSource dataSource;
  private String flywayScriptPath = Defaults.FLYWAY_SCRIPT_PATH;
  private Map<String, Object> hibernateProperties;
  private String persistenceUnitName;

  private BaseApp(String appName) {
    this.appName = appName;
  }

  /** Overwrites the SQL DataSource. */
  public BaseApp dataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    return this;
  }

  /** Overwrites the location of flyway migration scripts. */
  public BaseApp flywayScriptPath(String flywayScriptPath) {
    this.flywayScriptPath = flywayScriptPath;
    return this;
  }

  /** Overwrites a JPA/Hibernate property. */
  public BaseApp hibernateProperty(String key, Object value) {
    if (hibernateProperties == null) {
      hibernateProperties = new HashMap<String, Object>();
    }
    hibernateProperties.put(key, value);

    return this;
  }

  /** Overwrites JPA/Hibernate properties. */
  public BaseApp hibernateProperties(Map<String, Object> props) {
    hibernateProperties = props;
    return this;
  }

  /** Overwrites JPA persistence unit name. */
  public BaseApp persistenceUnitName(String persistenceUnitName) {
    this.persistenceUnitName = persistenceUnitName;
    return this;
  }

  /** Creates Javalin instance. */
  public Javalin create() {
    if (dataSource == null) {
      dataSource = Defaults.createDataSource(appName);
    }
    if (hibernateProperties == null) {
      hibernateProperties = Defaults.createHibernateProperties(dataSource);
    }
    if (persistenceUnitName == null) {
      persistenceUnitName = appName;
    }

    return Javalin.create()
      .register(FlywayExtension.create(dataSource, flywayScriptPath))
      .register(SpringDataExtension.create(persistenceUnitName, hibernateProperties));
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
  public static Javalin build(String appName) {
    return customize(appName).create();
  }

  /** Pre-configured defaults for a BaseApp. */
  private static final class Defaults {

    /** Location of Flyway scripts on class path. */
    private static final String FLYWAY_SCRIPT_PATH = "persistence/migrations/flyway";

    /** Defaults for data source. */
    private static DataSource createDataSource(String databaseName) {
      final DriverManagerDataSource ds = new DriverManagerDataSource();
      ds.setDriverClassName(Environment.value("JDBC_DRIVER", "org.sqlite.JDBC"));
      ds.setUrl(Environment.value("JDBC_URL", "jdbc:sqlite:sqlite/" + databaseName + ".db"));
      ds.setUsername(Environment.value("JDBC_USER", ""));
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
        .put("hibernate.dialect", Environment.value("HIBERNATE_DIALECT", "org.hibernate.dialect.SQLiteDialect"))
        .build();
    }

  }

  public static Set<Role> requires(Role... roles) {
    return Arrays.stream(roles).collect(Collectors.toSet());
  }

}
