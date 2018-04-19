package sparkles.support.javalin.testing.rules;

import java.lang.reflect.Field;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import sparkles.support.javalin.testing.HttpClient;
import sparkles.support.javalin.testing.TestClient;

import static sparkles.support.javalin.testing.reflection.Reflection.findAllFieldsAnnotatedWith;
import static sparkles.support.javalin.testing.reflection.Reflection.getter;
import static sparkles.support.javalin.testing.reflection.Reflection.setter;

public class TestClientResource extends ExternalResource implements TestRule  {
  private Object target;
  private HttpClient client;

  public TestClientResource(Object target, String serverUrl) {
    this.target = target;
    this.client = init(serverUrl);
  }

  private HttpClient init(String serverUrl) {
    final OkHttpClient okHttpClient = new OkHttpClient();

    return new HttpClient(
      okHttpClient,
      new Request.Builder().url(serverUrl).build()
    );
  }

  protected void before() {
    List<Field> fields = findAllFieldsAnnotatedWith(target.getClass(), TestClient.class);
    for (Field field : fields) {
      setter(target, field, client);
    }
  }

  protected void after() {
    List<Field> fields = findAllFieldsAnnotatedWith(target.getClass(), TestClient.class);
    for (Field field : fields) {
      HttpClient httpClient = getter(target, field);
	  	httpClient.release();
		  setter(target, field, null);
    }
  }

}
