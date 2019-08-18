package sparkles.replica;

import java.util.Map;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import sparkles.support.common.Environment;
import sparkles.support.common.collections.Collections;
import sparkles.support.javalin.flyway.FlywayPlugin;
import sparkles.support.javalin.springdata.SpringDataPlugin;

public final class TestCommons {
  static {
    System.setProperty(org.slf4j.impl.SimpleLogger.LOG_KEY_PREFIX + "sparkles", "debug");
  }

  static DataSource createDataSource() {
    final DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName(Environment.value("JDBC_DRIVER", "org.sqlite.JDBC"));
    ds.setUrl(Environment.value("JDBC_URL", "jdbc:sqlite:tmp/sqlite/replica-test.db"));
    ds.setUsername(Environment.value("JDBC_USER", ""));
    ds.setPassword(Environment.value("JDBC_PASSWORD", ""));

    return ds;
  }

  static Map<String, Object> createHibernateProperties(DataSource dataSource) {
    return Collections.<String, Object> newMap()
      .put("javax.persistence.nonJtaDataSource", dataSource)
      .put("javax.persistence.transactionType", "RESOURCE_LOCAL")
      .put("hibernate.show_sql", true || Environment.isDevelop())
      .put("hibernate.format_sql", true)
      .put("hibernate.hbm2ddl.auto", "create")
      .put("hibernate.dialect", Environment.value("HIBERNATE_DIALECT", "org.hibernate.dialect.SQLiteDialect"))
      .build();
  }

  static Javalin createTestApp(Logger log, Consumer<JavalinConfig> appCfg) {

    return Javalin
      .create(cfg -> {
        final DataSource ds = createDataSource();

        cfg.registerPlugin(FlywayPlugin.create(ds, "persistence/migration"));
        cfg.registerPlugin(SpringDataPlugin.create("replica", createHibernateProperties(ds)));

        cfg.requestLogger((ctx, ms) -> {
          log.info("{} {} served in {} msec", ctx.method(), ctx.path(), ms);
        });
        cfg.registerPlugin(R.createPlugin());

        appCfg.accept(cfg);
      })
      .get("/hello", ctx -> { ctx.status(204); });
  }
}
