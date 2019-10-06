package fr.umlv.java.inside.lab2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Objects;

import org.junit.jupiter.api.Test;

public class JSONBuilderTest {

	private static class Person {
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

	private static class Alien {
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

	private static class B {
		public B() {
		}
	}

	@Test
	public void testPersonParsing() {
		var person = new Person("John", "Doe");
		var expected = "{\"firstname\":\"John\",\"lastname\":\"Doe\"}";
		assertEquals(expected, JSONBuilder.toJSON(person));
	}

	@Test
	public void testAlienParsing() {
		var alien = new Alien("Neptune", 50);
		var expected = "{\"age\":\"50\",\"planet\":\"Neptune\"}";
		assertEquals(expected, JSONBuilder.toJSON(alien));
	}

	@Test
	public void testClassWithoutGetterParsing() {
		var b = new B();
		assertEquals("{}", JSONBuilder.toJSON(b));
	}

	@Test
	public void testIllegealArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> new Alien("GreenEarth", 0));
	}
	
	@Test
	public void testNPE() {
		assertThrows(NullPointerException.class, () -> JSONBuilder.toJSON(null));
	}
}
