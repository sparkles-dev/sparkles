package sparkles.downstream;

import java.io.IOException;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sparkles.support.common.functional.ThrowingFunction;
import sparkles.support.javalin.JavalinApp;
import sparkles.support.replication.Notification;
import sparkles.support.replication.ReplicationApi;
import sparkles.support.replication.Subscription;

@Slf4j
public class Downstream {
  private static final String UPSTREAM_URL = "http://localhost:7000/";

  public Javalin init() {

    return JavalinApp.create()
      .attribute(OkHttpClient.class, new OkHttpClient())
      .post("/replication/notification", (ctx) -> {
        final Notification notification = ctx.bodyAsClass(Notification.class);
        final ReplicationApi api = new ReplicationApi.Builder()
          .okHttpClient(ctx.appAttribute(OkHttpClient.class))
          .baseUrl(UPSTREAM_URL)
          .build();

        log.info("Received notification, fetching data...");

        /*
        ctx.result(api.fetchSinceAsFuture(notification.url(), "the-beginning-of-time")
          .thenApply(ThrowingFunction.wrap(r -> r.body().string(), RuntimeException::new)));
        */

        api.fetchSince(notification.url(), "the-beginning-of-time").enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            try {
              log.info("Fetched data: " + response.body().string());
            } catch (IOException e) {
              log.error("Cannot read response body", e);
            }
          }

          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
          }
        });

        ctx.status(204);
      })
      .post("/do-something-to-subscribe", (ctx) -> {
        final ReplicationApi api = new ReplicationApi.Builder()
          .okHttpClient(ctx.appAttribute(OkHttpClient.class))
          .baseUrl(UPSTREAM_URL)
          .build();

        String selfUrl = ctx.scheme() + "://" + ctx.host() + "/replication/notification";
        Subscription s = new Subscription()
          .notifyUrl(selfUrl)
          .topic(ctx.queryParam("topic", "foobar"));

        Response<Subscription> response = api.subscribe(s).execute();

        ctx.json(response.body());
        ctx.header("X-Subscription-Url", response.headers().get("Location"));
        ctx.status(200);
      });
  }
}
