package sparkles.replica.changes;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.common.base.Charsets;

@Entity
public class ChangeEntity {

  @Id
  @Column(updatable = false, nullable = false)
  public UUID id = UUID.randomUUID();

  @Column(name = "document_id", updatable = false, nullable = false)
  public UUID documentId;

  @Column(updatable = false, nullable = false)
  public ZonedDateTime date;

  @Column(updatable = false, nullable = false)
  public UUID versionFrom;

  @Column(updatable = false, nullable = false)
  public UUID versionTo;

  @Column(updatable = false, nullable = false)
  public byte[] patchForward;

  @Column(updatable = false, nullable = false)
  public byte[] patchReverse;

  public String getPatchForward() {
    return new String(patchForward, Charsets.UTF_8);
  }

  public void setPatchForward(String value) {
    patchForward = value.getBytes(Charsets.UTF_8);
  }

  public String getPatchReverse() {
    return new String(patchReverse, Charsets.UTF_8);
  }

  public void setPatchReverse(String value) {
    patchReverse = value.getBytes(Charsets.UTF_8);
  }

}
