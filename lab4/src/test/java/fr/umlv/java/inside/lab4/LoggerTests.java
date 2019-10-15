package fr.umlv.java.inside.lab4;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class LoggerTests {

	private static class A {
		private static final StringBuilder stringBuilder = new StringBuilder();
		private static final Logger LOGGER = Logger.of(A.class, (msg) -> {
			stringBuilder.setLength(0);
			stringBuilder.append(msg);
		});
	}

	@Test
	public void testLogger() {
		A.LOGGER.log("hello");
		assertEquals("hello", LoggerTests.A.stringBuilder.toString());
	}

	@Test
	public void ofNull() {
		assertAll(() -> assertThrows(NullPointerException.class, () -> Logger.of(A.class, null).log("")),
				() -> assertThrows(NullPointerException.class, () -> Logger.of(null, __ -> {
				}).log("")));
	}

	@Test
	public void logNull() {
		assertThrows(NullPointerException.class, () -> A.LOGGER.log(null));
	}

}
