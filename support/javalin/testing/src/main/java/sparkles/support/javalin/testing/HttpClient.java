package sparkles.support.javalin.testing;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class HttpClient {

  public final OkHttpClient okHttp;
  private final Request requestTemplate;

  public HttpClient(OkHttpClient client, Request requestTemplate) {
    this.okHttp = client;
    this.requestTemplate = requestTemplate;
  }

  public Request.Builder newRequest() {
    return requestTemplate.newBuilder();
  }

  public Request newRequest(String method, String path, RequestBody body) {
    return newRequest()
      .method(method, body)
      .url(requestTemplate.url()
        .newBuilder()
        .addPathSegment(path)
        .build())
      .build();
  }

  public Call newCall(String method, String path) {
    return okHttp.newCall(newRequest()
      .method(method, null)
      .url(requestTemplate.url()
        .newBuilder()
        .addPathSegment(path)
        .build())
      .build());
  }

  public Call newCall(Request request) {
    return okHttp.newCall(request);
  }

  public Call get(String path) {
    return newCall(newRequest("GET", path, null));
  }

  public Call head(String path) {
    return newCall(newRequest("HEAD", path, null));
  }

  public Call post(String path, RequestBody body) {
    return newCall(newRequest("POST", path, body));
  }

  public Call put(String path, RequestBody body) {
    return newCall(newRequest("PUT", path, body));
  }

  public Call delete(String path) {
    return newCall(newRequest("DELETE", path, null));
  }

  public void release() {
    okHttp.dispatcher().executorService().shutdown();
    okHttp.connectionPool().evictAll();
  }

}
