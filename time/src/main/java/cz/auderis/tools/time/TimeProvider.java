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

package cz.auderis.tools.time;

/**
 * This interface describes a general mechanism for obtaining "time". The time
 * typically corresponds to a system clock, but other implementations are
 * possible.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 */
public interface TimeProvider {

	/**
	 * Gets current time in milliseconds. The mechanism used to obtain the time value
	 * depends on implementation.
	 *
	 * @return time given in milliseconds
	 */
	long getTimeInMillis();

}
