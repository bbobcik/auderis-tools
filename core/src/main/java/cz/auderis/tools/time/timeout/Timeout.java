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

/**
 * Interface {@code Timeout} extends the concept of {@link cz.auderis.tools.time.timeout.ExpirationTrigger}
 * to make tighter relation to the flow of time as the primary cause of expiration triggering.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public interface
		Timeout extends ExpirationTrigger {

	/**
	 * The MAX _ pERCENT.
	 */
	int MAX_PERCENT = 100;

	/**
	 * Provides the total timeout time in milliseconds. Notice that this is configurable value, not a remaining time.
	 *
	 * @return milliseconds representing total timeout duration
	 */
	long getTimeout();

	/**
	 * Sets timeout.
	 *
	 * @param millis the millis
	 * @return the timeout
	 */
	Timeout setTimeout(long millis);

	/**
	 * Start timeout.
	 *
	 * @return the timeout
	 */
	Timeout start();

	/**
	 * Start if not running.
	 *
	 * @return the timeout
	 */
	Timeout startIfNotRunning();

	/**
	 * Stop timeout.
	 *
	 * @return the timeout
	 */
	Timeout stop();

	/**
	 * Restart timeout.
	 *
	 * @return the timeout
	 */
	Timeout restart();

	/**
	 * Is running.
	 *
	 * @return the boolean
	 */
	boolean isRunning();

	/**
	 * Is elapsed.
	 *
	 * @return the boolean
	 */
	boolean isElapsed();

	/**
	 * Restart if elapsed.
	 *
	 * @return the boolean
	 */
	boolean restartIfElapsed();

	/**
	 * Gets remaining percent.
	 *
	 * @return the remaining percent
	 */
	int getRemainingPercent();

	/**
	 * Gets remaining millis.
	 *
	 * @return the remaining millis
	 */
	Long getRemainingMillis();

}
