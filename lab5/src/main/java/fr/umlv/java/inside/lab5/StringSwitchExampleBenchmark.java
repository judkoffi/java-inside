package fr.umlv.java.inside.lab5;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class StringSwitchExampleBenchmark {

	private static final int ITERATION_NUMBER = 1000000;
	private static ArrayList<String> list;

	static {
		String[] strings = { "foo", "tata", "bazz", "toto", "bar", "titi" };
		list = new ArrayList<String>(ITERATION_NUMBER);

		var r = new Random();

		for (int i = 0; i < ITERATION_NUMBER; i++) {
			var randomValue = r.nextInt(strings.length);
			list.add(strings[randomValue]);
		}
	}

	@Benchmark
	public void stringSwitch1() {
		list.forEach((l) -> StringSwitchExample.stringSwitch(l));
	}

	@Benchmark
	public void stringSwitch2() {
		list.forEach((l) -> StringSwitchExample.stringSwitch2(l));
	}

	@Benchmark
	public void stringSwitch3() {
		list.forEach((l) -> StringSwitchExample.stringSwitch3(l));
	}
}
