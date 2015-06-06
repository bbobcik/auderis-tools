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

import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

class CountingTimeoutImpl implements CountingTimeout, Serializable {

	private static final long serialVersionUID = 5698692496260987879L;

	private final Timeout baseTimeout;
	private AtomicInteger expirationCount;
	private int expirationIncrement;

	public CountingTimeoutImpl(Timeout timeout) {
		if (null == timeout) {
			throw new NullPointerException();
		} else if (timeout.isRunning()) {
			throw new IllegalArgumentException("timeout is already running");
		}
		this.baseTimeout = timeout;
		this.expirationCount = new AtomicInteger();
		this.expirationIncrement = 0;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener lsnr) {
		if (null == lsnr) {
			throw new NullPointerException();
		} else if (baseTimeout instanceof BeanBasedTimeout) {
			((BeanBasedTimeout) baseTimeout).addPropertyChangeListener(lsnr);
		}
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener lsnr) {
		if (null == lsnr) {
			throw new NullPointerException();
		} else if (baseTimeout instanceof BeanBasedTimeout) {
			((BeanBasedTimeout) baseTimeout).addPropertyChangeListener(propertyName, lsnr);
		}
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener lsnr) {
		if (null == lsnr) {
			throw new NullPointerException();
		} else if (baseTimeout instanceof BeanBasedTimeout) {
			((BeanBasedTimeout) baseTimeout).removePropertyChangeListener(lsnr);
		}
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener lsnr) {
		if (null == lsnr) {
			throw new NullPointerException();
		} else if (baseTimeout instanceof BeanBasedTimeout) {
			((BeanBasedTimeout) baseTimeout).removePropertyChangeListener(propertyName, lsnr);
		}
	}

	@Override
	public CountingTimeoutImpl start() {
		synchronized (baseTimeout) {
			baseTimeout.start();
			expirationIncrement = 1;
			expirationCount.set(0);
		}
		return this;
	}

	@Override
	public CountingTimeoutImpl startIfNotRunning() {
		synchronized (baseTimeout) {
			if (!baseTimeout.isRunning()) {
				baseTimeout.start();
				expirationIncrement = 1;
				expirationCount.set(0);
			}
		}
		return this;
	}

	@Override
	public CountingTimeoutImpl stop() {
		synchronized (baseTimeout) {
			isElapsed();
			expirationIncrement = 0;
			baseTimeout.stop();
		}
		return this;
	}

	@Override
	public CountingTimeoutImpl restart() {
		synchronized (baseTimeout) {
			baseTimeout.restart();
			expirationIncrement = 1;
		}
		return this;
	}

	@Override
	public boolean restartIfElapsed() {
		synchronized (baseTimeout) {
			boolean result = true;
			if (!baseTimeout.isRunning()) {
				baseTimeout.start();
				expirationIncrement = 1;
				expirationCount.set(0);
			} else if (baseTimeout.restartIfElapsed()) {
				expirationCount.addAndGet(expirationIncrement);
				expirationIncrement = 1;
			} else {
				result = false;
			}
			return result;
		}
	}

	@Override
	public void expireNow() {
		baseTimeout.expireNow();
	}


	@Override
	public boolean isElapsed() {
		boolean elapsed = baseTimeout.isElapsed();
		if (elapsed) {
			synchronized (baseTimeout) {
				expirationCount.addAndGet(expirationIncrement);
				expirationIncrement = 0;
			}
		}
		return elapsed;
	}

	@Override
	public boolean isExpired() {
		return isRunning() ? isElapsed() : false;
	}

	@Override
	public long getTimeout() {
		return baseTimeout.getTimeout();
	}

	@Override
	public CountingTimeoutImpl setTimeout(long millis) {
		baseTimeout.setTimeout(millis);
		return this;
	}

	@Override
	public boolean isRunning() {
		return baseTimeout.isRunning();
	}

	@Override
	public int getRemainingPercent() {
		return baseTimeout.getRemainingPercent();
	}

	@Override
	public Long getRemainingMillis() {
		return baseTimeout.getRemainingMillis();
	}

	@Override
	public boolean isFirstRun() {
		synchronized (baseTimeout) {
			return 0 == expirationCount.get();
		}
	}

	@Override
	public int getExpirationCount() {
		synchronized (baseTimeout) {
			isElapsed();
			return expirationCount.get();
		}
	}

	@Override
	public CountingTimeoutImpl resetExpirationCount() {
		synchronized (baseTimeout) {
			expirationIncrement = 1;
			expirationCount.set(0);
		}
		return this;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder("CountingTimeout[");
		str.append(baseTimeout);
		str.append(", count=").append(expirationCount.get());
		str.append("]");
		return str.toString();
	}

}
