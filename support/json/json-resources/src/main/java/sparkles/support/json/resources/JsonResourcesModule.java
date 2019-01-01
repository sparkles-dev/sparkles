package sparkles.support.json.resources;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JsonResourcesModule extends SimpleModule {

  public JsonResourcesModule() {
    super(JsonResourcesModule.class.getSimpleName(), Version.unknownVersion());
  }

  // TODO:
  // - inspire from https://github.com/openapi-tools/jackson-dataformat-hal
  // - add serializer + modifier
  // - add deserializer + modifier
  // - change serialization and deserialization...
  //    - recognize custom @Embedded annotation
  //    - for @Links annotation and Link class

}
