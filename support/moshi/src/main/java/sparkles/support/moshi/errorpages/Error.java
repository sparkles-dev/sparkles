package sparkles.support.moshi.errorpages;

public final class Error {

  public int code;
  public String message;

  public Error withCode(int code) {
    this.code = code;

    return this;
  }

  public Error withMessage(String message) {
    this.message = message;

    return this;
  }

}
