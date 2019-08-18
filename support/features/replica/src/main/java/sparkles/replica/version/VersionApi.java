package sparkles.replica.version;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPatch;

import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.NotFoundResponse;
import sparkles.replica.R;
import sparkles.replica.changes.ChangeEntity;
import sparkles.replica.changes.ChangeRepository;
import sparkles.replica.document.DocumentEntity;
import sparkles.replica.document.DocumentRepository;
import sparkles.support.javalin.springdata.SpringData;
import sparkles.support.json.JavaxJson;

import static sparkles.replica.document.DocumentApi.toPublicDocument;

public class VersionApi implements Plugin {

  @Override
  public void apply(Javalin app) {

    app.get("collection/:name/document/:document/byVersion/:version", ctx -> {
      final UUID documentId = UUID.fromString(ctx.pathParam("document"));
      final UUID versionId = UUID.fromString(ctx.pathParam("version"));

      // SELECT * FROM DocumentEntity where id = :documentId
      final DocumentEntity document = ctx.use(SpringData.class)
        .repository(DocumentRepository.class)
        .findById(documentId)
        .orElseThrow(NotFoundResponse::new);
      JsonObject jsonObject = JavaxJson.readJsonObject(document.getJson());

      // TODO: if (document.version.equals(versiondId)) --> 200 Ok
      if (document.version.equals(versionId)) {

        ctx.header("X-Version-Api", "No patches computed");
        ctx.status(200);
        ctx.use(R.class).result(toPublicDocument(document));

      } else {

        // SELECT * FROM ChangesEntity where documentId = :documentId SORT BY change DESC
        final List<ChangeEntity> changes = ctx.use(SpringData.class)
          .repository(ChangeRepository.class)
          .findByDocumentId(documentId);

        // Collect changes until target version reached ... TODO: possible w/ streams or SQL query?
        boolean found = false;
        final List<ChangeEntity> changesToApply = new ArrayList<>(changes.size());
        for (final ChangeEntity change : changes) {
          changesToApply.add(change);
          if (change.versionFrom.equals(versionId)) {
            found = true;
            break;
          }
        }

        // Target version does not exist
        if (!found) {
          throw new BadRequestResponse("Target version does not exist. version=" + versionId);
        }

        // Collect JsonPatch objects
        final List<JsonPatch> patches = changesToApply.stream()
          .map(ChangeEntity::getPatchReverse)
          .map(JavaxJson::readJsonPatch)
          .collect(Collectors.toList());

        // Apply reverse patches to current document until version generated
        // TODO: replace with stream collector to reduce into JsonObject :-)
        for (final JsonPatch patch : patches) {
          jsonObject = patch.apply(jsonObject);
        }

        ctx.status(501);
        ctx.header("X-Version-Api", "Number of patches applied: " + patches.size());
        // TODO: toPublicDocument with state from corresponding version
        ctx.use(R.class).result(jsonObject);
      }
    });

  }

}
