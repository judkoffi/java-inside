package fr.umlv.java.inside.lab6;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class Example1Test {

	@Test
	public void test1() {
		var scope = new ContinuationScope("hello1");
		var builder = new StringBuilder();
		var continuation = new Continuation(scope, () ->
			{
				builder.append("hello contiuation");
			});
		continuation.run();

		assertEquals("hello contiuation", builder.toString());
	}

	@Test
	public void test2() {
		var scope = new ContinuationScope("hello1");
		var builder = new StringBuilder();
		var continuation = new Continuation(scope, () ->
			{
				Continuation.yield(scope);
				builder.append("hello contiuation");
			});
		continuation.run();

		assertEquals("", builder.toString());
	}

}
