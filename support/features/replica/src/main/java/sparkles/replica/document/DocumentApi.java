package sparkles.replica.document;

import java.io.StringReader;
import java.time.ZonedDateTime;
import java.util.Collections;
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
import io.javalin.http.Handler;
import io.javalin.http.HttpResponseException;
import io.javalin.http.NotFoundResponse;

import lombok.extern.slf4j.Slf4j;
import sparkles.replica.R;
import sparkles.replica.changes.ChangeEntity;
import sparkles.replica.changes.ChangeRepository;
import sparkles.replica.collection.CollectionEntity;
import sparkles.replica.collection.CollectionRepository;
import sparkles.support.javalin.http.NotModifiedResponse;
import sparkles.support.javalin.http.PreconditionFailedResponse;
import sparkles.support.javalin.springdata.SpringData;
import sparkles.support.json.JavaxJson;

import static sparkles.replica.R.jsonExceptionResponse;

@Slf4j
public class DocumentApi implements Plugin {

  private static CollectionEntity verifyCollectionOr404(Context ctx) {
    return ctx.use(SpringData.class)
      .repository(CollectionRepository.class)
      .findByName(ctx.pathParam("name"))
      .orElseThrow(NotFoundResponse::new);
  }

  public static void verifyConditionalRequest(DocumentEntity entity, Context ctx) {

    // Conditional request handling: If-None-Match
    if (ctx.header("If-None-Match") != null) {
      UUID version;
      try {
        version = UUID.fromString(ctx.header("If-None-Match"));
      } catch (Exception e) {
        throw new PreconditionFailedResponse();
      }

      if (entity.version.equals(version)) {
        throw new NotModifiedResponse();
      }
    }

    // Conditional request handling: If-Modified-Since
    if (ctx.header("If-Modified-Since") != null) {
      ZonedDateTime modifiedSince;
      try {
        modifiedSince = ZonedDateTime.parse(ctx.header("If-Modified-Since"));
      } catch (Exception e) {
        throw new PreconditionFailedResponse();
      }

      if (entity.lastModified.isAfter(modifiedSince) || entity.lastModified.isEqual(modifiedSince)) {
        throw new NotModifiedResponse();
      }
    }

  }

  private static JsonObjectBuilder buildUpon(JsonObject object) {
    return Optional.ofNullable(object)
      .map(Json::createObjectBuilder)
      .orElse(Json.createObjectBuilder());
  }

  public static JsonObject toPublicDocument(DocumentEntity entity) {
    return toPublicDocument(
      entity,
      JavaxJson.readJsonObject(entity.getJson())
    );
  }

  private static JsonObject toPublicDocument(DocumentEntity entity, JsonObject json) {
    return buildUpon(json)
      .add("_id", entity.id.toString())
      .add("_meta", Json.createObjectBuilder()
        .add("version", entity.version.toString())
        .add("lastModified", entity.lastModified.toString())
        .build())
      .build();
  }

  private static JsonObject toStorageDocument(JsonObject json) {
    return buildUpon(json)
      .remove("_id")
      .remove("_meta")
      .build();
  }

  @Override
  public void apply(Javalin app) {

    // Create document
    app.post("collection/:name/document", ctx -> {
      final CollectionEntity collection = verifyCollectionOr404(ctx);

      final JsonObject document = ctx.use(R.class).bodyJsonObject();
      log.info("JSON document: {}", document.toString());

      final UUID id = UUID.fromString(document.getString("_id", UUID.randomUUID().toString()));

      final DocumentEntity entity = new DocumentEntity();
      entity.id = id;
      entity.created = ZonedDateTime.now();
      entity.lastModified = entity.created;
      entity.version = UUID.randomUUID();
      entity.collectionId = collection.id;
      final JsonObject publicDocument = toPublicDocument(entity, document);
      final JsonObject storageDoc = toStorageDocument(publicDocument);
      entity.setJson(storageDoc.toString());

      ctx.use(SpringData.class)
        .repository(DocumentRepository.class)
        .save(entity);

      ctx.status(201);
      ctx.header("Location", "collection/" + collection.name + "/document/" + entity.id.toString());
      ctx.use(R.class).result(publicDocument);
    });

    // Update document
    app.put("collection/:name/document", ctx -> {
      // Parse request body
      final JsonObject document = ctx.use(R.class).bodyJsonObject();
      final UUID documentId = ctx.use(R.class).jsonPropertyUuid("/_id");
      final UUID versionId = ctx.use(R.class).jsonPropertyUuid("/_meta/version");
      log.debug("Updating document: {}", documentId);

      // Check if document exists, or 404
      final DocumentEntity entity = DocumentRepository.findByIdAndCollection(
        documentId,
        ctx.pathParam("name"),
        ctx.use(SpringData.class).entityManager()
      ).orElseThrow(NotFoundResponse::new);

      // Check version matches, or 409 Conflict
      if (!versionId.equals(entity.version)) {
        throw new ConflictResponse("Version in JSON body does not match w/ current server state. You would override existing data.");
      }

      // Generate JSON patches for change streams
      final JsonStructure existing = JavaxJson.readJsonObject(entity.getJson());
      final JsonObject updated = toStorageDocument(document);
      final JsonPatch forward = Json.createDiff(existing, updated);
      final JsonPatch reverse = Json.createDiff(updated, existing);
      log.debug("Forward patch: {}", forward);
      log.debug("Reverse patch: {}", reverse);

      // Record change and persist
      final UUID newVersionId = UUID.randomUUID();
      final ChangeEntity change = new ChangeEntity();
      change.documentId = documentId;
      change.date = ZonedDateTime.now();
      change.versionFrom = entity.version;
      change.versionTo = newVersionId;
      change.setPatchForward(forward.toString());
      change.setPatchReverse(reverse.toString());
      ctx.use(SpringData.class)
        .repository(ChangeRepository.class)
        .save(change);

      // Persist document entity
      entity.lastModified = ZonedDateTime.now();
      entity.version = newVersionId;
      entity.setJson(updated.toString());
      ctx.use(SpringData.class)
        .repository(DocumentRepository.class)
        .save(entity);

      // Return current state of document
      ctx.status(200);
      ctx.header("Location", "collection/" + ctx.pathParam("name") + "/document/" + entity.id);
      ctx.use(R.class).result(toPublicDocument(entity, updated));
    });

    // Check if document exists
    app.head("collection/:name/document/:id", ctx -> {
      final DocumentEntity entity = DocumentRepository.findByIdAndCollection(
        UUID.fromString(ctx.pathParam("id")),
        ctx.pathParam("name"),
        ctx.use(SpringData.class).entityManager()
      ).orElseThrow(NotFoundResponse::new);

      verifyConditionalRequest(entity, ctx);

      ctx.status(204);
    });

    // Get document
    app.get("collection/:name/document/:id", ctx -> {
      final DocumentEntity entity = DocumentRepository.findByIdAndCollection(
        UUID.fromString(ctx.pathParam("id")),
        ctx.pathParam("name"),
        ctx.use(SpringData.class).entityManager()
      ).orElseThrow(NotFoundResponse::new);

      log.debug("found document: {}", entity.getJson());
      verifyConditionalRequest(entity, ctx);

      final JsonObject json = toPublicDocument(entity);

      ctx.status(200);
      ctx.use(R.class).result(json);
    });

    app.exception(JsonException.class, jsonExceptionResponse(400));
    app.exception(IllegalArgumentException.class, jsonExceptionResponse(400));
    app.exception(RuntimeException.class, jsonExceptionResponse(500));

  }

}
