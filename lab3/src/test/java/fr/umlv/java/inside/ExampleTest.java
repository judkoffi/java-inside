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
}
