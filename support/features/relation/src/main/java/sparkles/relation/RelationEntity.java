package sparkles.relation;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

import sparkles.support.common.enums.PersistentEnum;
import sparkles.support.jpa.PersistentEnumToLongConverter;

@NamedQueries({
  @NamedQuery(
    name = RelationEntity.QUERY_FIND_BY_FROM_KIND_AND_FROM_ID,
    query = "SELECT entity FROM sparkles.relation.RelationEntity entity"
      + " WHERE entity.fromKind = :" + RelationEntity.PARAMETER_FROM_KIND
      + " AND entity.fromId = :" + RelationEntity.PARAMETER_FROM_ID
  ),
  @NamedQuery(
    name = RelationEntity.QUERY_FIND_BY_TO_KIND_AND_TO_ID,
    query = "SELECT entity FROM sparkles.relation.RelationEntity entity"
      + " WHERE entity.toKind = :" + RelationEntity.PARAMETER_TO_KIND
      + " AND entity.toId = :" + RelationEntity.PARAMETER_TO_ID
  ),
  @NamedQuery(
    name = RelationEntity.QUERY_FIND_BY_KIND_AND_ID,
    query = "SELECT entity FROM sparkles.relation.RelationEntity entity"
      + " WHERE (entity.toKind = :" + RelationEntity.PARAMETER_KIND + " AND entity.toId = :" + RelationEntity.PARAMETER_TO_ID + ")"
      + " OR    (entity.fromKind = :" + RelationEntity.PARAMETER_KIND + " AND entity.fromId = :" + RelationEntity.PARAMETER_ID + ")"
  )
})
@Entity
@Table(name = "SP_RELATION")
public class RelationEntity {

  public static final String QUERY_FIND_BY_FROM_KIND_AND_FROM_ID = "RelationEntity.QUERY_FIND_BY_FROM_KIND_AND_FROM_ID";
  public static final String QUERY_FIND_BY_TO_KIND_AND_TO_ID = "RelationEntity.QUERY_FIND_BY_TO_KIND_AND_TO_ID";
  public static final String QUERY_FIND_BY_KIND_AND_ID = "RelationEntity.QUERY_FIND_BY_KIND_AND_ID";

  public static final String PARAMETER_FROM_KIND = "PARAMETER_FROM_KIND";
  public static final String PARAMETER_FROM_ID = "PARAMETER_FROM_ID";
  public static final String PARAMETER_TO_KIND = "PARAMETER_TO_KIND";
  public static final String PARAMETER_TO_ID = "PARAMETER_TO_ID";
  public static final String PARAMETER_KIND = "PARAMETER_KIND";
  public static final String PARAMETER_ID = "PARAMETER_ID";

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
    name = "UUID",
    strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "id", updatable = false, nullable = false)
  public UUID id;

  @Column(name = "CREATED_AT")
  private Instant createdAt;

  @Convert(converter = KindConverter.class)
  @Column(name = "FROM_KIND")
  private Kind fromKind;

  @Column(name = "FROM_ID")
  private String fromId;

  @Convert(converter = RelationConverter.class)
  @Column(name = "REL")
  private Relation rel;

  @Convert(converter = DirectionConverter.class)
  @Column(name = "DIRECTION")
  private Direction direction;

  @Convert(converter = KindConverter.class)
  @Column(name = "TO_KIND")
  private Kind toKind;

  @Column(name = "TO_ID")
  private String toId;

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
  }


  public enum Kind implements PersistentEnum {
    FOO(1),
    BAR(2),
    FOOBAR(3);

    private final long p;

    Kind(long p) {
      this.p = p;
    }

    public long persistedValue() {
      return p;
    }
  }

  public enum Direction implements PersistentEnum {
    NO_DIRECTION(0),
    FORWARD(1),
    BACKWARD(2),
    BIDIRECTIONAL(3);

    private final long p;

    Direction(long p) {
      this.p = p;
    }

    public long persistedValue() {
      return p;
    }
  }

  public enum Relation implements PersistentEnum {
    IS_CHILD(1),
    IS_PARENT(2);

    private final long p;

    Relation(long p) {
      this.p = p;
    }

    public long persistedValue() {
      return p;
    }
  }

  @Converter()
  public static class KindConverter extends PersistentEnumToLongConverter<Kind> {

    public KindConverter() {
      super(Kind.class);
    }
  }

  @Converter()
  public static class RelationConverter extends PersistentEnumToLongConverter<Relation> {

    public RelationConverter() {
      super(Relation.class);
    }
  }

  @Converter()
  public static class DirectionConverter extends PersistentEnumToLongConverter<Direction> {

    public DirectionConverter() {
      super(Direction.class);
    }
  }

}
