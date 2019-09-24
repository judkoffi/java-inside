package fr.umlv.java.inside.lab1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SwitchExampleTest {

	@Test
	void testDog() {
		assertEquals(1, SwitchExample.switchExample("dog"));
	}

	@Test
	void testCat() {
		assertEquals(2, SwitchExample.switchExample("cat"));
	}

	@Test
	void testDefault() {
		assertEquals(4, SwitchExample.switchExample("toto"));
	}

	@Test
	void testNull() {
		assertThrows(NullPointerException.class, () -> SwitchExample.switchExample(null));
	}
}
