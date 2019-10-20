package fr.umlv.java.inside.lab4;

import static java.lang.invoke.MethodType.methodType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface Logger {
	public void log(String message);

	public static Logger of(Class<?> declaringClass, Consumer<? super String> consumer) {
		Objects.requireNonNull(declaringClass);
		Objects.requireNonNull(consumer);
		var mh = createLoggingMethodHandle(declaringClass, consumer);
		return new Logger() {
			@Override
			public void log(String message) {
				Objects.requireNonNull(message);
				try {
					mh.invokeExact(message);
				} catch (Throwable t) {
					if (t instanceof RuntimeException) {
						throw (RuntimeException) t;
					}
					if (t instanceof Error) {
						throw (Error) t;
					}
					throw new UndeclaredThrowableException(t);
				}
			}
		};
	}

	private static void lambdaBody(Class<?> declaringClass, String message, MethodHandle mh) {
		Objects.requireNonNull(message);
		try {
			mh.invokeExact(message);
		} catch (Throwable t) {
			if (t instanceof RuntimeException) {
				throw (RuntimeException) t;
			}
			if (t instanceof Error) {
				throw (Error) t;
			}
			throw new UndeclaredThrowableException(t);
		}
	}

	public static Logger fastOf(Class<?> declaringClass, Consumer<? super String> consumer) {
		var mh = createLoggingMethodHandle(declaringClass, consumer);
		return (message) -> lambdaBody(declaringClass, message, mh);
	}
	/*
	 * private static MethodHandle createLoggingMethodHandle(Class<?>
	 * declaringClass, Consumer<? super String> consumer) { MethodHandle mh; var
	 * lookup = MethodHandles.lookup(); try { mh =
	 * lookup.findVirtual(Consumer.class, "accept", methodType(void.class,
	 * Object.class)); } catch (NoSuchMethodException | IllegalAccessException e) {
	 * throw new AssertionError(e); } mh = mh.bindTo(consumer); // equivaut a
	 * insertArgument sur la pos 0 mh = mh.asType(methodType(void.class,
	 * String.class)); return mh; }
	 */

	private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, Consumer<? super String> consumer) {
		MethodHandle mh;
		MethodHandle mhTest;
		var lookup = MethodHandles.lookup();
		try {
			mhTest = ENABLE_CALLSITES.get(declaringClass).dynamicInvoker();
			mh = lookup.findVirtual(Consumer.class, "accept", methodType(void.class, Object.class));

		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
		mh = mh.bindTo(consumer);
		mh = mh.asType(methodType(void.class, String.class));
		return MethodHandles.guardWithTest(mhTest, mh, MethodHandles.empty(methodType(void.class, String.class)));
	}

	static final ClassValue<MutableCallSite> ENABLE_CALLSITES = new ClassValue<MutableCallSite>() {
		protected MutableCallSite computeValue(Class<?> type) {
			var mutableCallSite = new MutableCallSite(MethodHandles.constant(boolean.class, true));
			var array = new MutableCallSite[] { mutableCallSite };
			MutableCallSite.syncAll(array);
			return mutableCallSite;
		}
	};

	public static void enable(Class<?> declaringClass, boolean enable) {
		ENABLE_CALLSITES.get(declaringClass).setTarget(MethodHandles.constant(boolean.class, enable));
	}
}
