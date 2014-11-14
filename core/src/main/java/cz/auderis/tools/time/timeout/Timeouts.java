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

package cz.auderis.tools.time.timeout;

import cz.auderis.tools.time.TimeProvider;
import cz.auderis.tools.time.TimeProviders;

import java.util.concurrent.TimeUnit;

public final class Timeouts {

	private static final String INVALID_TIMEOUT = "invalid timeout duration";

	public static Timeout simple(long duration, TimeUnit durationUnit) {
		if (null == durationUnit) {
			throw new NullPointerException();
		} else if (duration <= 0L) {
			throw new IllegalArgumentException(INVALID_TIMEOUT);
		}
		final long millis = durationUnit.toMillis(duration);
		final Timeout t = new TimeoutWithProviderImpl(millis, TimeProviders.systemClock());
		return t;
	}

	public static Timeout simpleWithProvider(long duration, TimeUnit durationUnit, TimeProvider provider) {
		if (null == durationUnit) {
			throw new NullPointerException();
		} else if (null == provider) {
			throw new NullPointerException();
		} else if (duration <= 0L) {
			throw new IllegalArgumentException(INVALID_TIMEOUT);
		}
		final long millis = durationUnit.toMillis(duration);
		final Timeout t = new TimeoutWithProviderImpl(millis, provider);
		return t;
	}

	public static CountingTimeout counting(long duration, TimeUnit durationUnit) {
		if (null == durationUnit) {
			throw new NullPointerException();
		} else if (duration <= 0L) {
			throw new IllegalArgumentException(INVALID_TIMEOUT);
		}
		final long millis = durationUnit.toMillis(duration);
		final Timeout t1 = new TimeoutWithProviderImpl(millis, TimeProviders.systemClock());
		final CountingTimeout t2 = new CountingTimeoutImpl(t1);
		return t2;
	}

	public static CountingTimeout counting(Timeout decoratedTimeout) {
		if (null == decoratedTimeout) {
			throw new NullPointerException();
		}
		final CountingTimeout t = new CountingTimeoutImpl(decoratedTimeout);
		return t;
	}

	public static Timeout infinite() {
		final Timeout t = new InfiniteTimeoutImpl();
		return t;
	}

	public static Timeout alwaysExpired() {
		return AlwaysExpiredTimeoutImpl.getInstance();
	}

	private Timeouts() {
		throw new AssertionError();
	}

}
