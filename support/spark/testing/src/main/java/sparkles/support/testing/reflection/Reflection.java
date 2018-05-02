package sparkles.support.testing.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static java.lang.reflect.Modifier.isStatic;
import static java.util.Collections.addAll;

public final class Reflection {

  public static List<Field> findAllFields(Class<?> type) {
		List<Field> fields = new LinkedList<>();
		if (type != null) {
			addAll(fields, type.getDeclaredFields());

			if (type.getSuperclass() != null) {
				fields.addAll(findAllFields(type.getSuperclass()));
			}
		}

		return fields;
	}

  public static List<Field> findAllFieldsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotationType) {
    return findAllFields(type).stream()
      .filter(field -> Arrays.asList(field.getAnnotations()).stream()
        .filter(annotation -> annotation.annotationType().equals(annotationType))
        .findAny()
        .isPresent())
      .collect(Collectors.toList());
  }

  public static List<Field> findAllStaticFieldsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotationType) {
    return findAllFieldsAnnotatedWith(type, annotationType).stream()
      .filter(field -> isStatic(field.getModifiers()))
      .collect(Collectors.toList());
  }

  public static List<Method> findAllMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotationType) {
    return Arrays.asList(type.getDeclaredMethods()).stream()
      .filter(method -> method.isAnnotationPresent(annotationType))
      .collect(Collectors.toList());
  }

  public static List<Method> findAllStaticMethodsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotationType) {
    return Arrays.asList(type.getDeclaredMethods()).stream()
      .filter(method -> method.isAnnotationPresent(annotationType))
      .filter(method -> isStatic(method.getModifiers()))
      .collect(Collectors.toList());    
  }

	@SuppressWarnings("unchecked")
	public static <T> T getter(Object target, Field field) {
		boolean forceAccess = false;

		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
				forceAccess = true;
			}

			return (T) field.get(target);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (forceAccess) {
				field.setAccessible(false);
			}
		}
	}

  public static void setter(Object instance, Field field, Object value) {
		boolean forceAccess = false;

		try {
			if (!field.isAccessible()) {
				forceAccess = true;
				field.setAccessible(true);
			}

			field.set(instance, value);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (forceAccess) {
				field.setAccessible(false);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T invoke(Method method) {
		boolean forceAccess = false;

		try {
			if (!method.isAccessible()) {
				method.setAccessible(true);
				forceAccess = true;
			}

			return (T) method.invoke(null);
		} catch (InvocationTargetException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (forceAccess) {
				method.setAccessible(false);
			}
		}
	}

  private Reflection() {}

}
