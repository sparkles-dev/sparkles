package sparkles.replica.document;

import java.io.StringReader;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonPatch;

import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import io.javalin.http.ConflictResponse;
import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.NotFoundResponse;

import lombok.extern.slf4j.Slf4j;
import sparkles.replica.changes.ChangeEntity;
import sparkles.replica.changes.ChangeRepository;
import sparkles.replica.collection.CollectionEntity;
import sparkles.replica.collection.CollectionRepository;
import sparkles.support.javalin.springdata.SpringData;
import sparkles.support.json.JavaxJson;

@Slf4j
public class DocumentApi implements Plugin {

  private static <T extends Exception> ExceptionHandler<T> jsonExceptionResponse(int status) {
    return (e, ctx) -> {
      final JsonObject msg = Json.createObjectBuilder()
        .add("code", status)
        .add("type", e.getClass().getCanonicalName())
        .add("message", e.getMessage())
        .build();

      ctx.status(status);
      jsonResult(msg, ctx);
    };
  }

  private static CollectionEntity verifyCollectionOr404(Context ctx) {
    return ctx.use(SpringData.class)
      .repository(CollectionRepository.class)
      .findByName(ctx.pathParam("name"))
      .orElseThrow(NotFoundResponse::new);
  }

  private static void jsonResult(JsonStructure json, Context ctx) {
    ctx.header("Content-Type", "application/json");
    ctx.result(json.toString());
  }

  private static JsonObject jsonBody(Context ctx) {
    return jsonObject(ctx.body());
  }

  private static JsonObject jsonObject(String value) {
    final JsonReader reader = Json.createReader(new StringReader(value));
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

    // Create document
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
      jsonResult(toPersist, ctx);
    });

    // Update document
    app.put("collection/:name/document", ctx -> {
      // Parse request body
      final JsonObject document = jsonBody(ctx);
      final UUID documentId = UUID.fromString(JavaxJson.propertyString(document, "/_id"));
      final UUID versionId = UUID.fromString(JavaxJson.propertyString(document, "/_meta/version"));
      log.debug("Updating document: {}", documentId);

      // Check if document exists, or 404
      final DocumentEntity entity = ctx.use(SpringData.class)
        .repository(DocumentRepository.class)
        .findById(documentId)
        .orElseThrow(NotFoundResponse::new);
      /*
      DocumentRepository.findByIdAndCollection(
        documentId,
        ctx.pathParam("name"),
        ctx.use(SpringData.class).entityManager()
      ).orElseThrow(NotFoundResponse::new);
      */

      // Check version matches, or 409 Conflict
      if (!versionId.equals(entity.version)) {
        throw new ConflictResponse();
      }

      // Generate JSON patches for change streams
      final JsonStructure existing = JavaxJson.readJsonObject(entity.getJson());
      final JsonPatch forward = Json.createDiff(existing, document);
      final JsonPatch reverse = Json.createDiff(document, existing);
      log.debug("Forward patch: {}", forward);
      log.debug("Reverse patch: {}", reverse);

      // Persist change event
      final ChangeEntity change = new ChangeEntity();
      change.documentId = documentId;
      change.changeDate = ZonedDateTime.now();
      change.versionBefore = entity.version;
      change.versionAfter = UUID.randomUUID();
      ctx.use(SpringData.class)
        .repository(ChangeRepository.class)
        .save(change);

      // TODO: return proper new document (not just change...)
      ctx.status(501);
    });

    // Check if document exists
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

    // Get document
    app.get("collection/:name/document/:id", ctx -> {
      final DocumentEntity entity = DocumentRepository.findByIdAndCollection(
        UUID.fromString(ctx.pathParam("id")),
        ctx.pathParam("name"),
        ctx.use(SpringData.class).entityManager()
      ).orElseThrow(NotFoundResponse::new);

      log.info("found document: {}", entity.getJson());
      final JsonObject json = JavaxJson.readJsonObject(entity.getJson());
      jsonResult(json, ctx);
    });

    app.exception(JsonException.class, jsonExceptionResponse(400));
    app.exception(IllegalArgumentException.class, jsonExceptionResponse(400));

  }

}
