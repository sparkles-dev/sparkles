package sparkles.replica;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.javalin.Javalin;
import sparkles.replica.collection.CollectionApi;
import sparkles.replica.document.DocumentApi;
import sparkles.support.javalin.testing.HttpClient;
import sparkles.support.javalin.testing.JavalinTestRunner;
import sparkles.support.javalin.testing.TestApp;
import sparkles.support.javalin.testing.TestClient;
import sparkles.support.json.JavaxJson;

import static org.assertj.core.api.Assertions.assertThat;

import javax.json.JsonObject;

@RunWith(JavalinTestRunner.class)
public class DocumentApiTest {

  @TestApp
  private Javalin app = TestCommons.createTestApp(DocumentApiTest.class, cfg -> {
    cfg.registerPlugin(new CollectionApi());
    cfg.registerPlugin(new DocumentApi());
  });

  @TestClient
  private HttpClient client;

  @Test
  public void document_POST() {
    // POST: ensure collection exists
    client.post("/collection")
      .json("{\"name\":\"foo\"}")
      .send();
    assertThat(client.response().code()).isEqualTo(201);

    // POST: create document
    client.post("/collection/foo/document")
      .json("{\"name\":\"John Doe\",\"age\":40 }")
      .send();
    assertThat(client.response().code()).isEqualTo(201);
    final String documentUrl = client.response().header("Location");
    assertThat(documentUrl).startsWith("collection/foo/");
    final JsonObject documentJson = client.responseBodyJson();
    assertThat(documentJson).isNotNull();
    final String documentId = JavaxJson.propertyString(documentJson, "/_id");
    assertThat(documentId).isNotEmpty();
    final String documentVersion = JavaxJson.propertyString(documentJson, "/_meta/version");
    assertThat(documentVersion).isNotEmpty();

    // HEAD: document should exist
    client.head(documentUrl).send();
    assertThat(client.response().code()).isEqualTo(204);
  }

  @Test
  @Ignore
  public void document_PUT() {
    // TODO: should update, check version conflicts
    //...
  }

  @Test
  @Ignore
  public void document_GET_HEAD_conditional() {
    final String documentUrl = null;
    final JsonObject updatedDocument = null;

    // GET: conditional If-Modified-Since
    client.get(documentUrl)
      .header("If-Modified-Since", JavaxJson.propertyString(updatedDocument, "/_meta/lastModified"))
      .send();
    assertThat(client.response().code()).isEqualTo(304);

    // HEAD: conditional If-None-Match
    client.head(documentUrl)
      .header("If-None-Match", JavaxJson.propertyString(updatedDocument, "/_meta/version"))
      .send();
    assertThat(client.response().code()).isEqualTo(304);
  }

}
