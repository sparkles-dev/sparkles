package sparkles.replica.document;

import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.slf4j.LoggerFactory;

import io.javalin.http.Context;
import sparkles.support.jpa.QueryUtils;

public class DocumentRepositoryCustom {
  private final Context ctx;

  public DocumentRepositoryCustom(Context ctx) {
    this.ctx = ctx;
  }

  public DocumentEntity create(DocumentEntity entity) {
    ctx.use(EntityManager.class).persist(entity);

    return entity;
  }

  public DocumentEntity update(DocumentEntity entity) {
    ctx.use(EntityManager.class).merge(entity);

    return entity;
  }

  public boolean existsDocumentTable(String collectionName) {
    return QueryUtils.findOne(ctx.use(EntityManager.class)
        .createQuery("SELECT COUNT(*) FROM DOCUMENTS_"+collectionName, Integer.class))
      .filter(val -> val >= 0)
      .isPresent();
  }

  public boolean createDocumentTable(String collectionName) {
    final EntityManager em = ctx.use(EntityManager.class);

    return em.createQuery(QueryUtils.escapeSql("CREATE TABLE IF NOT EXISTS DOCUMENTS_" + collectionName))
      .executeUpdate() > 0;
  }

  public boolean existsByIdAndCollection(UUID id, String collectionName) {
    try {
      final Object result = ctx.use(EntityManager.class)
        .createNativeQuery("SELECT COUNT(d.id) FROM DocumentEntity d JOIN CollectionEntity c ON d.collection_id = c.id WHERE d.id =? and c.name =?")
        .setParameter(1, id)
        .setParameter(2, collectionName)
        .getSingleResult();

      LoggerFactory.getLogger(DocumentRepositoryCustom.class).debug("SQL query result: {}", result);

      return (result != null) && (result instanceof Integer) && ((Integer) result > 0);
    } catch (Exception e) {
      LoggerFactory.getLogger(DocumentRepositoryCustom.class).debug("Exception thrown", e);
      return false;
    }
  }

  public Optional<DocumentEntity> findByIdAndCollection(UUID id, String collectionName, EntityManager em) {

    return QueryUtils.findOne(
      em.createQuery("SELECT d FROM DocumentEntity d JOIN CollectionEntity c ON d.collectionId = c.id WHERE d.id = :id and c.name = :name", DocumentEntity.class)
        .setParameter("id", id)
        .setParameter("name", collectionName)
    );
  }

}
