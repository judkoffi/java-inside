package fr.umlv.java.inside.lab6;

import java.util.List;
import fr.umlv.java.inside.lab6.Scheduler.SCHEDULINGMODE;

public class SchedulerExample {

  public static void main(String[] args) {
    var scheduler = new Scheduler(SCHEDULINGMODE.RANDOM);
    var scope = new ContinuationScope("scope");

    var continuation1 = new Continuation(scope, () ->
    {
      System.out.println("start 1");
      scheduler.enqueue(scope);
      System.out.println("middle 1");
      scheduler.enqueue(scope);
      System.out.println("end 1");
    });

    var continuation2 = new Continuation(scope, () ->
    {
      System.out.println("start 2");
      scheduler.enqueue(scope);
      System.out.println("middle 2");
      scheduler.enqueue(scope);
      System.out.println("end 2");
    });

    var continuation3 = new Continuation(scope, () ->
    {
      System.out.println("start 3");
      scheduler.enqueue(scope);
      System.out.println("middle 3");
      scheduler.enqueue(scope);
      System.out.println("end 3");
    });

    var continuation4 = new Continuation(scope, () ->
    {
      System.out.println("start 4");
      scheduler.enqueue(scope);
      System.out.println("middle 4");
      scheduler.enqueue(scope);
      System.out.println("end 4");
    });

    var list = List.of(continuation1, continuation2, continuation3, continuation4);
    list.forEach(Continuation::run);
    scheduler.runLoop();
  }

}
