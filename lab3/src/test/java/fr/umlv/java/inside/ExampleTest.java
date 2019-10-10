package fr.umlv.java.inside;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import fr.umlv.java.inside.Example;

public class ExampleTest {

	@Test
	public void invokeMethodaStaticHello() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		var method = Example.class.getDeclaredMethod("aStaticHello", int.class);
		method.setAccessible(true);

		var value = method.invoke(null, 1);
		assertEquals("question 1", value);
	}

	@Test
	public void invokeMethodanInstanceHello() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		var method = Example.class.getDeclaredMethod("anInstanceHello", int.class);
		method.setAccessible(true);

		var value = method.invoke(new Example(), 2);
		assertEquals("question 2", value);
	}

	@Test
	public void lookUpaStaticHello() throws Throwable {

		var lookup = MethodHandles.lookup();

		var privateLookup = MethodHandles.privateLookupIn(Example.class, lookup);

		var handle = privateLookup.findStatic(Example.class, "aStaticHello",
				MethodType.methodType(String.class, int.class));

		assertEquals("question 3", (String) handle.invokeExact(3));
	}

	@Test
	public void lookUpaInstancecHello() throws Throwable {

		var lookup = MethodHandles.lookup();

		var privateLookup = MethodHandles.privateLookupIn(Example.class, lookup);
		var virtualMethod = privateLookup.findVirtual(Example.class, "anInstanceHello",
				MethodType.methodType(String.class, int.class));

		assertEquals("question 4", (String) virtualMethod.invokeExact(new Example(), 4));
	}

	@Test
	public void lookUpaInstancecHelloInsertArgs() throws Throwable {

		var lookup = MethodHandles.lookup();

		var privateLookup = MethodHandles.privateLookupIn(Example.class, lookup);
		var virtualMethod = privateLookup.findVirtual(Example.class, "anInstanceHello",
				MethodType.methodType(String.class, int.class));

		var insertedArgs = MethodHandles.insertArguments(virtualMethod, 1, 8);

		assertEquals("question 8", (String) insertedArgs.invokeExact(new Example()));
	}

	@Test
	public void lookUpaInstancecHelloDropArgs() throws Throwable {

		var lookup = MethodHandles.lookup();

		var privateLookup = MethodHandles.privateLookupIn(Example.class, lookup);
		var virtualMethod = privateLookup.findVirtual(Example.class, "anInstanceHello",
				MethodType.methodType(String.class, int.class));

		var dropArgument = MethodHandles.dropArguments(virtualMethod, 1, int.class);

		assertEquals("question 5", (String) dropArgument.invokeExact(new Example(), 9, 5));
	}

	@Test
	public void lookUpaInstancecHelloUnboxing() throws Throwable {

		var lookup = MethodHandles.lookup();

		var privateLookup = MethodHandles.privateLookupIn(Example.class, lookup);
		var virtualMethod = privateLookup
				.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class))
				.asType(MethodType.methodType(String.class, Example.class, Integer.class));

		assertEquals("question 15", (String) virtualMethod.invokeExact(new Example(), Integer.valueOf(15)));
	}

	@Test
	public void methodHandleConstant() throws Throwable {
		var constantHandle = MethodHandles.constant(int.class, 45);
		assertEquals(45, (int) constantHandle.invokeExact());
	}

	@Test
	public void methodHandleGuardWithTest() throws Throwable {
		var lookup = MethodHandles.lookup();
		var testHandle = lookup.findVirtual(String.class, "equals", MethodType.methodType(boolean.class, Object.class));

		var succesCallback = MethodHandles.constant(int.class, 1);
		var targetHandle = MethodHandles.dropArguments(succesCallback, 0, String.class, Object.class);

		var failCallback = MethodHandles.constant(int.class, -1);
		var fallbackHandle = MethodHandles.dropArguments(failCallback, 0, String.class, Object.class);

		var guardHandle = MethodHandles.guardWithTest(testHandle, targetHandle, fallbackHandle);

		var method = MethodHandles.insertArguments(guardHandle, 1, "foo");

		assertEquals(1, (int) method.invoke("foo"));
		assertEquals(-1, (int) method.invoke("hello"));
	}
}
