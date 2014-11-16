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

package cz.auderis.tools.math;

/**
 * The type Counter.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class Counter {

	private int value;

	/**
	 * Instantiates a new Counter.
	 */
	public Counter() {
		this(0);
	}

	/**
	 * Instantiates a new Counter.
	 *
	 * @param initial the initial
	 */
	public Counter(int initial) {
		this.value = initial - 1;
	}

	/**
	 * Next int.
	 *
	 * @return the int
	 */
	public int next() {
		++value;
		return value;
	}

	/**
	 * Value int.
	 *
	 * @return the int
	 */
	public int value() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof Counter)) {
			return false;
		}
		final Counter other = (Counter) obj;
		if (this.value != other.value) {
			return false;
		}
		return true;
	}

}
