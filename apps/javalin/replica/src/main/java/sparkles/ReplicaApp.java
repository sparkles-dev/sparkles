package sparkles;

import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

import javax.json.JsonObject;
import javax.sql.DataSource;

import okhttp3.Response;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sparkles.replica.collection.CollectionApi;
import sparkles.replica.document.DocumentApi;
import sparkles.replica.version.VersionApi;
import sparkles.replica.R;
import sparkles.support.common.Environment;
import sparkles.support.common.collections.Collections;
import sparkles.support.json.JavaxJson;
import sparkles.support.javalin.flyway.FlywayPlugin;
import sparkles.support.javalin.springdata.SpringDataPlugin;
import sparkles.support.javalin.version.VersionPlugin;

public class ReplicaApp {
  static {
    System.setProperty(org.slf4j.impl.SimpleLogger.LOG_KEY_PREFIX + "sparkles", Environment.logLevel());
  }

  private static final Logger log = LoggerFactory.getLogger(ReplicaApp.class);

  private static DataSource createDataSource() {
    final DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName(Environment.value("JDBC_DRIVER", "org.sqlite.JDBC"));
    ds.setUrl(Environment.value("JDBC_URL", "jdbc:sqlite:tmp/sqlite/replica-wip.db"));
    ds.setUsername(Environment.value("JDBC_USER", ""));
    ds.setPassword(Environment.value("JDBC_PASSWORD", ""));

    return ds;
  }

  private static Map<String, Object> createHibernateProperties(DataSource dataSource) {
    return Collections.<String, Object> newMap()
      .put("javax.persistence.nonJtaDataSource", dataSource)
      .put("javax.persistence.transactionType", "RESOURCE_LOCAL")
      .put("hibernate.show_sql", true || Environment.isDevelop())
      .put("hibernate.format_sql", true)
      .put("hibernate.hbm2ddl.auto", "create")
      .put("hibernate.dialect", Environment.value("HIBERNATE_DIALECT", "org.hibernate.dialect.SQLiteDialect"))
      .build();
  }

  public static void main(String[] args) {

    Javalin.create(cfg -> {
      final DataSource ds = createDataSource();
      //cfg.registerPlugin(FlywayPlugin.create(ds, "persistence/migration"));
      cfg.registerPlugin(SpringDataPlugin.create("replica", createHibernateProperties(ds)));
      cfg.registerPlugin(VersionPlugin.create());

      cfg.registerPlugin(R.createPlugin());
      cfg.registerPlugin(new CollectionApi());
      cfg.registerPlugin(new DocumentApi());
      cfg.registerPlugin(new VersionApi());

      cfg.requestLogger((ctx, ms) -> {
        log.info("{} {} served in {} msec", ctx.method(), ctx.path(), ms);
      });
    }).start(7000);

  }

}
