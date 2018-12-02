package sparkles;

import java.util.UUID;

import sparkles.support.javalin.spring.data.rest.RestRepositoryHandler;

public class StuffHandler extends RestRepositoryHandler<StuffRepository, StuffEntity, UUID> {

  public StuffHandler() {
    super(StuffRepository.class, StuffEntity.class);
  }

  @Override
  protected String toId(StuffEntity e) {
    return e.id.toString();
  }

}
