package sparkles;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import sparkles.support.spring.data.AuditingEntityListener;

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
  public LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false, nullable = false)
  public String createdBy;

  public String name;

  public StuffEntity withName(String name) {
    this.name = name;

    return this;
  }
}
