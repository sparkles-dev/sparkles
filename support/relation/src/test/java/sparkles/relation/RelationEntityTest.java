package sparkles.relation;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RelationEntityTest {

  @Test
  public void itShouldCreate() {
    assertThat(new RelationEntity()).isNotNull();
  }
}
