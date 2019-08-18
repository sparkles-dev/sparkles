package sparkles.replica.version;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonPatch;

import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import sparkles.replica.changes.ChangeEntity;
import sparkles.replica.changes.ChangeRepository;
import sparkles.support.javalin.springdata.SpringData;

public class VersionApi implements Plugin {

  @Override
  public void apply(Javalin app) {

    app.get("collection/:name/byVersion/:documentId/:versionId", ctx -> {

      // TODO: SELECT * FROM DocumentEntity where id = :documentId

      // TODO: if (document.version.equals(versiondId)) --> 200 Ok

      // TODO: variant 1 --> sort changes by date, find until date, apply reverse patches sequentially
      // TODO: SELECT * FROM ChangesEntity where documentId = :documentId SORT BY change DESC
      // TODO: changesToApply = changes.collect(collectUntil(ch -> ch.versionTo.equals(versionId)))      // JsonObject document;
      // TODO: JsonObject document = changesToApply.collect((ch, doc) -> ch.patchReverse.apply(doc));

      final List<ChangeEntity> changes = ctx.use(SpringData.class)
        .repository(ChangeRepository.class)
        .findByDocumentId(UUID.fromString(ctx.pathParam("documentId")));

      ctx.status(501);
      ctx.result(changes.stream()
        .map(ch -> ch.getPatchForward() + " // " + ch.getPatchReverse())
        .collect(Collectors.joining(",")));
    });

  }

}
