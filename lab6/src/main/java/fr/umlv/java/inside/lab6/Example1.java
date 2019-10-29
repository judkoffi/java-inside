package fr.umlv.java.inside.lab6;

public class Example1 {
	public static void main(String[] args) {
		var scope = new ContinuationScope("hello1");
		var continuation = new Continuation(scope, () ->
			{
				Continuation.yield(scope);
				System.out.println("hello contiuation");
			});

		continuation.run();
	}
}
