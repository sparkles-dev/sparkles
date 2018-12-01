package sparkles;

import org.hsqldb.jdbc.JDBCDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import sparkles.support.javalin.Environment;
import sparkles.support.javalin.JavalinApp;
import sparkles.support.common.collections.CollectionUtil;
import sparkles.support.replication.*;

import sparkles.support.javalin.spring.data.SpringDataExtension;

@Slf4j
public class Replicator {
  static Subscription currentSubscription;

  public static void main(String[] args) {
    new Replicator().upstream().start();

    new Replicator().downstream().start();
  }

  public Javalin upstream() {
    final DataSource dataSource = createDataSource();

    return JavalinApp.create()
      .register(SpringDataExtension.create(() -> Persistence.createEntityManagerFactory(
        "replicator", createHibernateProperties(dataSource))))
      .get("/replication/subscription/:id", (ctx) -> {
        String id = ctx.pathParam("id");

        ctx.status(200);
        ctx.json(id);
      })
      .post("/replication/subscription", (ctx) -> {
        UUID id = UUID.randomUUID();
        Subscription s = ctx.bodyAsClass(Subscription.class)
          .id(id)
          .subscriptionUrl("http://localhost:7000/replication/subscription/" + id.toString());

        currentSubscription = s;

        ctx.status(201);
        ctx.header("Location", "/replication/subscription/" + id.toString());

        ctx.json(s);
      })
      .post("/do-something-to-notify", (ctx) -> {
        final ReplicationApi api = new ReplicationApi.Builder()
          .baseUrl("http://localhost:7000/")
          .build();

        Notification n = new Notification()
          .url(currentSubscription.subscriptionUrl());
        api.notify(currentSubscription.notifyUrl(), n).execute();

        ctx.status(200);
      })
      .port(7000);

  }

  public Javalin downstream() {
    final DataSource dataSource = createDataSource();

    return JavalinApp.create()
      .register(SpringDataExtension.create(() -> Persistence.createEntityManagerFactory(
        "replicator", createHibernateProperties(dataSource))))
      .post("/replication/notification", (ctx) -> {
        final Notification n = ctx.bodyAsClass(Notification.class);
        final ReplicationApi api = new ReplicationApi.Builder()
          .baseUrl("http://localhost:7000/")
          .build();

        String response = api.get(n.url).execute().body().string();
        log.error("Fetched data: " + response);

        ctx.status(204);
      })
      .post("/do-something-to-subscribe", (ctx) -> {
        final ReplicationApi api = new ReplicationApi.Builder()
          .baseUrl("http://localhost:7000/")
          .build();

        Subscription s = new Subscription()
          .notifyUrl("http://localhost:7001/replication/notification");

        Subscription subscription = api.subscribe(s).execute().body();

        ctx.json(subscription);
        ctx.status(200);
      })
      .port(7001);
  }

  private static DataSource createDataSource() {
    final JDBCDataSource ds = new JDBCDataSource();
    ds.setUrl(Environment.value("JDBC_URL", "jdbc:hsqldb:mem:standalone"));
    ds.setUser(Environment.value("JDBC_USER", "sa"));
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
