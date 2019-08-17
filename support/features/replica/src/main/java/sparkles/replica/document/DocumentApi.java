package sparkles.replica.document;

import java.io.StringReader;
import java.util.Optional;
import java.util.UUID;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import sparkles.replica.collection.CollectionEntity;
import sparkles.replica.collection.CollectionRepository;
import sparkles.support.javalin.springdata.SpringData;
import lombok.extern.slf4j.Slf4j;

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

  private static JsonObjectBuilder buildUpon(JsonObject object) {
    return Optional.ofNullable(object)
      .map(Json::createObjectBuilder)
      .orElse(Json.createObjectBuilder());
  }

  private static JsonObject toDocument(DocumentEntity entity, JsonObject document) {
    return buildUpon(document)
      .add("_id", entity.id.toString())
      .add("_meta", Json.createObjectBuilder()
        .add("version", entity.version.toString())
        .build())
      .build();
  }


  @Override
  public void apply(Javalin app) {

    // Create a new document
    app.post("collection/:name/document", ctx -> {
      final CollectionEntity collection = verifyCollectionOr404(ctx);

      final JsonObject document = jsonBody(ctx);
      log.info("JSON document: {}", document.toString());

      final UUID id = UUID.fromString(document.getString("_id", UUID.randomUUID().toString()));

      final DocumentEntity entity = new DocumentEntity();
      entity.id = id;
      entity.version = UUID.randomUUID();
      entity.collectionId = collection.id;
      final JsonObject toPersist = toDocument(entity, document);
      entity.setJson(toPersist.toString());

      ctx.use(SpringData.class)
        .repository(DocumentRepository.class)
        .save(entity);

      ctx.status(201);
      ctx.header("Location", "collection/" + collection.name + "/document/" + entity.id.toString());
      ctx.result(toPersist.toString());
    });

    app.head("collection/:name/document/:id", ctx -> {
      /*
      verifyCollectionOr404(ctx);

      final boolean exists = ctx.use(SpringData.class)
        .repository(DocumentRepository.class)
        .existsById(UUID.fromString(ctx.pathParam("id")));
      */
      final boolean exists = DocumentRepository.existsByIdAndCollection(
        UUID.fromString(ctx.pathParam("id")),
        ctx.pathParam("name"),
        ctx.use(SpringData.class).entityManager()
      );

      if (exists) {
        ctx.status(204);
      } else {
        ctx.status(404);
      }
    });

  }

}
