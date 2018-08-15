package com.overload.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A simple timing utility used to throttle or time actions. This class has been
 * altered to use <code>NANOSECONDS</code> for extremely accurate timing.
 *
 * @author lare96
 */
public class Stopwatch1 {

	public static long currentTime() {
		return TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	private long time = Stopwatch1.currentTime();

	private long startTime = Stopwatch1.currentTime();

	public long elapsed() {
		return Stopwatch1.currentTime() - time;
	}

	public long elapsed(TimeUnit unit) {
		if (unit == TimeUnit.MILLISECONDS) {
			throw new IllegalArgumentException("Time is already in milliseconds!");
		}
		return unit.convert(elapsed(), TimeUnit.MILLISECONDS);
	}

	public boolean elapsed(long time) {
		return elapsed() >= time;
	}

	public Stopwatch1 reset() {
		startTime = time = Stopwatch1.currentTime();
		return this;
	}

	public Stopwatch1 reset(long start) {
		time = Stopwatch1.currentTime() + start;
		return this;
	}

	public long getStartTime() {
		return startTime;
	}

	@Override
	public String toString() {
		return String.format("STOPWATCH[time=%s, elapsed=%s]", time, elapsed());
	}

	public boolean hasElapsed(long time, TimeUnit unit) {
		return elapsed() >= TimeUnit.MILLISECONDS.convert(time, unit);
	}

	private enum State {
		RUNNING,
		STOPPED
	}

	public static final class AtomicStopwatch {

		private final AtomicLong cachedTime = new AtomicLong(Stopwatch1.currentTime());

		private final AtomicReference<State> state = new AtomicReference<>(State.STOPPED);

		@Override
		public String toString() {
			boolean stopped = (state.get() == State.STOPPED);
			return "STOPWATCH[elasped= " + (stopped ? 0 : elapsedTime()) + "]";
		}

		public static long currentTime() {
			return TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
		}

		public AtomicStopwatch reset() {
			cachedTime.set(Stopwatch1.currentTime());
			state.set(State.RUNNING);
			return this;
		}

		public AtomicStopwatch stop() {
			state.set(State.STOPPED);
			return this;
		}

		public long elapsedTime(TimeUnit unit) {
			if (state.get() == State.STOPPED)
				throw new IllegalStateException("The timer has been stopped!");
			return unit.convert((Stopwatch1.currentTime() - cachedTime.get()), TimeUnit.MILLISECONDS);
		}

		public long elapsedTime() {
			return elapsedTime(TimeUnit.MILLISECONDS);
		}

		public boolean elapsed(long time, TimeUnit unit) {
			if (state.get() == State.STOPPED) {
				return true;
			}
			return elapsedTime(unit) >= time;
		}

		public boolean elapsed(long time) {
			return elapsed(time, TimeUnit.MILLISECONDS);
		}

		public boolean isStopped() {
			return state.get() == State.STOPPED;
		}

	}
}