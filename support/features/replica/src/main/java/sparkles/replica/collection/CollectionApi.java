package sparkles.replica.collection;

import java.util.List;
import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import io.javalin.http.ConflictResponse;
import sparkles.support.javalin.springdata.SpringData;

public class CollectionApi implements Plugin {

  @Override
  public void apply(Javalin app) {

    // List collections
    app.get("collection", ctx -> {
      final List<CollectionEntity> result = ctx.use(SpringData.class)
        .repository(CollectionRepository.class)
        .findAll();

      ctx.status(200);
      ctx.json(result);
    });

    // Create collection
    app.post("collection", ctx -> {
      final CollectionEntity entity = ctx.bodyAsClass(CollectionEntity.class);

      final boolean exists = ctx.use(SpringData.class)
        .repository(CollectionRepository.class)
        .existsByName(entity.name);
      if (exists) {
        throw new ConflictResponse();
      }

      final CollectionEntity result = ctx.use(SpringData.class)
        .repository(CollectionRepository.class)
        .save(entity);

      ctx.status(201);
      ctx.header("Location", "collection/" + result.name);
      ctx.json(result);
    });

    // Check if collection exists
    app.head("collection/:name", ctx -> {
      final boolean exists = ctx.use(SpringData.class)
        .repository(CollectionRepository.class)
        .existsByName(ctx.pathParam("name"));

      if (exists) {
        ctx.status(204);
      } else {
        ctx.status(404);
      }
    });

    app.get("collection/:name", ctx -> {
      ctx.status(501);
    });

  }

}
