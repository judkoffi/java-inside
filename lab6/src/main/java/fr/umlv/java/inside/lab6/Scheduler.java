package fr.umlv.java.inside.lab6;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;

public class Scheduler {

	public enum SCHEDULERMODE {
		STACK, FIFO, RANDOM
	}

	private final ArrayDeque<Continuation> queue;
	private final SCHEDULERMODE mode;

	public Scheduler(SCHEDULERMODE mode) {
		this.queue = new ArrayDeque<Continuation>();
		this.mode = mode;
	}

	public void enqueue(ContinuationScope scope) {
		var continuation = Continuation.getCurrentContinuation(scope);
		if (continuation == null) {
			throw new IllegalStateException();
		}
		queue.offer(continuation);
		Continuation.yield(scope);
	}

	private void stackRunLoop() {
		while (!queue.isEmpty()) {
			var continuation = queue.pollLast();
			continuation.run();
		}
	}

	private void fifoRunLoop() {
		while (!queue.isEmpty()) {
			var continuation = queue.pollFirst();
			continuation.run();
		}
	}

	private void randomkRunLoop() {
		while (!queue.isEmpty()) {
			var shuffleQueue = queue.clone();
			Collections.shuffle(List.of(shuffleQueue));
			var continuation = shuffleQueue.getFirst();
			continuation.run();
		}
	}

	public void runLoop() {
		switch (mode) {
			case STACK :
				stackRunLoop();
				break;
			case FIFO :
				fifoRunLoop();
				break;
			case RANDOM :
				randomkRunLoop();
				break;
		}
	}
}
