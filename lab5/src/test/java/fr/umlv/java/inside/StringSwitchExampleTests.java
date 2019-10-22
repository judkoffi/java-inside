package fr.umlv.java.inside;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class StringSwitchExampleTests {
	@Test
	public void testSwitchExampe() {
		assertAll(() -> assertEquals(0, StringSwitchExample.stringSwitch("foo")),
				() -> assertEquals(1, StringSwitchExample.stringSwitch("bar")),
				() -> assertEquals(2, StringSwitchExample.stringSwitch("bazz")),
				() -> assertEquals(-1, StringSwitchExample.stringSwitch("toto")),
				() -> assertThrows(NullPointerException.class, () -> StringSwitchExample.stringSwitch(null)));
	}
}
