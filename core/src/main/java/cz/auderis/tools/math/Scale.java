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
 * The type Scale.
 * @author Boleslav Bobcik
 * @version 1.0
 */
public final class Scale {

	private static final double BASE_TEN = 10.0;
	private static final double LN10 = Math.log(10.0);

	/**
	 * Magnitude double.
	 *
	 * @param x the x
	 * @return double
	 */
	public static double magnitude(double x) {
		if (Double.isNaN(x)) {
			return x;
		} else if (Double.isInfinite(x)) {
			return Double.POSITIVE_INFINITY;
		} else if (0.0 == x) {
			return +0.0;
		}
		return Math.floor(absLog10(x));
	}

	/**
	 * Unit from number.
	 *
	 * @param x the x
	 * @return double
	 */
	public static double unitFromNumber(double x) {
		if (Double.isNaN(x)) {
			return x;
		} else if (Double.isInfinite(x)) {
			return Double.POSITIVE_INFINITY;
		} else if (0.0 == x) {
			return 1.0;
		}
		return Math.pow(BASE_TEN, Math.floor(absLog10(x)));
	}

	/**
	 * Unit from magnitude.
	 *
	 * @param mag the mag
	 * @return double
	 */
	public static double unitFromMagnitude(double mag) {
		if (Double.isNaN(mag)) {
			return mag;
		} else if (Double.POSITIVE_INFINITY == mag) {
			return Double.POSITIVE_INFINITY;
		} else if (Double.NEGATIVE_INFINITY == mag) {
			return 0;
		}
		return Math.pow(BASE_TEN, mag);
	}

	/**
	 * Normalize double.
	 *
	 * @param x the x
	 * @return double
	 */
	public static double normalize(double x) {
		if (Double.isNaN(x) || Double.isInfinite(x) || (0.0 == x)) {
			return x;
		}
		final double absX = Math.abs(x);
		return absX / Math.pow(BASE_TEN, Math.floor(absLog10(absX)));
	}

	/**
	 * Decompose number decomposition.
	 *
	 * @param x the x
	 * @return number decomposition
	 */
	public static NumberDecomposition decompose(double x) {
		if (Double.isNaN(x)) {
			return SpecialDecomposition.NAN;
		} else if (Double.POSITIVE_INFINITY == x) {
			return SpecialDecomposition.POS_INF;
		} else if (Double.NEGATIVE_INFINITY == x) {
			return SpecialDecomposition.NEG_INF;
		} else if (0.0 == x) {
			return SpecialDecomposition.ZERO;
		}
		double sign;
		if (x > 0) {
			sign = 1;
		} else {
			sign = -1;
			// Avoid negative zero...
			x = 0.0 - x;
		}
		double unit = Math.pow(BASE_TEN, Math.floor(absLog10(x)));
		double norm = x / unit;
		return new RegularDecomposition(x, sign, norm, unit);
	}


	private Scale() {
		throw new AssertionError("utility class, not to be instantiated");
	}

	private static double absLog10(double x) {
		return ((x <= 0.0) ? Math.log(0.0 - x) : Math.log(x)) / LN10;
	}

	private enum SpecialDecomposition implements NumberDecomposition {
		/**
		 * The ZERO.
		 */
		ZERO {
			@Override
			public double getNormalizedValue() {
				return 0.0;
			}

			@Override
			public double getSign() {
				return 0.0;
			}

			@Override
			public double getUnit() {
				return 1.0;
			}

			@Override
			public double getValue() {
				return 0.0;
			}
		},
		/**
		 * The POS_INF.
		 */
		POS_INF {
			@Override
			public double getNormalizedValue() {
				return 1.0;
			}

			@Override
			public double getSign() {
				return 1.0;
			}

			@Override
			public double getUnit() {
				return Double.POSITIVE_INFINITY;
			}

			@Override
			public double getValue() {
				return Double.POSITIVE_INFINITY;
			}
		},
		/**
		 * The NEG_INF.
		 */
		NEG_INF {
			@Override
			public double getNormalizedValue() {
				return 1.0;
			}

			@Override
			public double getSign() {
				return -1.0;
			}

			@Override
			public double getUnit() {
				return Double.POSITIVE_INFINITY;
			}

			@Override
			public double getValue() {
				return Double.NEGATIVE_INFINITY;
			}
		},
		/**
		 * The NAN.
		 */
		NAN {
			@Override
			public double getNormalizedValue() {
				return Double.NaN;
			}

			@Override
			public double getSign() {
				return Double.NaN;
			}

			@Override
			public double getUnit() {
				return Double.NaN;
			}

			@Override
			public double getValue() {
				return Double.NaN;
			}
		}
	}

	private static final class RegularDecomposition implements NumberDecomposition {
		private final double value;

		private final double sign;

		private final double norm;

		private final double unit;

		/**
		 * Instantiates a new Regular decomposition.
		 *
		 * @param value the value
		 * @param sign the sign
		 * @param norm the norm
		 * @param unit the unit
		 */
		protected RegularDecomposition(double value, double sign, double norm, double unit) {
			this.value = value;
			this.sign = sign;
			this.norm = norm;
			this.unit = unit;
		}

		@Override
		public double getValue() {
			return value;
		}

		@Override
		public double getSign() {
			return sign;
		}

		@Override
		public double getNormalizedValue() {
			return norm;
		}

		@Override
		public double getUnit() {
			return unit;
		}
	}

}
