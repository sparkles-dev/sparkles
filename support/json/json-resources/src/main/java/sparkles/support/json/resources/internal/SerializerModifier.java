package sparkles.support.json.resources.internal;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import sparkles.support.json.resources.Resource;

/**
 * Modifier ensuring that beans annotated with {@link Resource} is handled by the {@link Serializer}.
 */
public class SerializerModifier extends BeanSerializerModifier {

  @Override
  public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
    Resource ann = beanDesc.getClassAnnotations().get(Resource.class);
    if (ann != null) {
      return new Serializer((BeanSerializer) serializer, beanDesc);
    }
    return serializer;
  }
}
