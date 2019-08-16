package sparkles.support.javalin.springdata.crud;

import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.function.Function;

import io.javalin.apibuilder.CrudHandler;
import sparkles.support.javalin.springdata.SpringData;

public abstract class CrudRepositoryHandler<R extends CrudRepository<T, ID>, T, ID> implements CrudHandler {
  private static final Logger log = LoggerFactory.getLogger(CrudRepositoryHandler.class);

  private final Class<R> repoClz;
  private final Class<T> entityClz;

  public CrudRepositoryHandler(Class<R> repoClz, Class<T> entityClz) {
    this.repoClz = repoClz;
    this.entityClz = entityClz;
  }

  private R repository(Context ctx) {
    return ctx.use(SpringData.class).repository(repoClz);
  }

  public abstract ID parseId(String id);

  @Override
  public void getAll(Context ctx) {
    Iterable<T> entities = repository(ctx).findAll();

    ctx.json(entities);
  }

  @Override
  public void getOne(Context ctx, String id) {
    Optional<T> entity = repository(ctx).findById(parseId(id));

    if (entity.isPresent()) {
      ctx.status(200);
      ctx.json(entity.get());
    } else {
      ctx.status(404);
    }
  }

  @Override
  public void create(Context ctx) {
    log.info("create(): ");

    T body = ctx.bodyAsClass(entityClz);
    T entity = repository(ctx).save(body);

    ctx.json(entity);
  }

  @Override
  public void update(Context ctx, String id) {
    T body = ctx.bodyAsClass(entityClz);
    T entity = repository(ctx).save(body);

    ctx.json(entity);
  }

  @Override
  public void delete(Context ctx, String id) {
    repository(ctx).deleteById(parseId(id));
  }

  public static <R extends CrudRepository<T, ID>, T, ID> CrudHandler crudHandler(
    Class<R> repoClz,
    Class<T> entityClz,
    Function<String, ID> toId
  ) {
    return new SimpleHandler<>(repoClz, entityClz, toId);
  }

  private static class SimpleHandler<R extends CrudRepository<T, ID>, T, ID> extends CrudRepositoryHandler<R, T, ID> {

    private final Function<String, ID> toId;
    public SimpleHandler(Class<R> repoClz, Class<T> entityClz, Function<String, ID> toId) {
      super(repoClz, entityClz);
      this.toId = toId;
    }

    @Override
    public ID parseId(String id) {
      return toId.apply(id);
    }
  }
}
