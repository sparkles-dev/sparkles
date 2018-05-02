package sparkles.support.testing.rules;

import java.lang.reflect.Field;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import sparkles.support.testing.SparkHttpClient;
import sparkles.support.testing.TestClient;

import static sparkles.support.testing.reflection.Reflection.findAllFieldsAnnotatedWith;
import static sparkles.support.testing.reflection.Reflection.getter;
import static sparkles.support.testing.reflection.Reflection.setter;

public class SparkTestClientResource extends ExternalResource implements TestRule  {
  private Object target;
  private SparkHttpClient client;  

  public SparkTestClientResource(Object target, String serverUrl) {
    this.target = target;
    this.client = init(serverUrl);
  }

  private SparkHttpClient init(String serverUrl) {
    final OkHttpClient okHttpClient = new OkHttpClient();

    return new SparkHttpClient(
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
      SparkHttpClient httpClient = getter(target, field);
	  	httpClient.release();
		  setter(target, field, null);
    }
  }

}
