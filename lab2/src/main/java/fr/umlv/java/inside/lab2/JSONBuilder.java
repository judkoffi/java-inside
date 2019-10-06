package fr.umlv.java.inside.lab2;

import static java.util.stream.Collectors.joining;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

import fr.umlv.java.inside.lab2.JSONTest.Alien;

public class JSONBuilder {

	private final static ClassValue<Method[]> cachedMethods = new ClassValue<Method[]>() {

		@Override
		protected Method[] computeValue(Class<?> type) {
			return type.getMethods();
		}
	};

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

	public static boolean isValidString(String str) {
		return ((!str.equals("")) && (str != null) && (str.matches("^[a-zA-Z]*$")));
	}

	public static String toJSON(Object object) {

		//var methods = object.getClass().getMethods();

		var methods = cachedMethods.get(object.getClass());
		
		/*
		 * return Arrays.stream(methods).filter((method) ->
		 * method.getName().startsWith("get")) .filter((method) ->
		 * method.getDeclaringClass() != Object.class)
		 * .sorted(Comparator.comparing(Method::getName)) .map((method) ->
		 * buildJSONLine(object, method)) .collect(joining(",", "{", "}"));
		 */

		return Arrays.stream(methods).filter((method) -> method.isAnnotationPresent(JSONProperty.class))
				.map((annotedMethod) -> buildJSONLine(object, annotedMethod)).collect(joining(",", "{", "}"));

	}

	public static void main(String[] args) {
		var person = new Person("John", "Doe");
		System.out.println(toJSON(person));
		var alien = new Alien("Neptune", 50);
		System.out.println(toJSON(alien));
	}

}
