package sparkles;

import sparkles.downstream.Downstream;
import sparkles.upstream.Upstream;

public class ReplicationDemo {

  public static void main(String[] args) {
    new Upstream().init().port(7000).start();
    new Downstream().init().port(7001).start();
  }

}
