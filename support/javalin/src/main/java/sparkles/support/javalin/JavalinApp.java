package sparkles.support.javalin;

public class JavalinApp extends Javalin {

  private final Map<Class<?>, Extension> extensions = new HashMap<>();

  /**
   * Registers an anonymous {@link Extension} with the Javalin application.
   *
   * @param extension You're free to implement the extension as a class or a lambda expression
   * @return Self instance for fluent, method-chaining API
   */
  public Javalin extension(Extension extension) {
    extension.register(this);

    return this;
  }

  /**
   * Registers an {@link Extension} with the Javalin application.
   *
   * @param extClazz The extension key
   * @param extension You're free to implement the extension as a class or a lambda expression
   * @return Self instance for fluent, method-chaining API
   */
  public Javalin extension(Class<?> extClazz, Extension extension) {
    if (extClazz == null) {
      throw new IllegalArgumentException("Extension key extClazz must not be null");
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
