package sparkles.support.jpa;

import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public final class QueryUtils {

  public static <T> Optional<T> findOne(TypedQuery<T> query) {
    try {
      return Optional.ofNullable(query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  public static <T> List<T> findMany(TypedQuery<T> query) {
    return query.getResultList();
  }

  public static <T> List<T> findMany(Query query) {
    // TODO: safe call?!?
    return query.getResultList();
  }

  /**
   * <p>Escapes the characters in a <code>String</code> to be suitable to pass to
   * an SQL query.</p>
   *
   * <p>For example,
   * <pre>statement.executeQuery("SELECT * FROM MOVIES WHERE TITLE='" +
   *   StringEscapeUtils.escapeSql("McHale's Navy") +
   *   "'");</pre>
   * </p>
   *
   * <p>At present, this method only turns single-quotes into doubled single-quotes
   * (<code>"McHale's Navy"</code> => <code>"McHale''s Navy"</code>). It does not
   * handle the cases of percent (%) or underscore (_) for use in LIKE clauses.</p>
   *
   * see http://www.jguru.com/faq/view.jsp?EID=8881
   * @param str  the string to escape, may be null
   * @return a new String, escaped for SQL, <code>null</code> if null string input
   */
  public static String escapeSql(String str) {
    if (str == null) {
        return null;
    }
    return str.replaceAll("'", "''");
  }
}
