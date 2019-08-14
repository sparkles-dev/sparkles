package sparkles;

import sparkles.downstream.Downstream;
import sparkles.upstream.Upstream;

public class ReplicationDemo {

  public static void main(String[] args) {
    new Upstream().init().start(7000);
    new Downstream().init().start(7001);
  }

}
