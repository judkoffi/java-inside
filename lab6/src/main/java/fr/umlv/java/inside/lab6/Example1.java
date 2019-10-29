package fr.umlv.java.inside.lab6;

/* Si on appelle deux fois run sans scope, il y a une IllegalStateException */
public class Example1 {
	public static void main(String[] args) {
		var scope = new ContinuationScope("hello1");
		var continuation = new Continuation(scope, () ->
			{
				Continuation.yield(scope); // Redonne la main au thread du main
				System.out.println("hello contiuation");
			});

		continuation.run();
		continuation.run(); // Va la igne 8 et execute la suite du programme
	}
}
