package sparkles.replica.changes;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChangeRepository extends JpaRepository<ChangeEntity, UUID> {

  @Query("SELECT e FROM ChangeEntity e WHERE e.documentId = :documentId ORDER BY e.date DESC")
  List<ChangeEntity> findByDocumentId(@Param("documentId") UUID documentId);
}
