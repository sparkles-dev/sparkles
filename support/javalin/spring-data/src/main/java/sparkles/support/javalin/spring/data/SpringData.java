package sparkles.support.javalin.spring.data;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import javax.persistence.EntityManager;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SpringData {

  private final SpringDataExtension.SpringDataContext wrapped;

  public EntityManager entityManager() {
    return wrapped.entityManager();
  }

  public JpaRepositoryFactory jpaRepositoryFactory() {
    return wrapped.jpaRepositoryFactory();
  }

  public <T> T createRepository(Class<T> repositoryClazz) {
    return wrapped.createRepository(repositoryClazz);
  }

  public <T> T repository(Class<T> repositoryClz) {
    return wrapped.repository(repositoryClz);
  }

}
