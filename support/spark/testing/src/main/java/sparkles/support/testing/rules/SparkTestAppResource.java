package sparkles.support.testing.rules;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import spark.servlet.SparkApplication;
import sparkles.support.testing.TestApp;

import static spark.Spark.awaitInitialization;
import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.stop;
import static sparkles.support.testing.reflection.Reflection.findAllStaticFieldsAnnotatedWith;
import static sparkles.support.testing.reflection.Reflection.findAllStaticMethodsAnnotatedWith;
import static sparkles.support.testing.reflection.Reflection.invoke;
import static sparkles.support.testing.reflection.Reflection.getter;

public class SparkTestAppResource extends ExternalResource implements TestRule {

  private final Class<?> target;
  private final int port;

  public SparkTestAppResource(Class<?> target, int port) {
    this.target = target;
    this.port = port;
  }

  protected void before() {
    // Setup logic that used to be in @BeforeClass
    port(this.port);
    init();

    // initialize `@TestApp static app() {/*..*/}` methods
    List<Method> methods = findAllStaticMethodsAnnotatedWith(target, TestApp.class);
    for (Method method : methods) {
      invoke(method);
    }

    // initialize `@TestApp static SparkApplication app = /*..*/;` fields
    List<Field> fields = findAllStaticFieldsAnnotatedWith(target, TestApp.class);
    for (Field field : fields) {
      Object value = getter(null, field);
      if (value instanceof SparkApplication) {
        ((SparkApplication) value).init();
      }
    }

    awaitInitialization();
  }

  protected void after() {
    // Setup logic that used to be in @AfterClass
    stop();

    // Await server shutdown by http polling (hacky)
    boolean serverShutdown = false;
    while (serverShutdown != true) {
      try {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(new Request.Builder().url(serverUrl()).build()).execute();
      } catch (IOException ioException) {
        // on connectivity error, server was shutdown
        serverShutdown = true;
      }

      if (!serverShutdown) {
        try {
    			Thread.sleep(100);
		    } catch (InterruptedException e) {
          serverShutdown = true;
    		}
      }
  	}
  }

  public String serverUrl() {
    return "http://localhost:" + port;
  }

}
