package fr.umlv.java.inside;

import static java.lang.invoke.MethodHandles.constant;
import static java.lang.invoke.MethodHandles.dropArguments;
import static java.lang.invoke.MethodHandles.guardWithTest;
import static java.lang.invoke.MethodHandles.insertArguments;
import static java.lang.invoke.MethodType.methodType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;

public class StringSwitchExample {
    private static final MethodHandle STRING_EQUALS;
    static {
	var lookup = MethodHandles.lookup();

	try {
	    STRING_EQUALS = lookup.findVirtual(String.class, "equals", methodType(boolean.class, Object.class));
	} catch (NoSuchMethodException | IllegalAccessException e) {
	    throw new AssertionError(e);
	}
    };

    @SuppressWarnings("preview")
    public static int stringSwitch(String str) {
	Objects.requireNonNull(str);
	return switch (str) {
	case "foo" -> 0;
	case "bar" -> 1;
	case "bazz" -> 2;
	default -> -1;
	};

    }

    public static int stringSwitch2(String str) {
	Objects.requireNonNull(str);
	try {
	    var mh = createMHFromStrings2("foo", "bar", "bazz");
	    return (int) mh.invokeExact(str);
	} catch (RuntimeException | Error e) {
	    throw e;
	} catch (Throwable t) {
	    throw new UndeclaredThrowableException(t);
	}
    }

    private static MethodHandle createMHFromStrings2(String... strings) throws Throwable {
	var mh = dropArguments(constant(int.class, -1), 0, String.class);

	for (int i = 0; i < strings.length; i++) {
	    mh = guardWithTest(insertArguments(STRING_EQUALS, 1, strings[i]),
		    dropArguments(constant(int.class, i), 0, String.class), mh);
	}

	return mh;
    }
}
