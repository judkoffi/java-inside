package fr.umlv.java.inside.lab2;

import static java.util.stream.Collectors.joining;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

public class Main {

	private static String propertyName(String name) {
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}

	private static String buildJSONLine(Object object, Method method) {
		var property = propertyName(method.getName());
		try {
			var value = method.invoke(object);
			return "\"" + property + "\"" + ": " + "\"" + value + "\"";
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

	public static String toJSON(Object object) {

		var methods = object.getClass().getMethods();

		return Arrays.stream(methods).filter((method) -> method.getName().startsWith("get"))
				.map((method) -> buildJSONLine(object, method)).collect(joining(",", " {", "}"));
	}

	public static void main(String[] args) {
		var person = new Person("John", "Doe");
		System.out.println(toJSON(person));
	}

}
