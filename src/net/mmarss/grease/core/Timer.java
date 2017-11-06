package net.mmarss.grease.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import net.mmarss.grease.exception.GreaseInvalidArgumentException;

public class Timer {
	
	/** The time in seconds to count down for, or 0 for clock mode. */
	private double	countdown;
	/** The time at which the timer was started. */
	private double	time;
	/** Whether the timer is paused. */
	private boolean	isPaused;
	
	/**
	 * Creates a timer in clock mode. This mode tracks time elapsed.
	 */
	public Timer() {
		
		this(0);
	}
	
	/**
	 * For positive arguments, creates a timer in countdown mode. A zero argument
	 * starts the timer in clock mode. Negative arguments produce errors.
	 * 
	 * @param time
	 *            The time that this timer should run for.
	 */
	public Timer(double time) {
		
		if (time < 0)
			throw new GreaseInvalidArgumentException("time", time, "Timer time must be non-negative.");
		
		this.time = 0;
		countdown = time;
		isPaused = false;
	}
	
	/**
	 * Starts or restarts the timer. Returns this for chaining.
	 * 
	 * @return This timer.
	 */
	public synchronized Timer start() {
		
		time = now();
		isPaused = false;
		return this;
	}
	
	/**
	 * Resumes the timer. If not paused, does nothing.
	 */
	public synchronized void resume() {
		
		if (!isPaused) {
			return;
		}
		
		if (countdown != 0) {
			time = now() - (countdown - time); // countdown - time = {time elapsed}
		} else {
			time = now() - time; // time = {time elapsed}
		}
	}
	
	/**
	 * Restarts the timer.
	 * 
	 * @return The time in seconds that the timer ran for.
	 */
	public synchronized double restart() {
		
		double time = get();
		start();
		return time;
	}
	
	/**
	 * Stops the timer. Synonymous with {@link #pause()}.
	 * 
	 * @return In clock mode: the time elapsed. In countdown mode: the time
	 *         remaining.
	 */
	public synchronized double stop() {
		
		time = get();
		isPaused = true;
		return time;
	}
	
	/**
	 * Pauses the timer. Synonymous with {@link #stop()}.
	 * 
	 * @return In clock mode: the time elapsed. In countdown mode: the time
	 *         remaining.
	 */
	public synchronized double pause() {
		
		return stop();
	}
	
	/**
	 * Gets the current value of the timer. In countdown mode, this is the time
	 * remaining. In clock mode, this is the elapsed time. If stopped, this is the
	 * time at which the timer was stopped.
	 * 
	 * @return The timer's current value, in seconds.
	 */
	public double get() {
		
		if (isPaused) {
			return time;
		}
		
		if (countdown != 0) { // Countdown mode
			double elapsed = now() - time;
			
			if (elapsed > countdown) {
				// Timer has triggered
				return 0;
			}
			
			return countdown - elapsed; // Time remaining
		}
		
		// Clock mode
		return now() - time; // Running time since start
	}
	
	/**
	 * Checks if the timer is running.
	 * 
	 * @return true if the timer is running, false otherwise.
	 */
	public boolean isRunning() {
		
		return !isPaused;
	}
	
	/**
	 * Checks if the countdown timer has been triggered. If the timer is in clock
	 * mode, this returns false.
	 * 
	 * @return true if the timer has been triggered, false otherwise.
	 */
	public boolean isTriggered() {
		
		if (countdown != 0) { // Countdown mode
			return now() - time > countdown; // elapsed > countdown
		} else { // Clock mode
			return false;
		}
	}
	
	/**
	 * Sleeps until the countdown timer elapses. In clock mode, returns immediately.
	 */
	public void sleep() {
		
		if (countdown != 0) {
			while (get() != 0) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
	/**
	 * Sleeps until the countdown timer elapses, then restarts. In clock mode, does
	 * nothing.
	 */
	public void sleepAndRestart() {
		
		if (countdown != 0) {
			sleep();
			restart();
		}
	}
	
	/**
	 * Returns the value of the engine timer. This timer measures the time elapsed
	 * since GLFW was initialized.
	 * 
	 * @return The current timer value, in seconds, or zero if an error occurred.
	 */
	public static double now() {
		
		return glfwGetTime();
	}
}
