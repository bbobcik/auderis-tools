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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SwitchingTimeout extends AbstractBeanBasedTimeout {

	private static final long serialVersionUID = 2751030086140217459L;

	private final List<TimeoutRecord> timeouts;
	private final TimeoutRecord finalTimeout;
	private int currentIndex;

	public SwitchingTimeout() {
		super();
		timeouts = new ArrayList<TimeoutRecord>();
		finalTimeout = new TimeoutRecord(Timeouts.infinite(), 1, false);
		currentIndex = -1;
	}

	public SwitchingTimeout append(Timeout timeout, int repeatCount, boolean removeAfterLastCycle) {
		if (null == timeout) {
			throw new NullPointerException();
		} else if (timeout.isRunning()) {
			throw new IllegalArgumentException("provided timeout is running");
		} else if (repeatCount < 1) {
			throw new IllegalArgumentException("repeat count must be positive");
		} else if (isRunning()) {
			throw new IllegalStateException("timeout is already running");
		}
		final TimeoutRecord record = new TimeoutRecord(timeout, repeatCount, removeAfterLastCycle);
		timeouts.add(record);
		return this;
	}

	@Override
	public long getTimeout() {
		final TimeoutRecord record = getCurrentRecord();
		return record.getInstance().getTimeout();
	}

	@Override
	public SwitchingTimeout setTimeout(long millis) {
		throw new UnsupportedOperationException("cannot change switching timeout");
	}

	@Override
	public SwitchingTimeout start() {
		if (isRunning()) {
			throw new IllegalStateException("timeout is already running");
		}
		synchronized (timeouts) {
			currentIndex = 0;
			final TimeoutRecord record = getCurrentRecord();
			record.getInstance().start().resetExpirationCount();
		}
		return this;
	}

	@Override
	public SwitchingTimeout startIfNotRunning() {
		if (timeouts.isEmpty()) {
			finalTimeout.getInstance().startIfNotRunning();
			return this;
		}
		synchronized (timeouts) {
			if (isRunning()) {
				// Do nothing
			} else if (currentIndex < 0) {
				currentIndex = 0;
				final TimeoutRecord record = getCurrentRecord();
				record.getInstance().restart();
			} else {
				TimeoutRecord record = timeouts.get(currentIndex);
				if (record.isSwitchNeeded()) {
					if (record.isRemovableAfterFullCycle()) {
						timeouts.remove(currentIndex);
					} else {
						record.getInstance().stop();
						++currentIndex;
					}
					fixCurrentIndex();
					record = getCurrentRecord();
					record.getInstance().resetExpirationCount();
				}
				record.getInstance().restart();
			}
		}
		return this;
	}

	@Override
	public SwitchingTimeout stop() {
		if (timeouts.isEmpty()) {
			return this;
		}
		synchronized (timeouts) {
			if (isRunning()) {
				final TimeoutRecord record = getCurrentRecord();
				record.getInstance().stop();
			}
		}
		return this;
	}

	private void fixCurrentIndex() {
		final int size = timeouts.size();
		if (0 == size) {
			currentIndex = -1;
		} else if (currentIndex >= size) {
			currentIndex = currentIndex % size;
		}
	}

	private TimeoutRecord getCurrentRecord() {
		if (timeouts.isEmpty()) {
			return finalTimeout;
		} else if (currentIndex < 0) {
			return timeouts.get(0);
		}
		return timeouts.get(currentIndex);
	}

	@Override
	public SwitchingTimeout restart() {
		if (timeouts.isEmpty()) {
			finalTimeout.getInstance().restart();
			return this;
		}
		synchronized (timeouts) {
			if (currentIndex < 0) {
				currentIndex = 0;
				final TimeoutRecord record = timeouts.get(currentIndex);
				record.getInstance().startIfNotRunning();
			} else {
				TimeoutRecord record = timeouts.get(currentIndex);
				if (record.isSwitchNeeded()) {
					record.getInstance().stop();
					if (record.isRemovableAfterFullCycle()) {
						timeouts.remove(currentIndex);
					} else {
						++currentIndex;
					}
					fixCurrentIndex();
					record = getCurrentRecord();
					record.getInstance().resetExpirationCount();
				}
				record.getInstance().restart();
			}
		}
		return this;
	}

	@Override
	public boolean restartIfElapsed() {
		boolean result = false;
		if (timeouts.isEmpty()) {
			result = finalTimeout.getInstance().restartIfElapsed();
		} else if (!isRunning() || isElapsed()) {
			restart();
			result = true;
		}
		return result;
	}

	@Override
	public void expireNow() {
		if (isRunning()) {
			getCurrentRecord().getInstance().expireNow();
		}
	}

	@Override
	public boolean isRunning() {
		return getCurrentRecord().getInstance().isRunning();
	}

	@Override
	public boolean isElapsed() {
		return getCurrentRecord().getInstance().isElapsed();
	}

	@Override
	public boolean isExpired() {
		return getCurrentRecord().getInstance().isExpired();
	}

	@Override
	public int getRemainingPercent() {
		if (!isRunning()) {
			return -1;
		}
		return getCurrentRecord().getInstance().getRemainingPercent();
	}

	@Override
	public Long getRemainingMillis() {
		if (!isRunning()) {
			return -1L;
		}
		return getCurrentRecord().getInstance().getRemainingMillis();
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder("SwitchingTimeout[");
		String separator = null;
		for (int i = 0; i < timeouts.size(); ++i) {
			if (null == separator) {
				separator = ", ";
			} else {
				str.append(separator);
			}
			if (currentIndex == i) {
				str.append('*');
			}
			final TimeoutRecord record = timeouts.get(i);
			str.append(record.getInstance());
			str.append(" x").append(record.getMaxRepeatCount());
		}
		str.append("]");
		return str.toString();
	}

	static class TimeoutRecord implements Serializable {

		private static final long serialVersionUID = -8941865969050966469L;

		private final Timeout timeout;
		private final CountingTimeoutImpl countingDecorator;
		private int repeatCount;
		private boolean removeAfterLastExpiration;

		public TimeoutRecord(Timeout timeout, int repeats, boolean removable) {
			this.timeout = timeout;
			this.countingDecorator = new CountingTimeoutImpl(timeout);
			this.repeatCount = repeats;
			this.removeAfterLastExpiration = removable;
		}

		public CountingTimeoutImpl getInstance() {
			return countingDecorator;
		}

		public Timeout getOriginalInstance() {
			return timeout;
		}

		public int getMaxRepeatCount() {
			return repeatCount;
		}

		public boolean isRemovableAfterFullCycle() {
			return removeAfterLastExpiration;
		}

		public boolean isSwitchNeeded() {
			if (!countingDecorator.isRunning()) {
				return false;
			}
			final int currentCount = countingDecorator.getExpirationCount();
			return currentCount >= repeatCount;
		}
	}

	final class PropertyChangeForwarder implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			final String propertyName = evt.getPropertyName();
			final Object oldValue = evt.getOldValue();
			final Object newValue = evt.getOldValue();
			final PropertyChangeEvent forwardedEvent = new PropertyChangeEvent(SwitchingTimeout.this, propertyName, oldValue, newValue);
			forwardedEvent.setPropagationId(evt.getPropagationId());
			firePropertyChange(forwardedEvent);
		}
	}

}
