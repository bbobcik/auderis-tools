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

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class TimeoutWithProviderImpl extends AbstractBeanBasedTimeout implements Serializable {

	private static final long serialVersionUID = 7678557975218056071L;

	private static final String ERR_TIMEOUT_NEGATIVE = "timeout must not be negative";
	private static final String ERR_TIMEOUT_NOT_RUNNING = "timeout not running";
	private static final long PREC_SCALE = 1024L;
	private static final AtomicInteger TIMEOUT_ID_SEQUENCE = new AtomicInteger(1);

	private final int id;
	private final TimeProvider clock;
	private final ReadWriteLock mutex;
	private volatile boolean running;
	private volatile long timeout;
	private volatile long startTime;

	TimeoutWithProviderImpl(long millis, TimeProvider clock) {
		super();
		if (millis < 0L) {
			throw new IllegalArgumentException(ERR_TIMEOUT_NEGATIVE);
		} else if (null == clock) {
			throw new NullPointerException();
		}
		this.id = TIMEOUT_ID_SEQUENCE.getAndIncrement();
		this.clock = clock;
		this.mutex = new ReentrantReadWriteLock();
		this.timeout = millis;
	}

	@Override
	public long getTimeout() {
		return timeout;
	}

	@Override
	public TimeoutWithProviderImpl setTimeout(long millis) {
		if (millis < 0L) {
			throw new IllegalArgumentException(ERR_TIMEOUT_NEGATIVE);
		} else if (running) {
			throw new IllegalStateException("cannot change running timeout");
		}
		mutex.writeLock().lock();
		long oldMillis = this.timeout;
		try {
			this.timeout = millis;
		} finally {
			mutex.writeLock().unlock();
		}
		firePropertyChange(PROPERTY_TIMEOUT, oldMillis, millis);
		return this;
	}

	public void updateTimeout(long millis) {
		if (millis < 0L) {
			throw new IllegalArgumentException(ERR_TIMEOUT_NEGATIVE);
		}
		mutex.writeLock().lock();
		long oldMillis = this.timeout;
		try {
			this.timeout = millis;
		} finally {
			mutex.writeLock().unlock();
		}
		firePropertyChange(PROPERTY_TIMEOUT, oldMillis, millis);
	}

	@Override
	public TimeoutWithProviderImpl start() {
		final long currentTime = clock.getTimeInMillis();
		if (running) {
			throw new IllegalStateException("timeout is already running");
		}
		mutex.writeLock().lock();
		try {
			if (!running) {
				running = true;
				startTime = currentTime;
			}
		} finally {
			mutex.writeLock().unlock();
		}
		firePropertyChange(PROPERTY_RUNNING, false, true);
		return this;
	}

	@Override
	public TimeoutWithProviderImpl startIfNotRunning() {
		final long currentTime = clock.getTimeInMillis();
		if (running) {
			return this;
		}
		mutex.writeLock().lock();
		try {
			if (!running) {
				running = true;
				startTime = currentTime;
			}
		} finally {
			mutex.writeLock().unlock();
		}
		firePropertyChange(PROPERTY_RUNNING, false, true);
		return this;
	}

	@Override
	public TimeoutWithProviderImpl stop() {
		if (running) {
			mutex.writeLock().lock();
			try {
				running = false;
			} finally {
				mutex.writeLock().unlock();
			}
			firePropertyChange(PROPERTY_RUNNING, true, false);
		}
		return this;
	}

	@Override
	public TimeoutWithProviderImpl restart() {
		final long currentTime = clock.getTimeInMillis();
		mutex.writeLock().lock();
		boolean oldRunning = running;
		try {
			running = true;
			startTime = currentTime;
		} finally {
			mutex.writeLock().unlock();
		}
		firePropertyChange(PROPERTY_RUNNING, oldRunning, true);
		return this;
	}

	@Override
	public boolean restartIfElapsed() {
		long currentTime = clock.getTimeInMillis();
		long expirationTime = startTime + timeout;
		if (running && (currentTime < expirationTime)) {
			// No need for restart
			return false;
		}
		mutex.writeLock().lock();
		boolean oldRunning = running;
		boolean restarted = false;
		try {
			currentTime = clock.getTimeInMillis();
			expirationTime = startTime + timeout;
			if (!running || (currentTime >= expirationTime)) {
				running = true;
				startTime = currentTime;
				restarted = true;
			}
		} finally {
			mutex.writeLock().unlock();
		}
		firePropertyChange(PROPERTY_RUNNING, oldRunning, true);
		return restarted;
	}

	@Override
	public void expireNow() {
		final long currentTime = clock.getTimeInMillis();
		mutex.writeLock().lock();
		try {
			if (running) {
				startTime = currentTime - timeout;
			} else {
				throw new IllegalStateException(ERR_TIMEOUT_NOT_RUNNING);
			}
		} finally {
			mutex.writeLock().unlock();
		}
		firePropertyChange(PROPERTY_ELAPSED, false, true);
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public boolean isElapsed() {
		if (!running) {
			return false;
		}
		final boolean mutexAcquired = mutex.readLock().tryLock();
		try {
			final long currentTime = clock.getTimeInMillis();
			return (currentTime >= (startTime + timeout));
		} finally {
			if (mutexAcquired) {
				mutex.readLock().unlock();
			}
		}
	}

	@Override
	public int getRemainingPercent() {
		if (!running) {
			throw new IllegalStateException(ERR_TIMEOUT_NOT_RUNNING);
		}
		final long currentTime = clock.getTimeInMillis();
		final long diff;
		final long tmout;
		final boolean mutexAcquired = mutex.readLock().tryLock();
		try {
			if (!running) {
				throw new IllegalStateException(ERR_TIMEOUT_NOT_RUNNING);
			}
			diff = startTime + timeout - currentTime;
			tmout = this.timeout;
		} finally {
			if (mutexAcquired) {
				mutex.readLock().unlock();
			}
		}
		if (diff >= tmout) {
			return MAX_PERCENT;
		} else if (diff <= 0L) {
			return 0;
		}
		final long percent = ((100 * PREC_SCALE * diff) / tmout + (PREC_SCALE / 2)) / PREC_SCALE;
		return (int) percent;
	}

	@Override
	public Long getRemainingMillis() {
		final long currentTime = clock.getTimeInMillis();
		if (!running) {
			return null;
		}
		final boolean mutexAcquired = mutex.readLock().tryLock();
		try {
			if (!running) {
				return null;
			}
			final long remaining = startTime + timeout - currentTime;
			return (remaining > 0L) ? remaining : 0L;
		} finally {
			if (mutexAcquired) {
				mutex.readLock().unlock();
			}
		}
	}

	@Override
	public boolean isExpired() {
		return isRunning() ? isElapsed() : false;
	}

	@Override
	public int hashCode() {
		return 101 ^ Objects.hash(clock) ^ id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if ((null == obj) || !(obj instanceof TimeoutWithProviderImpl)) {
			return false;
		}
		final TimeoutWithProviderImpl other = (TimeoutWithProviderImpl) obj;
		if (!Objects.equals(this.clock, other.clock)) {
			return false;
		} else if (this.running != other.running) {
			return false;
		} else if (this.timeout != other.timeout) {
			return false;
		} else if (this.startTime != other.startTime) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Timeout[");
		mutex.readLock().lock();
		try {
//			if (running) {
//				str.append("running since ").append(new LocalDateTime(startTime));
//				LocalDateTime expireDate = new LocalDateTime(startTime + timeout);
//				if (hasExpired()) {
//					str.append(", expired on ").append(expireDate);
//				} else {
//					str.append(", will expire on ").append(expireDate);
//				}
//			} else {
//				str.append("idle");
//			}
//			str.append(", timeout=").append(Duration.millis(timeout));
		} finally {
			mutex.readLock().unlock();
		}
		str.append("]");
		return str.toString();
	}

}
