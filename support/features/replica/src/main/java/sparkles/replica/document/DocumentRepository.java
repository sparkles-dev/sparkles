package sparkles.replica.document;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {

  /*
  @Query("CREATE TABLE DOCUMENTS_?1", native = true)
  void createDocumentsTable(String collection);

  @Query("SELECT * from DOCUMENTS_?1", native = true)
  List<DocumentEntity> findAll(String collection);
  */

}
