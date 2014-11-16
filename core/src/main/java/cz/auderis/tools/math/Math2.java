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

import java.math.BigDecimal;

/**
 * The type Math 2.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public final class Math2 {

	/**
	 * Min long.
	 *
	 * @param xs the xs
	 * @return the long
	 */
	public static long min(long... xs) {
		if ((null == xs) || (0 == xs.length)) {
			throw new IllegalArgumentException();
		}
		long min = xs[0];
		for (int i = 1; i < xs.length; ++i) {
			if (xs[i] < min) {
				min = xs[i];
			}
		}
		return min;
	}

	/**
	 * Max long.
	 *
	 * @param xs the xs
	 * @return the long
	 */
	public static long max(long... xs) {
		if ((null == xs) || (0 == xs.length)) {
			throw new IllegalArgumentException();
		}
		long max = xs[0];
		for (int i = 1; i < xs.length; ++i) {
			if (xs[i] > max) {
				max = xs[i];
			}
		}
		return max;
	}

	/**
	 * Min int.
	 *
	 * @param xs the xs
	 * @return the int
	 */
	public static int min(int... xs) {
		if ((null == xs) || (0 == xs.length)) {
			throw new IllegalArgumentException();
		}
		int min = xs[0];
		for (int i = 1; i < xs.length; ++i) {
			if (xs[i] < min) {
				min = xs[i];
			}
		}
		return min;
	}

	/**
	 * Max int.
	 *
	 * @param xs the xs
	 * @return the int
	 */
	public static int max(int... xs) {
		if ((null == xs) || (0 == xs.length)) {
			throw new IllegalArgumentException();
		}
		int max = xs[0];
		for (int i = 1; i < xs.length; ++i) {
			if (xs[i] > max) {
				max = xs[i];
			}
		}
		return max;
	}

	/**
	 * Min big decimal.
	 *
	 * @param xs the xs
	 * @return the big decimal
	 */
	public static BigDecimal min(BigDecimal... xs) {
		if ((null == xs) || (0 == xs.length)) {
			throw new IllegalArgumentException();
		}
		BigDecimal min = null;
		for (int i = 0; i < xs.length; ++i) {
			final BigDecimal x = xs[i];
			if (null == x) {
				continue;
			}
			if ((null == min) || (x.compareTo(min) < 0))  {
				min = x;
			}
		}
		return min;
	}

	/**
	 * Max big decimal.
	 *
	 * @param xs the xs
	 * @return the big decimal
	 */
	public static BigDecimal max(BigDecimal... xs) {
		if ((null == xs) || (0 == xs.length)) {
			throw new IllegalArgumentException();
		}
		BigDecimal max = null;
		for (int i = 0; i < xs.length; ++i) {
			final BigDecimal x = xs[i];
			if (null == x) {
				continue;
			}
			if ((null == max) || (x.compareTo(max) > 0))  {
				max = x;
			}
		}
		return max;
	}

	private Math2() {
		throw new AssertionError();
	}

}
