package sparkles.replica.collection;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CollectionEntity {

  @Id
  @Column(updatable = false, nullable = false)
  public UUID id = UUID.randomUUID();

  @Column(updatable = false, nullable = false)
  public String name;
}
