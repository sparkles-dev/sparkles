package sparkles.replica.document;

import com.google.common.base.Charsets;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DocumentEntity {

  @Id
  @Column(updatable = false, nullable = false)
  public UUID id = UUID.randomUUID();

  /* Foreign key to collection */
  @Column(name = "collection_id", updatable = false, nullable = false)
  public UUID collectionId;

  @Column(updatable = true, nullable = false)
  public UUID version;

  @Column(updatable = false, nullable = false)
  public ZonedDateTime created;

  @Column(updatable = true, nullable = true)
  public ZonedDateTime lastModified;

  // @Lob ... SQLite JDBC does not implement BLOB handling, but does support getByte()
  // https://github.com/xerial/sqlite-jdbc/issues/135
  // https://github.com/xerial/sqlite-jdbc/issues/135#issuecomment-522299951
  @Column(updatable = true, nullable = false)
  public byte[] json;

  public void setJson(String data) {
    json = data.getBytes(Charsets.UTF_8);
  }

  public String getJson() {
    return new String(json, Charsets.UTF_8);
  }

}
