package sparkles;

import org.hsqldb.jdbc.JDBCDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import io.javalin.Javalin;
import io.javalin.JavalinEvent;

import sparkles.support.javalin.Environment;
import sparkles.support.javalin.JavalinApp;
import sparkles.support.javalin.flyway.FlywayExtension;
import sparkles.support.javalin.keycloak.security.KeycloakAccessManager;
import sparkles.support.javalin.keycloak.security.KeycloakRoles;
import sparkles.support.javalin.spring.data.Auditing;
import sparkles.support.javalin.spring.data.AuditingExtension;
import sparkles.support.javalin.spring.data.SpringDataExtension;

import static sparkles.support.javalin.spring.data.SpringDataExtension.springData;

public class StuffApp {
  static {
    System.setProperty(org.slf4j.impl.SimpleLogger.LOG_KEY_PREFIX + "sparkles", Environment.logLevel());
  }

  private static final Logger LOG = LoggerFactory.getLogger(StuffApp.class);

  public static void main(String[] args) {
    new StuffApp().init().start();
  }

  private Javalin init() {
    final DataSource dataSource = createDataSource();

    return JavalinApp.create()
      .register(FlywayExtension.create(() -> dataSource, "persistence/migrations/flyway"))
      .register(SpringDataExtension.create(() -> Persistence.createEntityManagerFactory("stuff", createHibernateProperties(dataSource))))
      .register(AuditingExtension.create((ctx) -> {
        // TODO: resolve auditor from request context
        return "foo";
      }))
      .accessManager(KeycloakAccessManager.create(
        "https://foobar",
        "realm",
        (claims) -> {
          // TODO: resolve role from keycloak stuff
          return KeycloakRoles.ANYONE;
        }
      ))
      .get("/", (ctx) -> {
        StuffRepository repository = springData(ctx).createRepository(StuffRepository.class);

        List<StuffEntity> stuffs = repository.findAll();
        for (StuffEntity stuff : stuffs) {
          LOG.info("stuff is {} // {}", stuff.createdAt, stuff.createdBy);
        }

        ctx.result("count: " + stuffs.size());
      }, Collections.singleton(KeycloakRoles.ANYONE))
      .post("/", (ctx) -> {
        Object auditor = Auditing.getStrategy().resolveCurrentContext().getCurrentAuditor().get();
        LOG.info("Current auditor is {}", auditor);

        StuffRepository repository = springData(ctx).createRepository(StuffRepository.class);
        StuffEntity entity = repository.save(new StuffEntity().withName("foobararar"));

        ctx.result(entity.id.toString()).status(201);
      });
  }

  private static DataSource createDataSource() {
    JDBCDataSource ds = new JDBCDataSource();
    ds.setUrl("jdbc:hsqldb:mem:standalone");
    ds.setUser("sa");
    ds.setPassword("");

    return ds;
  }

  private static Map<String, Object> createHibernateProperties(DataSource dataSource) {
    Map<String, Object> props = new HashMap<String, Object>();
    props.put("javax.persistence.nonJtaDataSource", dataSource);
    props.put("javax.persistence.transactionType", "RESOURCE_LOCAL");
    props.put("hibernate.show_sql", Environment.isDevelop());
    props.put("hibernate.format_sql", false);
    props.put("hibernate.hbm2ddl.auto", "validate");
    props.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");

    return props;
  }

}
