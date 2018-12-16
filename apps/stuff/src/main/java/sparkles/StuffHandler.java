package sparkles;

import java.util.UUID;

import io.javalin.Context;
import sparkles.support.javalin.spring.data.rest.RestRepositoryHandler;

public class StuffHandler extends RestRepositoryHandler<StuffRepository, StuffEntity, UUID> {

  public StuffHandler() {
    super("foo/:id", StuffRepository.class, StuffEntity.class);
  }

  @Override
  protected UUID toId(Context ctx) {
    return UUID.fromString(ctx.pathParam(":id"));
  }

  @Override
  protected String toUrl(StuffEntity entity) {
    return "/foo/" + entity.id.toString();
  }

}
