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
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@code TimeoutPropertyDecorator}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class TimeoutPropertyDecorator implements Timeout {

	/**
	 * The PROPERTY _ tIMEOUT.
	 */
	public static final String PROPERTY_TIMEOUT = "timeout";
	/**
	 * The PROPERTY _ rUNNING.
	 */
	public static final String PROPERTY_RUNNING = "running";
	/**
	 * The PROPERTY _ eLAPSED.
	 */
	public static final String PROPERTY_ELAPSED = "elapsed";

	protected final Timeout delegate;
	protected PropertyChangeSupport propertyChangeSupport;
	protected AtomicBoolean lastExpirationState;

	public TimeoutPropertyDecorator(Timeout delegate) {
		if (null == delegate) {
			throw new NullPointerException();
		}
		this.delegate = delegate;
		this.propertyChangeSupport = new PropertyChangeSupport(this);
		this.lastExpirationState = new AtomicBoolean(delegate.isElapsed());
	}

	/**
	 * Registers property change listener that will monitor all property changes.
	 *
	 * @param lsnr property change listener
	 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener lsnr) {
		propertyChangeSupport.addPropertyChangeListener(lsnr);
	}

	/**
	 * Registers property change listener for a specific property
	 *
	 * @param propertyName property to be monitored
	 * @param lsnr property change listener
	 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(String, java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener lsnr) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, lsnr);
	}

	/**
	 * Deregisters a property change listener.
	 *
	 * @param lsnr property change listener
	 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener lsnr) {
		propertyChangeSupport.removePropertyChangeListener(lsnr);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener lsnr) {
		propertyChangeSupport.removePropertyChangeListener(propertyName, lsnr);
	}

	@Override
	public long getTimeout() {
		final long result = delegate.getTimeout();
		checkExpirationChange();
		return result;
	}

	@Override
	public Timeout setTimeout(long millis) {
		final long origTimeout = delegate.getTimeout();
		delegate.setTimeout(millis);
		propertyChangeSupport.firePropertyChange(PROPERTY_TIMEOUT, origTimeout, millis);
		checkExpirationChange();
		return this;
	}

	@Override
	public Timeout start() {
		final boolean origRunning = delegate.isRunning();
		delegate.start();
		propertyChangeSupport.firePropertyChange(PROPERTY_RUNNING, origRunning, true);
		checkExpirationChange();
		return this;
	}

	@Override
	public Timeout startIfNotRunning() {
		final boolean origRunning = delegate.isRunning();
		delegate.startIfNotRunning();
		propertyChangeSupport.firePropertyChange(PROPERTY_RUNNING, origRunning, true);
		checkExpirationChange();
		return this;
	}

	@Override
	public Timeout stop() {
		final boolean origRunning = delegate.isRunning();
		delegate.stop();
		propertyChangeSupport.firePropertyChange(PROPERTY_RUNNING, origRunning, false);
		checkExpirationChange();
		return this;
	}

	@Override
	public Timeout restart() {
		final boolean origRunning = delegate.isRunning();
		delegate.restart();
		propertyChangeSupport.firePropertyChange(PROPERTY_RUNNING, origRunning, true);
		checkExpirationChange();
		return this;
	}

	@Override
	public boolean isRunning() {
		final boolean result = delegate.isRunning();
		checkExpirationChange();
		return result;
	}

	@Override
	public boolean isElapsed() {
		final boolean result = delegate.isElapsed();
		checkExpirationChange();
		return result;
	}

	@Override
	public boolean restartIfElapsed() {
		final boolean origRunning = delegate.isRunning();
		final boolean restarted = delegate.restartIfElapsed();
		propertyChangeSupport.firePropertyChange(PROPERTY_RUNNING, origRunning, delegate.isRunning());
		checkExpirationChange();
		return restarted;
	}

	@Override
	public int getRemainingPercent() {
		final int result = delegate.getRemainingPercent();
		checkExpirationChange();
		return result;
	}

	@Override
	public Long getRemainingMillis() {
		final Long result = delegate.getRemainingMillis();
		checkExpirationChange();
		return result;
	}

	@Override
	public boolean isExpired() {
		final boolean result = delegate.isExpired();
		checkExpirationChange();
		return result;
	}

	@Override
	public void expireNow() {
		delegate.expireNow();
		checkExpirationChange();
	}

	public void checkExpirationChange() {
		final boolean currentState = delegate.isElapsed();
		final boolean changeOccurred = lastExpirationState.compareAndSet(!currentState, currentState);
		if (changeOccurred) {
			propertyChangeSupport.firePropertyChange(PROPERTY_ELAPSED, !currentState, currentState);
		}
	}

}
