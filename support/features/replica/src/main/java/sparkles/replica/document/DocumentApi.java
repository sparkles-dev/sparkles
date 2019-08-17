package sparkles.replica.document;

import java.io.StringReader;
import java.util.List;
import java.util.UUID;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;
import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import sparkles.replica.collection.CollectionEntity;
import sparkles.replica.collection.CollectionRepository;
import sparkles.support.javalin.springdata.SpringData;
import lombok.extern.slf4j.Slf4j;

import static org.hibernate.criterion.Restrictions.sqlRestriction;

@Slf4j
public class DocumentApi implements Plugin {

  private static CollectionEntity verifyCollectionOr404(Context ctx) {
    return ctx.use(SpringData.class)
      .repository(CollectionRepository.class)
      .findByName(ctx.pathParam("name"))
      .orElseThrow(NotFoundResponse::new);
  }

  private static JsonObject jsonBody(Context ctx) {
    final JsonReader reader = Json.createReader(new StringReader(ctx.body()));
    try {
      return reader.readObject();
    } finally {
      reader.close();
    }
  }

  @Override
  public void apply(Javalin app) {

    // Create a new document
    app.post("collection/:name/document", ctx -> {
      final CollectionEntity collection = verifyCollectionOr404(ctx);

      final JsonObject document = jsonBody(ctx);
      final UUID id = UUID.fromString(document.getString("_id"));

      final DocmentEntity entity = new DocumentEntity();
      entity.id = id;
      entity.version = UUID.randomUuid();
      entity.collectionId = collection.id;
      entity.setJson(document.toString());

      ctx.status(201);
      ctx.header("Location", "collection/" + collection.name + "/" + entity.id.toString());
      ctx.result(document.toString());
    });

  }

}
