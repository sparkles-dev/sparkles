package sparkles.replica.document;

import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import io.javalin.Javalin;
import io.javalin.core.plugin.Plugin;
import io.javalin.http.Context;
import sparkles.support.javalin.springdata.SpringData;
import lombok.extern.slf4j.Slf4j;

import static org.hibernate.criterion.Restrictions.sqlRestriction;

@Slf4j
public class DocumentApi implements Plugin {

  @Override
  public void apply(Javalin app) {

    // Create a new document
    app.post("collection/:name/document", ctx -> {
      final JsonObject document = Json.createReader(new StringReader(ctx.body())).readObject();

      log.info("JSON document: {}", document);
    });

  }

}
