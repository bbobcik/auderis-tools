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

/**
 * {@code BeanBasedTimeout}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public interface BeanBasedTimeout extends Timeout {

	String PROPERTY_TIMEOUT = "timeout";

	String PROPERTY_RUNNING = "running";

	String PROPERTY_ELAPSED = "elapsed";


	public void addPropertyChangeListener(PropertyChangeListener lsnr);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener lsnr);

	public void removePropertyChangeListener(PropertyChangeListener lsnr);

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener lsnr);

}
