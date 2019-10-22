package fr.umlv.java.inside;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class StringSwitchExampleTests {

    @Test
    public void testSwitchExampe() {
	assertAll(() -> assertEquals(0, StringSwitchExample.stringSwitch("foo")),
		() -> assertEquals(1, StringSwitchExample.stringSwitch("bar")),
		() -> assertEquals(2, StringSwitchExample.stringSwitch("bazz")),
		() -> assertEquals(-1, StringSwitchExample.stringSwitch("toto")),
		() -> assertThrows(NullPointerException.class, () -> StringSwitchExample.stringSwitch(null)));
    }

    @ParameterizedTest
    @MethodSource("stringSwithFunction")
    public void testSwitchExampleParameterized(ToIntFunction<String> function) {
	assertAll(() -> assertEquals(0, function.applyAsInt("foo")), () -> assertEquals(1, function.applyAsInt("bar")),
		() -> assertEquals(2, function.applyAsInt("bazz")),
		() -> assertEquals(-1, function.applyAsInt("toto")));
    }

    private static Stream<ToIntFunction<String>> stringSwithFunction() {
	return Stream.of(StringSwitchExample::stringSwitch, StringSwitchExample::stringSwitch2);
    }

}
