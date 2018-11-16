package sparkles.support.javalin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import io.javalin.BadRequestResponse;
import io.javalin.UnauthorizedResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtensionTest {

  private static JavalinApp app;
  private static String origin = null;
  private static OkHttpClient okhttp = new OkHttpClient.Builder().build();

  @BeforeClass
  public static void setup() throws IOException {
    app = JavalinApp.create();
    app.port(0).start();
    origin = "http://localhost:" + app.port();
  }

  @After
  public void clear() {
    //app.clearMatcherAndMappers();
  }

  @AfterClass
  public static void tearDown() {
    app.stop();
  }

  @Test
  public void test_helloWorldExtension() throws Exception {
    app.extension((app) -> {
      app.before("/protected", ctx -> {
        throw new UnauthorizedResponse("Protected");
      });
      app.get("/", ctx -> ctx.result("Hello world"));
    });

    Response response = okhttp.newCall(new Request.Builder()
      .get()
      .url(origin + "/").build()
    ).execute();
    assertThat(response.code()).isEqualTo(200);
    assertThat(response.body().string()).isEqualTo("Hello world");

    Response response2 = okhttp.newCall(new Request.Builder()
      .get()
      .url(origin + "/protected").build()
    ).execute();
    assertThat(response2.code()).isEqualTo(401);
    assertThat(response2.body().string()).isEqualTo("Protected");
  }

  @Test
  public void test_javaClassExtension() throws Exception {
    app.extension(new JavaClassExtension("Foobar!"));

    Response response = okhttp.newCall(new Request.Builder()
      .get()
      .url(origin + "/").build()
    ).execute();
    assertThat(response.code()).isEqualTo(400);
    assertThat(response.body().string()).isEqualTo("Foobar!");
  }

  static class JavaClassExtension implements Extension {
    private final String magicValue;

    public JavaClassExtension(String magicValue) {
      this.magicValue = magicValue;
    }

    public String getMagicValue() {
      return magicValue;
    }

    @Override
    public void register(JavalinApp app) {
      app.before(ctx -> {
        throw new BadRequestResponse(magicValue);
      });
    }
  }

  @Test
  public void test_javaClassExtensions() {
    app.extension(new JavaClassExtension("Foobar!"))
      .extension((app) -> {
        assertThat(app.extension(JavaClassExtension.class).getMagicValue()).isEqualTo("Foobar!");
      });
  }

  @Test
  public void test_registerOrderIsFirstComeFirstServe() {
    List<Integer> values = new ArrayList<>();
    app.extension(app -> { values.add(1); })
      .extension(app -> { values.add(2); });

    assertThat(values.get(0)).isEqualTo(1);
    assertThat(values.get(1)).isEqualTo(2);
  }

}
