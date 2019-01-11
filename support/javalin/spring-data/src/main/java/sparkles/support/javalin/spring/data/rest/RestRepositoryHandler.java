package sparkles.support.javalin.spring.data.rest;

import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.javalin.Context;
import io.javalin.Extension;
import io.javalin.Javalin;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import sparkles.support.common.collections.CollectionUtil;

import static sparkles.support.javalin.spring.data.SpringDataExtension.springData;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RestRepositoryHandler<Repo extends CrudRepository<Entity, ID>, Entity, ID> implements Extension {
  private final String path;
  private final Class<Repo> repoClz;
  private final Class<Entity> entityClz;

  private Repo repository(Context ctx) {
    return springData(ctx).repository(repoClz);
  }

  private String basePath () {
    String segments[] = path.split("/");
    if (segments.length < 2) {
      throw new IllegalStateException("Path parameter must contain at least one slash: " + path);
    }

    return Arrays.stream(segments, 0, segments.length - 1)
      .collect(Collectors.joining("/"));
  }

  @Override
  public void registerOnJavalin(Javalin app) {
    final String basePath = basePath();

    app
      .get(basePath, this::findAll)
      .post(basePath, this::create)
      .put(basePath, this::update)
      .get(path, this::findOne)
      .head(path, this::exists)
      .delete(path, this::delete);
  }

  protected abstract ID toId(Context ctx);
  protected abstract String toUrl(Entity entity);

  protected EntityResource<Entity> toResource(Entity entity) {
    final EntityResource<Entity> resource = new EntityResource<>();
    resource.entity = entity;
    resource.withSelfRel(toUrl(entity));

    return resource;
  }

  protected EntityCollectionResource<Entity> toResourceCollection(Context ctx, List<Entity> entities) {
    final EntityCollectionResource<Entity> resource = EntityCollectionResource.from(
      entities.stream().map(this::toResource).collect(Collectors.toList())
    );
    resource.withSelfRel(ctx.path());

    return resource;
  }

  public void findAll(Context ctx) {
    Iterable<Entity> iterable = repository(ctx).findAll();
    List<Entity> entities = CollectionUtil.toList(iterable);

    ctx.status(200);
    ctx.json(toResourceCollection(ctx, entities));
  }

  public void create(Context ctx) {
    Entity entity = ctx.bodyAsClass(entityClz);
    Entity result = repository(ctx).save(entity);

    ctx.status(201);
    ctx.header("Location", toUrl(result));
    ctx.json(toResource(result));
  }

  public void update(Context ctx) {
    Entity entity = ctx.bodyAsClass(entityClz);
    Entity result = repository(ctx).save(entity);

    ctx.status(200);
    ctx.json(toResource(result));
  }

  public void exists(Context ctx) {
    if (repository(ctx).existsById(toId(ctx))) {
      ctx.status(200);
    } else {
      ctx.status(404);
    }
  }

  public void findOne(Context ctx) {
    Optional<Entity> entity = repository(ctx).findById(toId(ctx));

    if (entity.isPresent()) {
      final EntityResource resource = new EntityResource();
      resource.entity = entity;

      ctx.status(200);
      ctx.json(resource);
    } else {
      ctx.status(404);
    }
  }

  public void delete(Context ctx) {
    repository(ctx).deleteById(toId(ctx));

    ctx.status(200);
  }

}
