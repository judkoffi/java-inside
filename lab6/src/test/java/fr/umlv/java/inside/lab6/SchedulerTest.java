package fr.umlv.java.inside.lab6;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import java.util.List;
import org.junit.jupiter.api.Test;
import fr.umlv.java.inside.lab6.Scheduler.SCHEDULINGMODE;

public class SchedulerTest {

  @Test
  public void testNullSchedulerMode() {
    assertThrows(NullPointerException.class, () -> new Scheduler(null));
  }

  @Test
  public void testFifoSchedulerMode() {
    var scheduler = new Scheduler(SCHEDULINGMODE.FIFO);
    var scope = new ContinuationScope("scope");

    var stringBuilder = new StringBuilder();

    var expectedStringBuilder = new StringBuilder();
    expectedStringBuilder.append("start 1\n").append("start 2\n");
    expectedStringBuilder.append("middle 1\n").append("middle 2\n");
    expectedStringBuilder.append("end 1\n").append("end 2\n");


    var continuation1 = new Continuation(scope, () ->
    {
      stringBuilder.append("start 1\n");
      scheduler.enqueue(scope);
      stringBuilder.append("middle 1\n");
      scheduler.enqueue(scope);
      stringBuilder.append("end 1\n");
    });

    var continuation2 = new Continuation(scope, () ->
    {
      stringBuilder.append("start 2\n");
      scheduler.enqueue(scope);
      stringBuilder.append("middle 2\n");
      scheduler.enqueue(scope);
      stringBuilder.append("end 2\n");
    });

    var continuations = List.of(continuation1, continuation2);
    continuations.forEach(Continuation::run);

    scheduler.runLoop();

    assertEquals(expectedStringBuilder.toString(), stringBuilder.toString());
  }


  @Test
  public void testStackSchedulerMode() {
    var scheduler = new Scheduler(SCHEDULINGMODE.STACK);
    var scope = new ContinuationScope("scope");

    var stringBuilder = new StringBuilder();

    var expectedStringBuilder = new StringBuilder();
    expectedStringBuilder.append("start 1\n").append("start 2\n");
    expectedStringBuilder.append("middle 2\n").append("end 2\n");
    expectedStringBuilder.append("middle 1\n").append("end 1\n");


    var continuation1 = new Continuation(scope, () ->
    {
      stringBuilder.append("start 1\n");
      scheduler.enqueue(scope);
      stringBuilder.append("middle 1\n");
      scheduler.enqueue(scope);
      stringBuilder.append("end 1\n");
    });

    var continuation2 = new Continuation(scope, () ->
    {
      stringBuilder.append("start 2\n");
      scheduler.enqueue(scope);
      stringBuilder.append("middle 2\n");
      scheduler.enqueue(scope);
      stringBuilder.append("end 2\n");
    });

    var continuations = List.of(continuation1, continuation2);
    continuations.forEach(Continuation::run);

    scheduler.runLoop();

    assertEquals(expectedStringBuilder.toString(), stringBuilder.toString());
  }


}
