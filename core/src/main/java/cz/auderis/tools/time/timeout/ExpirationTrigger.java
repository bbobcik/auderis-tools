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
 * Interface {@code ExpirationTrigger} declares an abstract time-keeping
 * object that contains information about expiration.
 *
 * <p>The expiration may be caused either by normal flow of time or by
 * some other external events.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public interface ExpirationTrigger {

	/**
	 * Returns information whether this trigger has already expired.
	 *
	 * @return {@code true} if the trigger is expired
	 */
	boolean isExpired();

	/**
	 * Causes this trigger to expire immediately.
	 */
	void expireNow();

}
