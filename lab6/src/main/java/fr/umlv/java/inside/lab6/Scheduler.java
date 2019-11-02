package fr.umlv.java.inside.lab6;

import java.util.AbstractCollection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Scheduler {

  public enum SCHEDULINGMODE {
    STACK, FIFO, RANDOM
  }

  private final AbstractCollection<Continuation> collection;
  private final SCHEDULINGMODE mode;


  public Scheduler(SCHEDULINGMODE mode) {
    Objects.requireNonNull(mode);
    this.collection = (mode == SCHEDULINGMODE.RANDOM) ? new ArrayList<Continuation>()
        : new ArrayDeque<Continuation>();
    this.mode = mode;
  }

  public void enqueue(ContinuationScope scope) {
    var continuation = Continuation.getCurrentContinuation(scope);
    if (continuation == null) {
      throw new IllegalStateException();
    }
    collection.add(continuation);
    Continuation.yield(scope);
  }

  private void stackRunLoop() {
    while (!collection.isEmpty()) {
      var continuation = ((ArrayDeque<Continuation>) collection).pollLast();
      continuation.run();
    }
  }

  private void fifoRunLoop() {
    while (!collection.isEmpty()) {
      var continuation = ((ArrayDeque<Continuation>) collection).pollFirst();
      continuation.run();
    }
  }

  private void randomRunLoop() {
    while (!collection.isEmpty()) {
      var randomIndex = ThreadLocalRandom.current().nextInt(0, collection.size());
      var continuation = ((ArrayList<Continuation>) collection).get(randomIndex);

      if (!continuation.isDone())
        continuation.run();
    }
  }

  public void runLoop() {
    switch (mode) {
      case STACK:
        stackRunLoop();
        break;
      case FIFO:
        fifoRunLoop();
        break;
      case RANDOM:
        randomRunLoop();
        break;
    }
  }
}
