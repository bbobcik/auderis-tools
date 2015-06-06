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
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * {@code AbstractBeanBasedTimeout}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public abstract class AbstractBeanBasedTimeout implements BeanBasedTimeout, Serializable {

	private PropertyChangeSupport beanSupport;

	@Override
	public void addPropertyChangeListener(PropertyChangeListener lsnr) {
		if (null == lsnr) {
			throw new NullPointerException();
		} else if (null == beanSupport) {
			beanSupport = new PropertyChangeSupport(this);
		}
		beanSupport.addPropertyChangeListener(lsnr);
	}

	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener lsnr) {
		if (null == lsnr) {
			throw new NullPointerException();
		} else if (null == beanSupport) {
			beanSupport = new PropertyChangeSupport(this);
		}
		beanSupport.addPropertyChangeListener(propertyName, lsnr);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener lsnr) {
		if (null == lsnr) {
			throw new NullPointerException();
		} else if (null != beanSupport) {
			beanSupport.removePropertyChangeListener(lsnr);
		}
	}

	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener lsnr) {
		if (null == lsnr) {
			throw new NullPointerException();
		} else if (null != beanSupport) {
			beanSupport.removePropertyChangeListener(propertyName, lsnr);
		}
	}

	protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
		if (null != beanSupport) {
			beanSupport.firePropertyChange(propertyName, oldValue, newValue);
		}
	}

	protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
		if (null != beanSupport) {
			beanSupport.firePropertyChange(propertyName, oldValue, newValue);
		}
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		if (null != beanSupport) {
			beanSupport.firePropertyChange(propertyName, oldValue, newValue);
		}
	}

	protected void firePropertyChange(PropertyChangeEvent forwardedEvent) {
		if (null != beanSupport) {
			beanSupport.firePropertyChange(forwardedEvent);
		}
	}

}
