package fr.umlv.java.inside.lab2;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

public class JSONBuilder {

	private static String propertyName(String name) {
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}

	private static Object invokeGetter(Object object, Method method) {

		try {
			return method.invoke(object);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			var cause = e.getCause();
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			}
			if (cause instanceof Error) {
				throw (Error) cause;
			}
			throw new UndeclaredThrowableException(cause);
		}
	}

	private static String buildJSONLine(Object object, Method annotedMethod) {
		var valueOfAnnotation = annotedMethod.getAnnotation(JSONProperty.class).value();
		var propertyName = isValidString(valueOfAnnotation) ? valueOfAnnotation : propertyName(annotedMethod.getName());
		var value = invokeGetter(object, annotedMethod);
		return "\"" + propertyName + "\"" + ":" + "\"" + value + "\"";
	}

	private static boolean isValidString(String str) {
		return ((!str.equals("")) && (str != null) && (str.matches("^[a-zA-Z]*$")));
	}

	/* Cached methods */
	/*
	 * La méthode getMethods() est lent car elle fait une copie de chacune des
	 * méthodes et ne peut pas être accelerer car a cause de la méthode
	 * setAccesible(), la classe Method est une classe mutatble.
	 */
	private final static ClassValue<Method[]> cachedMethods = new ClassValue<Method[]>() {
		@Override
		protected Method[] computeValue(Class<?> type) {
			return type.getMethods();
		}
	};

	private final static ClassValue<Function<Object, String>> cache = new ClassValue<Function<Object, String>>() {
		@Override
		protected Function<Object, String> computeValue(Class<?> type) {
			var methods = Arrays.stream(type.getMethods())
					.filter((method) -> method.isAnnotationPresent(JSONProperty.class))
					.sorted(Comparator.comparing(Method::getName)).collect(toList());

			return (sourceObject) -> methods.stream().map((annotedMethod) -> buildJSONLine(sourceObject, annotedMethod))
					.collect(joining(",", "{", "}"));
		}
	};

	public static String toJSON(Object object) {
		var checkedObject = Objects.requireNonNull(object);

		// var methods = cachedMethods.get(checkedObject.getClass());

		/*
		 * return Arrays.stream(methods).filter((method) ->
		 * method.getName().startsWith("get")) .filter((method) ->
		 * method.getDeclaringClass() != Object.class)
		 * .sorted(Comparator.comparing(Method::getName)) .map((method) ->
		 * buildJSONLine(object, method)) .collect(joining(",", "{", "}"));
		 */

		/*
		 * return Arrays.stream(methods).filter((method) ->
		 * method.isAnnotationPresent(JSONProperty.class))
		 * .sorted(Comparator.comparing(Method::getName)) .map((annotedMethod) ->
		 * buildJSONLine(object, annotedMethod)).collect(joining(",", "{", "}"));
		 */

		return cache.get(checkedObject.getClass()).apply(checkedObject);

	}
}
