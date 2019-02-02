package sparkles;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
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
  public LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false, nullable = false)
  public String createdBy;

  @Column(name = "name")
  public String name;

  @Column(name = "kinds")
  @Convert(converter = KindsConverter.class)
  private List<Kind> kinds = new ArrayList<>();

  public StuffEntity withName(String name) {
    this.name = name;

    return this;
  }

  public List<Kind> getKinds() {
    return new ArrayList<>(kinds);
  }

  public StuffEntity addKind(Kind kind) {
    if (!kinds.contains(kind)) {
      kinds.add(kind);
    }

    return this;
  }

  public StuffEntity setKinds(List<Kind> kinds) {
    this.kinds = new ArrayList<>(kinds);

    return this;
  }

  public enum Kind {
    FOO,
    BAR,
    FOOBAR,
    BLA,
    BLUBB
  }

  @Converter
  public static class KindsConverter extends EnumsToBitmaskConverter<Kind> {

    public KindsConverter() {
      super(Kind.class);
    }
  }
}
