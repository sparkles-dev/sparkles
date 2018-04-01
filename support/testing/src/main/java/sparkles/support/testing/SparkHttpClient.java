package sparkles.support.testing;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public final class SparkHttpClient {

  public final OkHttpClient okHttp;
  private final Request requestTemplate;

  public SparkHttpClient(OkHttpClient client, Request requestTemplate) {
    this.okHttp = client;
    this.requestTemplate = requestTemplate;
  }

  public Request.Builder newRequest() {
    return requestTemplate.newBuilder();
  }

  public Call newCall(String method, String path) {
    return okHttp.newCall(newRequest()
      .method(method, null)
      .url(requestTemplate.url().newBuilder()
        .addPathSegment(path)
        .build())
      .build());
  }

  public void release() {
    okHttp.dispatcher().executorService().shutdown();
    okHttp.connectionPool().evictAll();
  }

}
