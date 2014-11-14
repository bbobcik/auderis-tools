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

final class InfiniteTimeoutImpl extends AbstractBeanBasedTimeout  {

	private volatile boolean running;
	private volatile boolean forcedExpiration;

	InfiniteTimeoutImpl() {
		super();
	}

	@Override
	public boolean isExpired() {
		return isElapsed();
	}

	@Override
	public void expireNow() {
		if (!running) {
			return;
		}
		boolean oldExpiration = forcedExpiration;
		synchronized (this) {
			if (running) {
				forcedExpiration = true;
			}
		}
		firePropertyChange(PROPERTY_ELAPSED, oldExpiration, forcedExpiration);
	}

	@Override
	public long getTimeout() {
		return Long.MAX_VALUE;
	}

	@Override
	public InfiniteTimeoutImpl setTimeout(long millis) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InfiniteTimeoutImpl start() {
		if (running) {
			return this;
		}
		boolean oldRunning = running;
		synchronized (this) {
			if (!running) {
				running = true;
				forcedExpiration = false;
			}
		}
		firePropertyChange(PROPERTY_RUNNING, oldRunning, running);
		return this;
	}

	@Override
	public InfiniteTimeoutImpl startIfNotRunning() {
		return start();
	}

	@Override
	public InfiniteTimeoutImpl stop() {
		if (!running) {
			return this;
		}
		boolean oldRunning = running;
		synchronized (this) {
			if (running) {
				running = false;
			}
		}
		firePropertyChange(PROPERTY_RUNNING, oldRunning, running);
		return this;
	}

	@Override
	public InfiniteTimeoutImpl restart() {
		boolean oldRunning = running;
		synchronized (this) {
			running = true;
			forcedExpiration = false;
		}
		firePropertyChange(PROPERTY_RUNNING, oldRunning, running);
		return this;
	}

	@Override
	public boolean restartIfElapsed() {
		if (!forcedExpiration && running) {
			return false;
		}
		boolean oldRunning = running;
		synchronized (this) {
			if (forcedExpiration || !running) {
				running = true;
				forcedExpiration = false;
			}
		}
		firePropertyChange(PROPERTY_RUNNING, oldRunning, running);
		return true;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public boolean isElapsed() {
		return forcedExpiration;
	}

	@Override
	public int getRemainingPercent() {
		return forcedExpiration ? 0 : MAX_PERCENT;
	}

	@Override
	public Long getRemainingMillis() {
		return forcedExpiration ? 0L : Long.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "InfiniteTimeout";
	}

}
