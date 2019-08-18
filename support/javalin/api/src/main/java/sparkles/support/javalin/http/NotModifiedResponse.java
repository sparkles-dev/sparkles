package sparkles.support.javalin.http;

import java.util.Collections;

import io.javalin.http.HttpResponseException;

public class NotModifiedResponse extends HttpResponseException {
  private static final long serialVersionUID = 1547829376953051943L;

  public NotModifiedResponse() {
    super(304, "Not Modified", Collections.emptyMap());
	}
}
