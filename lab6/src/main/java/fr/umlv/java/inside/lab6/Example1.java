package fr.umlv.java.inside.lab6;

public class Example1 {
	public static void main(String[] args) {

		var continuation = new Continuation(new ContinuationScope("hello"),
				() -> System.out.println("hello contiuation"));
	}
}
