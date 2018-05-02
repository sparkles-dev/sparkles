package sparkles.support.springdata;

import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.HashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import static spark.Spark.before;
import static spark.Spark.afterAfter;

public final class SpringDataSupport {
  private EntityManagerFactory entityManagerFactory;

  void init(EntityManagerFactory entityManagerFactory) {
    // EntityManagerFactory is application-scoped
    this.entityManagerFactory = entityManagerFactory;

    before((request, response) -> {
      // EntityManager and the factory are request scoped
      EntityManager entityManager = createEntityManager(entityManagerFactory);
      JpaRepositoryFactory jpaRepositoryFactory = createJpaRepositoryFactory(entityManager);

      request.attribute("persistence.entityManager", entityManager);
      request.attribute("persistence.jpaRepositoryFactory", jpaRepositoryFactory);

      // automatically begin a transaction for a request
      entityManager.getTransaction().begin();
    });

    afterAfter((request, response) -> {
      EntityManager entityManager = request.attribute("persistence.entityManager");

      // close transaction
      entityManager.getTransaction().commit();
      closeEntityManager(entityManager);
    });
  }

  void shutdown() {
    closeEntityManagerFactory(entityManagerFactory);
  }

  public static void initPersistence(EntityManagerFactory entityManagerFactory) {
    getInstance().init(entityManagerFactory);
  }

  public static void shutdownPersistence() {
    getInstance().shutdown();
  }

  public static void closeEntityManagerFactory(EntityManagerFactory factory) {
    factory.close();
  }

  public static EntityManager createEntityManager(EntityManagerFactory factory) {
    return factory.createEntityManager();
  }

  public static void closeEntityManager(EntityManager entityManager) {
    entityManager.close();
  }

  public static JpaRepositoryFactory createJpaRepositoryFactory(EntityManager entityManager) {
    return new JpaRepositoryFactory(entityManager);
  }

  public static JpaRepositoryFactory jpaRepositoryFactory(Request request) {
    return request.attribute("persistence.jpaRepositoryFactory");
  }

  public static <T extends Repository> T repository(Request request, Class<T> repositoryClazz) {
    return jpaRepositoryFactory(request).getRepository(repositoryClazz);
  }

  private SpringDataSupport() {}

  private static class SingletonHolder {
    private static final SpringDataSupport INSTANCE = new SpringDataSupport();
  }

  private static SpringDataSupport getInstance() {
    return SingletonHolder.INSTANCE;
  }
}
