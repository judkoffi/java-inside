package fr.umlv.java.inside.lab6;

/* Si on appelle deux fois run sans scope, il y a une IllegalStateException */
public class Example1 {
	public static void main(String[] args) {
		var scope = new ContinuationScope("hello1");
		var continuation = new Continuation(scope, () ->
			{
				System.out.println(Continuation.getCurrentContinuation(scope));
				Continuation.yield(scope); // Redonne la main au thread du main
				System.out.println("hello contiuation");
			});

		continuation.run();
		continuation.run(); // Va la igne 8 et execute la suite du programme

		System.out.println(Continuation.getCurrentContinuation(scope));
	}
}

/*
 * 8. Continuation.getCurrentContinuation(scope) renvoie le scope du runnable
 * dans la continuation et null dans le main. Il peut y avoir des continuations
 * dans des continuations, ça permet de connaitre le scope de la contination
 * courante
 * 
 * 
 * 9. Un block synchronized permet de prendre un lock pour eviter d'être déschéduler durant le bloc synchonized
 */
