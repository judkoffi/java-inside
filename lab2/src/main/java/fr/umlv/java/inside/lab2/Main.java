package fr.umlv.java.inside.lab2;

import java.util.Arrays;

public class Main {

	private static String propertyName(String name) {
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}

	public static String toJSON(Object object) {
		var methods = object.getClass().getMethods();

		var stream = Arrays.stream(methods);
		
		stream.forEach((method) -> System.out.println(propertyName(method.getName())));

		return "";
	}

	public static void main(String[] args) {
		var person = new Person("John", "Doe");
		toJSON(person);
	}

}
