package fr.umlv.java.inside.lab2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.Test;

public class JSONTest {

	public static class Person {
		private final String firstName;
		private final String lastName;

		public Person(String firstName, String lastName) {
			this.firstName = Objects.requireNonNull(firstName);
			this.lastName = Objects.requireNonNull(lastName);
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}
	}

	public static class Alien {
		private final String planet;
		private final int age;

		public Alien(String planet, int age) {
			if (age <= 0) {
				throw new IllegalArgumentException("Too young...");
			}
			this.planet = Objects.requireNonNull(planet);
			this.age = age;
		}

		public String getPlanet() {
			return planet;
		}

		public int getAge() {
			return age;
		}
	}

	public static class A {
		private final String field;

		public A(String field) {
			this.field = Objects.requireNonNull(field);
		}

		private String getField() {
			return this.field;
		}
	}

	@Test
	public void testGoodParsing() {
		var person = new Person("John", "Doe");
		var result = "{\"firstName\":\"John\",\"lastName\":\"Doe\"}";
		assertEquals(result, Main.toJSON(person));
	}

	/*@Test
	public void testExceptionParsing() {
		assertThrows(IllegalStateException.class, () -> Main.toJSON(new A("")));
	}*/

}
