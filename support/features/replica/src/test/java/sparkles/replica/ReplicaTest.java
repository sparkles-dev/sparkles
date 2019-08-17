package sparkles.replica;

import java.util.Map;
import javax.sql.DataSource;
import io.javalin.Javalin;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import sparkles.replica.collection.CollectionApi;
import sparkles.replica.document.DocumentApi;
import sparkles.support.common.Environment;
import sparkles.support.common.collections.Collections;
import sparkles.support.javalin.flyway.FlywayPlugin;
import sparkles.support.javalin.springdata.SpringDataPlugin;
import sparkles.support.javalin.testing.JavalinTestRunner;
import sparkles.support.javalin.testing.HttpClient;
import sparkles.support.javalin.testing.TestApp;
import sparkles.support.javalin.testing.TestClient;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JavalinTestRunner.class)
@Slf4j
public class ReplicaTest {

  private static DataSource createDataSource() {
    final DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName(Environment.value("JDBC_DRIVER", "org.sqlite.JDBC"));
    ds.setUrl(Environment.value("JDBC_URL", "jdbc:sqlite:tmp/sqlite/replica-test.db"));
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

  @TestApp
  private Javalin app = Javalin.create(cfg -> {
    final DataSource ds = createDataSource();

    cfg.registerPlugin(FlywayPlugin.create(ds, "persistence/migration"));
    cfg.registerPlugin(SpringDataPlugin.create("replica", createHibernateProperties(ds)));

    cfg.registerPlugin(new CollectionApi());
    cfg.registerPlugin(new DocumentApi());

    cfg.requestLogger((ctx, ms) -> {
      log.info("{} {} served in {} msec", ctx.method(), ctx.path(), ms);
    });
  }).get("/hello", ctx -> { ctx.status(204); });

  @TestClient
  private HttpClient client;

  @Test
  public void itCreates() {
    assertThat(app).isNotNull();
    assertThat(client).isNotNull();
    assertThat(client.get("/hello").send().code()).isEqualTo(204);
  }

  @Test
  public void collection_POST() {
    client.post("/collection")
      .json("{\"name\":\"foo\"}")
      .send();
    assertThat(client.response().code()).isEqualTo(201);
    assertThat(client.response().header("Location")).isNotEmpty();

    client.head(client.response().header("Location")).send();
    assertThat(client.response().code()).isEqualTo(204);

    client.post("/collection/foo/document")
      .json("{\"name\":\"John Doe\",\"age\":40 }")
      .send();
    assertThat(client.response().code()).isEqualTo(201);
    final String documentUrl = client.response().header("Location");
    assertThat(documentUrl).startsWith("collection/foo/");
    assertThat(client.responseBodyJson().getString("_id")).isNotEmpty();

    client.head(documentUrl.substring(0, documentUrl.length() - 2)).send();
    assertThat(client.response().code()).isEqualTo(204);
  }

}
