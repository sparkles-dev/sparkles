package sparkles.support.javalin.springdata;

import io.javalin.core.plugin.Plugin;
import io.javalin.http.Context;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import java.util.HashMap;
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
public class SpringDataExtension implements Plugin {

  private final Supplier<EntityManagerFactory> entityManagerFactory;

  @Override
  public void apply(Javalin app) {

    app
      .events(evts -> {
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
      })
      .before(ctx -> {
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

      })
      .after(ctx -> {
        EntityManager entityManager = springData(ctx).entityManager();

        // Commit and close transaction
        entityManager.getTransaction().commit();
        entityManager.close();
      });

  }

  public static SpringDataExtension create(Supplier<EntityManagerFactory> entityManagerFactory) {
    return new SpringDataExtension(entityManagerFactory);
  }

  public static SpringDataExtension create(String persistenceUnitName, Map<String, Object> hibernateProperties) {
    return create(() -> Persistence.createEntityManagerFactory(persistenceUnitName, hibernateProperties));
  }

  public static SpringDataContext springData(Context ctx) {
    return new SpringDataContext(ctx);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class SpringDataContext {
    private final Context ctx;
    private final Map<Class, Object> repos = new HashMap<>();

    /**
     * Returns the EntityManager for the request context
     * @return EntityManager
     */
    public EntityManager entityManager() {
      return ctx.use(EntityManager.class);
    }

    /**
     * Returns the JpaRepositoryFactory the request context
     * @return
     */
    public JpaRepositoryFactory jpaRepositoryFactory() {
      return ctx.use(JpaRepositoryFactory.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T createRepository(Class<T> repositoryClazz) {
      if (repos.containsKey(repositoryClazz)) {
        return (T) repos.get(repositoryClazz);
      } else {
        T repo = jpaRepositoryFactory().getRepository(repositoryClazz);
        repos.put(repositoryClazz, repo);

        return repo;
      }
    }
  }

}
