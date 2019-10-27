package fr.umlv.java.inside.lab5;

import static java.lang.invoke.MethodHandles.constant;
import static java.lang.invoke.MethodHandles.dropArguments;
import static java.lang.invoke.MethodHandles.guardWithTest;
import static java.lang.invoke.MethodHandles.insertArguments;
import static java.lang.invoke.MethodType.methodType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
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

	public static int stringSwitch(String str) {
		Objects.requireNonNull(str);
		switch (str) {
		case "foo":
			return 0;
		case "bar":
			return 1;
		case "bazz":
			return 2;
		default:
			return -1;
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

	public static MethodHandle createMHFromStrings3(String... matches) {
		return new InliningCache(matches).dynamicInvoker();
	}

	/* InlineCache => genere une suite d'instruction */
	static class InliningCache extends MutableCallSite {
		private static final MethodHandle SLOW_PATH;
		static {
			var lookup = MethodHandles.lookup();
			try {
				SLOW_PATH = lookup.findVirtual(InliningCache.class, "slowPath", methodType(int.class, String.class));
			} catch (NoSuchMethodException | IllegalAccessException e) {
				throw new AssertionError(e);
			}
		}

		private final List<String> matches;

		public InliningCache(String... matches) {
			super(methodType(int.class, String.class));
			this.matches = List.of(matches);
			setTarget(insertArguments(SLOW_PATH, 0, this));
		}

		private int slowPath(String value) {
			var index = matches.indexOf(value);

			var mh = guardWithTest(insertArguments(STRING_EQUALS, 1, value),
					dropArguments(constant(int.class, index), 0, String.class), getTarget());

			setTarget(mh);
			return index;
		}
	}

	public static int stringSwitch3(String str) {
		Objects.requireNonNull(str);
		try {
			var mh = createMHFromStrings3("foo", "bar", "bazz");
			return (int) mh.invokeExact(str);
		} catch (RuntimeException | Error e) {
			throw e;
		} catch (Throwable t) {
			throw new UndeclaredThrowableException(t);
		}
	}

}
