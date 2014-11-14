/*
 * Copyright 2014 Boleslav Bobcik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.auderis.tools.time;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of {@link cz.auderis.tools.time.TimeProvider} that provides time
 * that is constant; the time is changed exclusively by invoking appropriate methods.
 *
 * <p>This implementation is useful for testing, where non-real-time scenarios
 * need to be checked.
 *
 * <p>Notice that the millisecond value may be negative - the class makes no checks
 * in this regard.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class ManualTimeProvider implements TimeProvider, Serializable {
	private static final long serialVersionUID = 1L;
	protected final AtomicLong time;

	/**
	 * Instantiates a new manual time provider that is initialized
	 * to the current system time.
	 */
	public ManualTimeProvider() {
		this(TimeProviders.systemClock().getTimeInMillis());
	}

	/**
	 * Instantiates a new manual time provider that is initialized
	 * to the provided start time specified as milliseconds.
	 *
	 * @param startTime the start time given in milliseconds
	 */
	public ManualTimeProvider(long startTime) {
		time = new AtomicLong(startTime);
	}

	@Override
	public long getTimeInMillis() {
		return time.get();
	}

	/**
	 * Changes the time of this {@code TimeProvider} to the new value given in milliseconds.
	 *
	 * @param millis new time given in milliseconds
	 * @return the current instance
	 */
	public ManualTimeProvider setTimeInMillis(long millis) {
		time.set(millis);
		return this;
	}

	/**
	 * Changes the time of this {@code TimeProvider} by the specified increment
	 * specified in milliseconds.
	 *
	 * <p>The increment may be negative.
	 *
	 * @param incrementMillis the increment given in milliseconds
	 * @return the current instance
	 */
	public ManualTimeProvider step(long incrementMillis) {
		time.addAndGet(incrementMillis);
		return this;
	}

	/**
	 * Changes the time of this {@code TimeProvider} by the specified increment.
	 *
	 * <p>The increment may be negative.
	 *
	 * @param increment the increment
	 * @param unit time unit of the increment
	 * @return the current instance
	 * @throws java.lang.NullPointerException if {@code unit} is {@code null}
	 */
	public ManualTimeProvider step(long increment, TimeUnit unit) {
		if (null == unit) {
			throw new NullPointerException();
		}
		time.addAndGet(unit.toMillis(increment));
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder("TimeProvider[");
		str.append("t=").append(time.get());
		str.append(", mode=manual]");
		return str.toString();
	}

}
