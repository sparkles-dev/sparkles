package sparkles.replica.changes;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChangeRepository extends JpaRepository<ChangeEntity, UUID> {
  static final String QUERY_FIND_BY_DOCUMENT_ID = "SELECT e FROM ChangeEntity e WHERE e.documentId = :documentId ORDER BY e.date DESC";
  static final String PARAM_DOCUMENT_ID = "documentId";

  @Query(ChangeRepository.QUERY_FIND_BY_DOCUMENT_ID)
  List<ChangeEntity> findByDocumentId(@Param(ChangeRepository.PARAM_DOCUMENT_ID) UUID documentId);

}
