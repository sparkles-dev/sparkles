package sparkles;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import sparkles.support.javalin.spring.data.auditing.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class StuffEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
    name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "id", updatable = false, nullable = false)
  public UUID id;

  @CreatedDate
  @Column(name = "created_at", updatable = false, nullable = false)
  public Instant createdAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false, nullable = false)
  public String createdBy;

  @Column(name = "name")
  public String name;

  public StuffEntity withName(String name) {
    this.name = name;

    return this;
  }
}
