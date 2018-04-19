package sparkles.support.javalin.testing.rules;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import io.javalin.Javalin;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import sparkles.support.javalin.testing.TestApp;

import static sparkles.support.javalin.testing.reflection.Reflection.findAllFieldsAnnotatedWith;
import static sparkles.support.javalin.testing.reflection.Reflection.findAllMethodsAnnotatedWith;
import static sparkles.support.javalin.testing.reflection.Reflection.invoke;
import static sparkles.support.javalin.testing.reflection.Reflection.getter;

public class TestAppResource extends ExternalResource implements TestRule {

  private final Class<?> target;
  private Javalin testApp;

  public TestAppResource(Class<?> target) {
    this.target = target;
  }

  @Override
  protected void before() throws IllegalAccessException, InstantiationException {
    // Setup logic that used to be in @BeforeClass
    final Object targetValue = this.target.newInstance();

    // initialize `@TestApp Javalin app() {/*..*/}` methods
    List<Method> methods = findAllMethodsAnnotatedWith(target, TestApp.class);
    for (Method method : methods) {
      testApp = invoke(method, targetValue);
    }

    // initialize `@TestApp Javalin app = /*..*/;` fields
    List<Field> fields = findAllFieldsAnnotatedWith(target, TestApp.class);
    for (Field field : fields) {
      testApp = getter(targetValue, field);
    }

    // Await server start
    testApp.start();
  }

  @Override
  protected void after() {
    // Setup logic that used to be in @AfterClass

    // Await server shutdown
    testApp.stop();
  }

  public String serverUrl() {
    return "http://localhost:" + testApp.port();
  }

}
