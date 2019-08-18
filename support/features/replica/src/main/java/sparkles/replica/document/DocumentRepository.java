package sparkles.replica.document;

import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import sparkles.support.jpa.QueryUtils;

import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {

  /*
  @Query("CREATE TABLE DOCUMENTS_?1", native = true)
  void createDocumentsTable(String collection);

  @Query("SELECT * from DOCUMENTS_?1", native = true)
  List<DocumentEntity> findAll(String collection);
  */

  static boolean existsByIdAndCollection(UUID id, String collectionName, EntityManager em) {

    try {
      final Object result = em
        .createNativeQuery("SELECT COUNT(d.id) FROM DocumentEntity d JOIN CollectionEntity c ON d.collection_id = c.id WHERE d.id =? and c.name =?")
        .setParameter(1, id)
        .setParameter(2, collectionName)
        .getSingleResult();

      LoggerFactory.getLogger(DocumentRepository.class).info("SQL query result: {}", result);

      return (result != null) && (result instanceof Integer) && ((Integer) result > 0);
    } catch (Exception e) {
      LoggerFactory.getLogger(DocumentRepository.class).debug("Exception thrown", e);
      return false;
    }
  }

  static Optional<DocumentEntity> findByIdAndCollection(UUID id, String collectionName, EntityManager em) {

    return QueryUtils.findOne(
      em.createQuery("SELECT d FROM DocumentEntity d JOIN CollectionEntity c ON d.collectionId = c.id WHERE d.id = :id and c.name = :name", DocumentEntity.class)
        .setParameter("id", id)
        .setParameter("name", collectionName)
    );
  }

}
