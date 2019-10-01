package fr.umlv.java.inside.lab2;

import java.util.Objects;

public class Alien {
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
