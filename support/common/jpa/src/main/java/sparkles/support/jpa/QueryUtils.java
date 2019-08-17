package sparkles.support.jpa;

import java.util.Optional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public final class QueryUtils {

  public static <T> Optional<T> findOne(TypedQuery<T> query) {
    try {
      return Optional.ofNullable(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }
}
