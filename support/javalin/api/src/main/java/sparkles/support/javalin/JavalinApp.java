package sparkles.support.javalin;

import io.javalin.Javalin;
import io.javalin.security.Role;
import sparkles.support.common.Environment;
import sparkles.support.common.collections.CollectionUtil;
import sparkles.support.javalin.flyway.FlywayExtension;
import sparkles.support.javalin.spring.data.SpringDataExtension;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JavalinApp {
  static {
    System.setProperty(org.slf4j.impl.SimpleLogger.LOG_KEY_PREFIX + "sparkles", Environment.logLevel());
  }

  public static Javalin create() {
    return Javalin.create();
  }

  public static Set<Role> requires(Role... roles) {
    return Arrays.stream(roles).collect(Collectors.toSet());
  }

  public static Javalin create(String appName) {
    final DataSource dataSource = createDataSource();

    return Javalin.create()
      .register(FlywayExtension.create(() -> dataSource,
        "persistence/migrations/flyway"))
      .register(SpringDataExtension.create(() -> Persistence.createEntityManagerFactory(
        appName, createHibernateProperties(dataSource))));
  }

  private static DataSource createDataSource() {
    final DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName(Environment.value("JDBC_DRIVER", "org.hsqldb.jdbc.JDBCDataSource"));
    ds.setUrl(Environment.value("JDBC_URL", "jdbc:hsqldb:mem:standalone"));
    ds.setUsername(Environment.value("JDBC_USER", "sa"));
    ds.setPassword(Environment.value("JDBC_PASSWORD", ""));

    return ds;
  }

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
