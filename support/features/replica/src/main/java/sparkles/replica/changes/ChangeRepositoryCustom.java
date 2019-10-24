package sparkles.replica.changes;

import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import io.javalin.http.Context;
import sparkles.support.jpa.QueryUtils;

public class ChangeRepositoryCustom {
  private final Context ctx;

  public ChangeRepositoryCustom(Context ctx) {
    this.ctx = ctx;
  }

  public ChangeEntity create(ChangeEntity change) {
    ctx.use(EntityManager.class).persist(change);

    return change;
  }

  public List<ChangeEntity> findByDocumentId(UUID documentId) {
    return QueryUtils.findMany(ctx.use(EntityManager.class)
      .createNativeQuery(ChangeRepository.QUERY_FIND_BY_DOCUMENT_ID, ChangeEntity.class)
      .setParameter(ChangeRepository.PARAM_DOCUMENT_ID, documentId));
  }

}
