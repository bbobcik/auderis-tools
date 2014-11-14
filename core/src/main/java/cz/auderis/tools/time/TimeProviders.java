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

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Utility class that serves as a factory for commonly used
 * {@link cz.auderis.tools.time.TimeProvider}s.
 */
public final class TimeProviders {

	/**
	 * Obtains a {@link cz.auderis.tools.time.TimeProvider} that is based
	 * on system clock.
	 *
	 * @return real-time clock time provider
	 * @see System#nanoTime()
	 */
	public static TimeProvider systemClock() {
		return ProviderImpl.SYSTEM;
	}

	/**
	 * Returns a new instance of {@link cz.auderis.tools.time.ManualTimeProvider},
	 * that is initialized to the current system time.
	 *
	 * @return new instance of manual time provider
	 */
	public static ManualTimeProvider manual() {
		return new ManualTimeProvider(systemClock().getTimeInMillis());
	}

	/**
	 * Returns a new instance of {@link cz.auderis.tools.time.ManualTimeProvider},
	 * that is initialized to the provided date/time.
	 *
	 * @param startTime initial value for the time provider
	 * @return new instance of manual time provider
	 * @throws java.lang.NullPointerException if {@code startTime} is {@code null}
	 */
	public static ManualTimeProvider manual(Date startTime) {
		if (null == startTime) {
			throw new NullPointerException();
		}
		return new ManualTimeProvider(startTime.getTime());
	}

	private TimeProviders() {
		throw new AssertionError();
	}

	/**
	 * This enum provides safe singleton instances of {@code TimeProvider}
	 */
	protected static enum ProviderImpl implements TimeProvider {
		/**
		 * {@code TimeProvider} based on {@link System#nanoTime()} real-time
		 * clock.
		 */
		SYSTEM {
			@Override
			public long getTimeInMillis() {
				return TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
			}
		}
	}

}
