package fr.umlv.java.inside.lab6;

import java.util.ArrayDeque;
import java.util.List;

/* Si on appelle deux fois run sans scope, il y a une IllegalStateException */

/*
 * 8. Continuation.getCurrentContinuation(scope) renvoie le scope du runnable
 * dans la continuation et null dans le main. Il peut y avoir des continuations
 * dans des continuations, ça permet de connaitre le scope de la contination
 * courante
 * 
 * 
 * 9. Un block synchronized permet de prendre un lock pour eviter d'être
 * déschéduler durant le bloc synchonized Si on met un lock, il y a une
 * IllegalStateException
 * 
 * 10. 
 * - Communs:
 *  	-> une seule execution du runnable
 * - Differences:
 * 	 	-> 1 thread peut exécutte plusieurs continuations pour un seul Thread
 * 		-> Plus petit qu'in Thread et utilse un Thread pour s'éxécuter
 * 		-> Pour un Thread, on gère pas le scheduling alors que pour une continuation oui
 * 		-> Pour les contination, on parle de concurrence coorperative et compétitif pour les threads
 */
public class Example1 {
	private final static Object lock = new Object();
	public static void main(String[] args) {
		/*
		 * var scope = new ContinuationScope("hello1");
		 * 
		 * var continuation = new Continuation(scope, () -> { synchronized
		 * (lock) { Continuation.yield(scope); // Redonne la main au thread du }
		 * // main System.out.println("hello contiuation"); });
		 * 
		 * continuation.run(); continuation.run(); // Va la igne 8 et execute la
		 * suite du programme
		 * 
		 * System.out.println(Continuation.getCurrentContinuation(scope));
		 */

		var scope = new ContinuationScope("scope");
		var contiuation1 = new Continuation(scope, () ->
			{
				System.out.println("start 1");
				Continuation.yield(scope);
				System.out.println("middle 1");
				Continuation.yield(scope);
				System.out.println("end 1");
			});
		var contiuation2 = new Continuation(scope, () ->
			{
				System.out.println("start 2");
				Continuation.yield(scope);
				System.out.println("middle 2");
				Continuation.yield(scope);
				System.out.println("end 2");
			});

		var contiuation3 = new Continuation(scope, () ->
			{
				System.out.println("start 3");
				Continuation.yield(scope);
				System.out.println("middle 3");
				Continuation.yield(scope);
				System.out.println("end 3");
			});

		var contiuation4 = new Continuation(scope, () ->
			{
				System.out.println("start 4");
				Continuation.yield(scope);
				System.out.println("middle 4");
				Continuation.yield(scope);
				System.out.println("end 4");
			});

		var list = List.of(contiuation1, contiuation2, contiuation3,
				contiuation4);

		var dequeue = new ArrayDeque<Continuation>(list);

		while (!dequeue.isEmpty()) {
			var continuation = dequeue.poll();
			continuation.run();
			if (!continuation.isDone()) {
				dequeue.offer(continuation);
			}
		}
	}
}
