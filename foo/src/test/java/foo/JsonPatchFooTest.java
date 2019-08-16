package foo;

import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonPatchFooTest {

  private final String BEFORE = "{\n" +
    "    \"name\": \"John Doe\"\n" +
    "}\n";
  private final String AFTER = "{\n" +
    "    \"age\": 14,\n" +
    "    \"name\": \"Alice Bob\"\n" +
    "}\n";

  @Test
  public void foo() throws IOException {
    System.err.println(BEFORE);
    System.err.println(AFTER);

    // Construct JSON patch from previous and new document
    final JsonObject before = toJson(BEFORE);
    final JsonObject after = toJson(AFTER);
    final JsonPatch patch = Json.createDiff(before, after);

    final String patchJson = patch.toJsonArray().toString();

    System.out.println("--->" + patch.getClass().getCanonicalName());
    System.out.println("--->" + patch.toJsonArray().getClass().getCanonicalName());
    //log.info("JSON Patch: {}", patchJson);
    System.err.println(patchJson.toString());

    /*
    final JsonNode before2 = new ObjectMapper().readTree(BEFORE);
    final JsonNode after2 = new ObjectMapper().readTree(AFTER);
    final JsonNode diff = com.flipkart.zjsonpatch.JsonDiff.asJson(before2, after2);

    //final JsonNode diff = com.github.fge.jsonpatch.diff.JsonDiff.asJson(before2, after2);
    log.info("Jackson JSON Diff: {}", new ObjectMapper().writeValueAsString(diff));
    System.err.println(diff.toString());

     */

    assertThat(patchJson).isEmpty();
  }


  private static JsonObject toJson(String data) {
    return Json.createReader(new StringReader(data)).readObject();
  }

}
