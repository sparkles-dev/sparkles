package sparkles.support.javalin.springdata;

import io.javalin.core.plugin.Plugin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import java.util.Map;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import io.javalin.Javalin;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SpringDataPlugin implements Plugin {

  private final Supplier<EntityManagerFactory> entityManagerFactory;
  private final String paths;

  @Override
  public void apply(Javalin app) {

    // Register event listener for creation/closing of database connection
    app.events(evts -> {

      evts.serverStarting(() -> {
        log.debug("Creating EntityManagerFactory for JPA/Hibernate...");
        app.attribute(EntityManagerFactory.class, entityManagerFactory.get());
        log.info("Created EntityManagarFactory.");
      });

      evts.serverStopping(() -> {
        log.debug("Closing EntityManagerFactory...");
        app.attribute(EntityManagerFactory.class).close();
        log.info("Closed EntityManagarFactory.");
      });
    });

    // Register before and after handler
    final Handler beforeHandler = ctx -> {
      // EntityManagerFactory is application scoped
      EntityManagerFactory entityManagerFactory = ctx.appAttribute(EntityManagerFactory.class);
      // EntityManager and JpaRepositoryFactory are request scoped
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);

      ctx.register(EntityManager.class, entityManager);
      ctx.register(JpaRepositoryFactory.class, jpaRepositoryFactory);
      ctx.register(SpringData.class, new SpringData(entityManager, jpaRepositoryFactory));

      // Begin a transaction per request (transaction boundary is request boundary)
      entityManager.getTransaction().begin();
    };
    final Handler afterHandler = ctx -> {
      EntityManager entityManager = springData(ctx).entityManager();

      // Commit and close transaction
      entityManager.getTransaction().commit();
      entityManager.close();
    };
    if (paths != null) {
      app.before(paths, beforeHandler);
      app.after(paths, afterHandler);
    } else {
      app.before(beforeHandler);
      app.after(afterHandler);
    }

  }

  public static SpringDataPlugin create(Supplier<EntityManagerFactory> entityManagerFactory, String paths) {
    return new SpringDataPlugin(entityManagerFactory, paths);
  }

  public static SpringDataPlugin create(Supplier<EntityManagerFactory> entityManagerFactory) {
    return new SpringDataPlugin(entityManagerFactory, null);
  }

  public static SpringDataPlugin create(String persistenceUnitName, Map<String, Object> hibernateProperties, String paths) {
    return create(() -> Persistence.createEntityManagerFactory(persistenceUnitName, hibernateProperties));
  }

  public static SpringDataPlugin create(String persistenceUnitName, Map<String, Object> hibernateProperties) {
    return create(persistenceUnitName, hibernateProperties, null);
  }

  public static SpringData springData(Context ctx) {
    return new SpringData(
      ctx.use(EntityManager.class),
      ctx.use(JpaRepositoryFactory.class)
    );
  }

}
