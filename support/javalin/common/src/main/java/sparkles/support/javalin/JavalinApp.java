package sparkles.support.javalin;

import java.util.HashMap;
import java.util.Map;

import io.javalin.Javalin;

public class JavalinApp extends Javalin {

  private final Map<Class<?>, Extension> extensions = new HashMap<>();

  protected JavalinApp() {
    super();
  }

  public static JavalinApp create() {
    return new JavalinApp();
  }

  /**
   * Registers an {@link Extension} with the Javalin application.
   *
   * If the extension is a lambda expression or an anonymous class, it's considered an anonymous
   * extension.
   *
   * @param extension You're free to implement the extension as a class or a lambda expression
   * @return Self instance for fluent, method-chaining API
   */
  public JavalinApp extension(Extension extension) {
    Class<?> extClazz = extension.getClass();
    if (extClazz == null) {
      throw new IllegalArgumentException("Extension must have a class to be registered");
    }
    extension.register(this);
    extensions.put(extClazz, extension);

    return this;
  }

  /**
   * Returns an {@link Extension} that was registered with the Javalin application.
   *
   * @param extClazz The class implementing the `Extension` interface
   * @return An instance of `T` or null
   */
  @SuppressWarnings("unchecked")
  public <T> T extension(Class<T> extClazz) {

    return (T) extensions.get(extClazz);
  }

}
