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

		@JSONProperty("firstname")
		public String getFirstName() {
			return firstName;
		}

		@JSONProperty("lastname")
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

		@JSONProperty("planet")
		public String getPlanet() {
			return planet;
		}

		@JSONProperty("age")
		public int getAge() {
			return age;
		}
	}

	public static class B {
		public B() {
		}
	}

	@Test
	public void testGoodParsing() {
		var person = new Person("John", "Doe");
		var result = "{\"firstname\":\"John\",\"lastname\":\"Doe\"}";
		assertEquals(result, JSONBuilder.toJSON(person));
	}

	@Test
	public void testClassWithoutGetterParsing() {
		var b = new B();
		assertEquals("{}", JSONBuilder.toJSON(b));
	}

}
