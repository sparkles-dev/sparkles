package sparkles.replica.document;

import com.google.common.base.Charsets;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class DocumentEntity {

  @Id
  public UUID id = UUID.randomUUID();

  /* Foreign key to collection */
  @Column(name = "collection_id")
  public UUID collectionId;

  public UUID version;

  public ZonedDateTime created;

  public ZonedDateTime lastModified;

  @Lob
  public byte[] json;

  public void setJson(String data) {
    json = data.getBytes(Charsets.UTF_8);
  }

  public String getJson() {
    return new String(json, Charsets.UTF_8);
  }

}
