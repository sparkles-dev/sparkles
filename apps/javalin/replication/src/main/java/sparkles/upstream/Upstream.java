package sparkles.upstream;

import org.hsqldb.jdbc.JDBCDataSource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import sparkles.support.common.collections.CollectionUtil;
import sparkles.support.common.Environment;
import sparkles.support.javalin.BaseApp;
import sparkles.support.javalin.spring.data.SpringData;
import sparkles.support.replication.Notification;
import sparkles.support.replication.ReplicationApi;
import sparkles.support.replication.Subscription;

import static sparkles.support.javalin.spring.data.SpringDataExtension.springData;

@Slf4j
public class Upstream {
  private static final String SELF_URL = "http://localhost:7000";

  public Javalin init() {
    final DataSource dataSource = createDataSource();
    final OkHttpClient okHttpClient = new OkHttpClient();

    return BaseApp.customize("upstream")
      .dataSource(dataSource)
      .flywayScriptPath("persistence/migrations")
      .hibernateProperties(createHibernateProperties(dataSource))
      .create()
      .get("/replication/subscription/:id", (ctx) -> {
        String id = ctx.pathParam("id");
        String since = ctx.queryParam("since", "");

        ctx.status(200);
        if ("".equals(since)) {
          ctx.result("For subscription " + id + ": here are all the results in the world.");
        } else {
          ctx.result("Here are your results since " + since);
        }
      })
      .delete("/replication/subscription/:id", (ctx) -> {
        String id = ctx.pathParam("id");
        SubscriptionRepository repository = springData(ctx).createRepository(SubscriptionRepository.class);

        repository.deleteById(UUID.fromString(id));

        ctx.status(200);
      })
      .post("/replication/subscription", (ctx) -> {
        // parse request
        Subscription reqBody = ctx.bodyAsClass(Subscription.class);

        // save subscription to database
        SubscriptionRepository repository = ctx.use(SpringData.class).repository(SubscriptionRepository.class);
        SubscriptionEntity entity = new SubscriptionEntity();
        entity.notifyUrl = reqBody.notifyUrl();
        entity.topic = reqBody.topic() != null ? reqBody.topic() : "*";
        repository.save(entity);

        reqBody
          .id(entity.id)
          .subscriptionUrl(SELF_URL + "/replication/subscription/" + entity.id.toString());
        log.info("Created subscription", entity.id.toString());

        // Build response
        ctx.status(201);
        ctx.header("Location", "/replication/subscription/" + entity.id.toString());
        ctx.json(reqBody);
      })
      .post("/do-something-to-notify", (ctx) -> {
        SubscriptionRepository repository = springData(ctx).createRepository(SubscriptionRepository.class);
        List<SubscriptionEntity> subscribers = repository.findByTopic(ctx.queryParam("topic", "*"));

        ReplicationApi api = new ReplicationApi.Builder()
          .okHttpClient(okHttpClient)
          .baseUrl(SELF_URL)
          .build();

        subscribers.forEach(sub -> {
          Notification n = new Notification()
            .url(SELF_URL + "/replication/subscription/" + sub.id.toString());

          try {
            api.notify(sub.notifyUrl, n).execute();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });

        ctx.status(200);
      });
  }

  private static DataSource createDataSource() {
    final JDBCDataSource ds = new JDBCDataSource();
    ds.setUrl(Environment.value("JDBC_URL", "jdbc:sqlite:sqlite/sample.db")); // jdbc:sqlite:sample.db | "jdbc:sqlite::memory"
    ds.setUser(Environment.value("JDBC_USER", ""));
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
