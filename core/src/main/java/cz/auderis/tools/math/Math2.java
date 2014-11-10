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

public final class Math2 {

	public static long min(long... xs) {
		long min = Long.MAX_VALUE;
		for (int i = 0; i < xs.length; ++i) {
			if (xs[i] < min) {
				min = xs[i];
			}
		}
		return min;
	}

	public static long max(long... xs) {
		long max = Long.MIN_VALUE;
		for (int i = 0; i < xs.length; ++i) {
			if (xs[i] > max) {
				max = xs[i];
			}
		}
		return max;
	}

	public static int min(int... xs) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < xs.length; ++i) {
			if (xs[i] < min) {
				min = xs[i];
			}
		}
		return min;
	}

	public static int max(int... xs) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < xs.length; ++i) {
			if (xs[i] > max) {
				max = xs[i];
			}
		}
		return max;
	}

	private Math2() {
		throw new AssertionError();
	}

}
