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
 * Collection of methods for rounding real numbers. The functions are provided in two variants:
 * <ul>
 * <li>rounding to an integral multiple of a given base value, which must be positive non-zero value, but does not have
 * to be an integer
 * <li>rounding to an integer (i.e. the base value is 1.0)
 * </ul>
 * 
 * @author Boleslav Bobcik
 * @version 1.0
 */
public final class Round {

	/**
	 * Rounds the number to the greatest integer <code>n</code>, such that <code>n &le; x</code>. Special cases are:
	 * <ul>
	 * <li><code>x = NaN</code>: the result is <code>NaN</code>
	 * <li><code>x</code> is positive or negative infinity: the result is unchanged
	 * </ul>
	 * 
	 * @param x
	 *            a number
	 * @return the greatest integer less than or equal to <code>x</code>
	 * @see java.lang.Math#floor(double)
	 */
	public static double floor(double x) {
		return Math.floor(x);
	}

	public static BigDecimal floor(BigDecimal x) {
		if (null == x) {
			throw new NullPointerException();
		}
		return x.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_FLOOR);
	}

	/**
	 * Rounds the number to the greatest integral multiple of the base step value, which is less than or equal to the
	 * input value. For example, calling <code>floor(27.5, 10.0)</code> will return value <code>20.0</code>. Special
	 * cases:
	 * <ul>
	 * <li><code>x = NaN</code>: the result is <code>NaN</code>
	 * <li><code>x</code> is positive or negative infinity: the result is unchanged
	 * </ul>
	 * <p>
	 * The base step value must be positive (greater than 0) and finite.
	 * <p>
	 * Formal mathematical description is <code>floor(x, b) = N*b</code>, where <code>N</code> is an integer and
	 * <code>N*b</code> &le; x &lt; <code>(N+1)*b</code>.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @return the greatest integral multiple of <code>baseStep</code> less than or equal to <code>x</code>
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite
	 */
	public static double floor(double x, double baseStep) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isNormalNumber(x)) {
			return x;
		}
		return baseStep * Math.floor(x / baseStep);
	}

	public static BigDecimal floor(BigDecimal x, BigDecimal baseStep) {
		if ((null == x) || (null == baseStep)) {
			throw new NullPointerException();
		} else if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		}
		return baseStep.multiply(x.divide(baseStep, 0, BigDecimal.ROUND_FLOOR));
	}

	/**
	 * Rounds the number to the least integer <code>n</code>, such that <code>x &le; n</code>. Special cases are:
	 * <ul>
	 * <li><code>x = NaN</code>: the result is <code>NaN</code>
	 * <li><code>x</code> is positive or negative infinity: the result is unchanged
	 * </ul>
	 * 
	 * @param x
	 *            a number
	 * @return the least integer greater than or equal to <code>x</code>
	 * @see java.lang.Math#ceil(double)
	 */
	public static double ceiling(double x) {
		return Math.ceil(x);
	}

	public static BigDecimal ceiling(BigDecimal x) {
		if (null == x) {
			throw new NullPointerException();
		}
		return x.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_CEILING);
	}

	/**
	 * Rounds the number to the least integral multiple of the base step value, which is greater than or equal to the
	 * input value. For example, calling <code>ceiling(27.5, 10.0)</code> will return value <code>30.0</code>. Special
	 * cases:
	 * <ul>
	 * <li><code>x = NaN</code>: the result is <code>NaN</code>
	 * <li><code>x</code> is positive or negative infinity: the result is unchanged
	 * </ul>
	 * <p>
	 * The base step value must be positive (greater than 0) and finite.
	 * <p>
	 * Formal mathematical description is <code>ceiling(x, b) = N*b</code>, where <code>N</code> is an integer and
	 * <code>(N-1)*b</code> &lt; x &le; <code>(N)*b</code>.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @return the least integral multiple of <code>baseStep</code> greater than or equal to <code>x</code>
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite
	 */
	public static double ceiling(double x, double baseStep) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isNormalNumber(x)) {
			return x;
		}
		return baseStep * Math.ceil(x / baseStep);
	}

	public static BigDecimal ceiling(BigDecimal x, BigDecimal baseStep) {
		if ((null == x) || (null == baseStep)) {
			throw new NullPointerException();
		} else if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		}
		return baseStep.multiply(x.divide(baseStep, 0, BigDecimal.ROUND_CEILING));
	}

	/**
	 * Rounds the number to the nearest integer, absolute value of which is greater than or equal to absolute value of
	 * the source number. Formally, for non-zero <code>x</code>, <code>truncFromZero(x) = n</code> such that
	 * <code>abs(n)-1</code> &lt; <code>abs(x)</code> &le; <code>abs(n)</code> and <code>sgn(n)</code> =
	 * <code>sgn(x)</code>.
	 * <p>
	 * Special cases are:
	 * <ul>
	 * <li><code>x</code> is zero (positive or negative): the result is unchanged
	 * <li><code>x = NaN</code>: the result is <code>NaN</code>
	 * <li><code>x</code> is positive or negative infinity: the result is unchanged
	 * </ul>
	 * 
	 * @param x
	 *            a number
	 * @return the nearest integer, which is either equal to <code>x</code> or further from 0.0 than the value
	 *         <code>x</code>
	 */
	public static double truncFromZero(double x) {
		if (!isNormalNumber(x)) {
			return x;
		}
		return (x >= 0) ? Math.ceil(x) : Math.floor(x);
	}

	public static BigDecimal truncFromZero(BigDecimal x) {
		if (null == x) {
			throw new NullPointerException();
		}
		return x.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP);
	}

	/**
	 * Rounds the number to the nearest integral multiple of the base step value, which is not closer to zero than the
	 * source value. Formally, for non-zero <code>x</code>, <code>truncFromZero(x, b) = N*b</code> such that
	 * <code>sgn(x)</code> = <code>sgn(N)</code> and <code>(abs(N)-1)*b</code> &lt; <code>abs(x)</code> &le;
	 * <code>abs(N)*b</code>.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li><code>x</code> is zero (positive or negative): the result is unchanged
	 * <li><code>x = NaN</code>: the result is <code>NaN</code>
	 * <li><code>x</code> is positive or negative infinity: the result is unchanged
	 * </ul>
	 * <p>
	 * The base step value must be positive (greater than 0) and finite.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @return the integral multiple of the base step value, which is not closer to zero than the source value itself.
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite
	 */
	public static double truncFromZero(double x, double baseStep) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isNormalNumber(x)) {
			return x;
		} else if (x >= 0) {
			return baseStep * Math.ceil(x / baseStep);
		} else {
			return baseStep * Math.floor(x / baseStep);
		}
	}

	public static BigDecimal truncFromZero(BigDecimal x, BigDecimal baseStep) {
		if ((null == x) || (null == baseStep)) {
			throw new NullPointerException();
		} else if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		}
		return baseStep.multiply(x.divide(baseStep, 0, BigDecimal.ROUND_UP));
	}

	/**
	 * Rounds the number to the nearest integer, absolute value of which is less than or equal to absolute value of the
	 * source number. Formally, for <code>x</code> (where <code>abs(x)</code> &ge; 1),
	 * <code>truncTowardsZero(x) = n</code> such that <code>abs(n)</code> &le; <code>abs(x)</code> &le;
	 * <code>abs(n)+1</code> and <code>sgn(n)</code> = <code>sgn(x)</code>.
	 * <p>
	 * Special cases are:
	 * <ul>
	 * <li><code>x</code> is zero (positive or negative): the result is unchanged
	 * <li><code>x = NaN</code>: the result is <code>NaN</code>
	 * <li><code>x</code> is positive or negative infinity: the result is unchanged
	 * </ul>
	 * 
	 * @param x
	 *            a number
	 * @return the nearest integer, which is either equal to <code>x</code> or closer to 0.0 than the value
	 *         <code>x</code>
	 */
	public static double truncTowardsZero(double x) {
		if (!isNormalNumber(x)) {
			return x;
		}
		return (x >= 0) ? Math.floor(x) : Math.ceil(x);
	}

	public static BigDecimal truncTowardsZero(BigDecimal x) {
		if (null == x) {
			throw new NullPointerException();
		}
		return x.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_DOWN);
	}

	/**
	 * Rounds the number to the nearest integral multiple of the base step value, which is not further to zero than the
	 * source value. Formally, for non-zero <code>x</code> such that <code>abs(x)</code> &ge; 1,
	 * <code>truncTowardsZero(x, b) = N*b</code>, where <code>sgn(x)</code> = <code>sgn(N)</code> and
	 * <code>abs(N)*b</code> &le; <code>abs(x)</code> &lt; <code>(abs(N)+1)*b</code>.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>for 0 &lt; <code>x</code> &lt; base step: the result is zero (+0)
	 * <li>for -base step &lt; <code>x</code> &lt; 0: the result is negative zero (-0)
	 * <li><code>x</code> is zero (positive or negative): the result is unchanged
	 * <li><code>x = NaN</code>: the result is <code>NaN</code>
	 * <li><code>x</code> is positive or negative infinity: the result is unchanged
	 * </ul>
	 * <p>
	 * The base step value must be positive (greater than 0) and finite.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @return the integral multiple of the base step value, which is not further from zero than the source value
	 *         itself.
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite
	 */
	public static double truncTowardsZero(double x, double baseStep) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isNormalNumber(x)) {
			return x;
		} else if (x >= 0) {
			return baseStep * Math.floor(x / baseStep);
		} else {
			return baseStep * Math.ceil(x / baseStep);
		}
	}

	public static BigDecimal truncTowardsZero(BigDecimal x, BigDecimal baseStep) {
		if ((null == x) || (null == baseStep)) {
			throw new NullPointerException();
		} else if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		}
		return baseStep.multiply(x.divide(baseStep, 0, BigDecimal.ROUND_DOWN));
	}

	/**
	 * Rounds the number to a nearest integer. If the distance to surrounding integers is equal, the result is the
	 * integer with greater absolute value.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>halfFromZero(3.3) = 3.0</code>
	 * <li><code>halfFromZero(3.81) = 4.0</code>
	 * <li><code>halfFromZero(-5.2) = -5.0</code>
	 * <li><code>halfFromZero(-5.64) = -6.0</code>
	 * <li><code>halfFromZero(1.5) = 2.0</code>
	 * <li><code>halfFromZero(-1.5) = -2.0</code>
	 * </ul>
	 * 
	 * @param x
	 *            a number
	 * @return the nearest integer or, if two integers have equal distance to <b>x</b>, the integer with greater
	 *         absolute value is returned
	 */
	public static double halfFromZero(double x) {
		if (!isNormalNumber(x)) {
			return x;
		} else if (x >= 0) {
			return Math.floor(x + HALF_FACTOR);
		} else {
			return Math.ceil(x - HALF_FACTOR);
		}
	}

	public static BigDecimal halfFromZero(BigDecimal x) {
		if (null == x) {
			throw new NullPointerException();
		}
		return x.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Rounds the number to a nearest integral multiple of the base step value. If two base step multiples have equal
	 * distance to the number <b>x</b>, the multiple with greater absolute value is returned.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @return the nearest integral multiple of <b>baseStep</b>; if two multiples have identical distance to <b>x</b>
	 *         the result is the multiple with greater absolute value
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite
	 */
	public static double halfFromZero(double x, double baseStep) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isNormalNumber(x)) {
			return x;
		} else if (x >= 0) {
			return baseStep * Math.floor(x / baseStep + HALF_FACTOR);
		} else {
			return baseStep * Math.ceil(x / baseStep - HALF_FACTOR);
		}
	}

	public static BigDecimal halfFromZero(BigDecimal x, BigDecimal baseStep) {
		if ((null == x) || (null == baseStep)) {
			throw new NullPointerException();
		} else if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		}
		return baseStep.multiply(x.divide(baseStep, 0, BigDecimal.ROUND_HALF_UP));
	}

	/**
	 * Rounds the number to a nearest integer. If the distance to surrounding integers is equal, the result is the
	 * integer with smaller absolute value.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>halfTowardsZero(3.3) = 3.0</code>
	 * <li><code>halfTowardsZero(3.81) = 4.0</code>
	 * <li><code>halfTowardsZero(-5.2) = -5.0</code>
	 * <li><code>halfTowardsZero(-5.64) = -6.0</code>
	 * <li><code>halfTowardsZero(1.5) = 1.0</code>
	 * <li><code>halfTowardsZero(-1.5) = -1.0</code>
	 * </ul>
	 * 
	 * @param x
	 *            a number
	 * @return the nearest integer or, if two integers have equal distance to <b>x</b>, the integer with smaller
	 *         absolute value is returned
	 */
	public static double halfTowardsZero(double x) {
		if (!isNormalNumber(x)) {
			return x;
		} else if (x >= 0) {
			return Math.ceil(x - HALF_FACTOR);
		} else {
			return Math.floor(x + HALF_FACTOR);
		}
	}

	public static BigDecimal halfTowardsZero(BigDecimal x) {
		if (null == x) {
			throw new NullPointerException();
		}
		return x.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_DOWN);
	}

	/**
	 * Rounds the number to a nearest integral multiple of the base step value. If two base step multiples have equal
	 * distance to the number <b>x</b>, the multiple with smaller absolute value is returned.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @return the nearest integral multiple of <b>baseStep</b>; if two multiples have identical distance to <b>x</b>
	 *         the result is the multiple with smaller absolute value
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite
	 */
	public static double halfTowardsZero(double x, double baseStep) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isNormalNumber(x)) {
			return x;
		} else if (x >= 0) {
			return baseStep * Math.ceil(x / baseStep - HALF_FACTOR);
		} else {
			return baseStep * Math.floor(x / baseStep + HALF_FACTOR);
		}
	}

	public static BigDecimal halfTowardsZero(BigDecimal x, BigDecimal baseStep) {
		if ((null == x) || (null == baseStep)) {
			throw new NullPointerException();
		} else if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		}
		return baseStep.multiply(x.divide(baseStep, 0, BigDecimal.ROUND_HALF_DOWN));
	}

	/**
	 * Rounds the number to a nearest integer. If the distance to surrounding integers is equal, the result is the
	 * greater integer (i.e. the number closer to positive infinity).
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>halfUp(3.3) = 3.0</code>
	 * <li><code>halfUp(3.81) = 4.0</code>
	 * <li><code>halfUp(-5.2) = -5.0</code>
	 * <li><code>halfUp(-5.64) = -6.0</code>
	 * <li><code>halfUp(1.5) = 2.0</code>
	 * <li><code>halfUp(-1.5) = -1.0</code>
	 * </ul>
	 * 
	 * @param x
	 *            a number
	 * @return the nearest integer or, if two integers have equal distance to <b>x</b>, the greater integer is returned
	 */
	public static double halfUp(double x) {
		if (!isNormalNumber(x)) {
			return x;
		}
		return Math.floor(x + HALF_FACTOR);
	}

	public static BigDecimal halfUp(BigDecimal x) {
		if (null == x) {
			throw new NullPointerException();
		}
		final int roundMode = (x.signum() >= 0) ? BigDecimal.ROUND_HALF_UP : BigDecimal.ROUND_HALF_DOWN;
		return x.divide(BigDecimal.ONE, 0, roundMode);
	}

	/**
	 * Rounds the number to a nearest integral multiple of the base step value. If two base step multiples have equal
	 * distance to the number <b>x</b>, the greater multiple of the two candidates is returned.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @return the nearest integral multiple of <b>baseStep</b>; if two multiples have identical distance to <b>x</b>
	 *         the result is the greater multiple
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite
	 */
	public static double halfUp(double x, double baseStep) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isNormalNumber(x)) {
			return x;
		}
		return baseStep * Math.floor(x / baseStep + HALF_FACTOR);
	}

	public static BigDecimal halfUp(BigDecimal x, BigDecimal baseStep) {
		if ((null == x) || (null == baseStep)) {
			throw new NullPointerException();
		} else if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		}
		final int roundMode = (x.signum() >= 0) ? BigDecimal.ROUND_HALF_UP : BigDecimal.ROUND_HALF_DOWN;
		return baseStep.multiply(x.divide(baseStep, 0, roundMode));
	}

	/**
	 * Rounds the number to a nearest integer. If the distance to surrounding integers is equal, the result is the
	 * smaller integer (i.e. the number closer to negative infinity).
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>halfDown(3.3) = 3.0</code>
	 * <li><code>halfDown(3.81) = 4.0</code>
	 * <li><code>halfDown(-5.2) = -5.0</code>
	 * <li><code>halfDown(-5.64) = -6.0</code>
	 * <li><code>halfDown(1.5) = 1.0</code>
	 * <li><code>halfDown(-1.5) = -2.0</code>
	 * </ul>
	 * 
	 * @param x
	 *            a number
	 * @return the nearest integer or, if two integers have equal distance to <b>x</b>, the smaller integer is returned
	 */
	public static double halfDown(double x) {
		if (!isNormalNumber(x)) {
			return x;
		}
		return Math.ceil(x - HALF_FACTOR);
	}

	public static BigDecimal halfDown(BigDecimal x) {
		if (null == x) {
			throw new NullPointerException();
		}
		final int roundMode = (x.signum() >= 0) ? BigDecimal.ROUND_HALF_DOWN : BigDecimal.ROUND_HALF_UP;
		return x.divide(BigDecimal.ONE, 0, roundMode);
	}

	/**
	 * Rounds the number to a nearest integral multiple of the base step value. If two base step multiples have equal
	 * distance to the number <b>x</b>, the smaller multiple of the two candidates is returned.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @return the nearest integral multiple of <b>baseStep</b>; if two multiples have identical distance to <b>x</b>
	 *         the result is the smaller multiple
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite
	 */
	public static double halfDown(double x, double baseStep) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isNormalNumber(x)) {
			return x;
		}
		return baseStep * Math.ceil(x / baseStep - HALF_FACTOR);
	}

	public static BigDecimal halfDown(BigDecimal x, BigDecimal baseStep) {
		if ((null == x) || (null == baseStep)) {
			throw new NullPointerException();
		} else if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		}
		final int roundMode = (x.signum() >= 0) ? BigDecimal.ROUND_HALF_DOWN : BigDecimal.ROUND_HALF_UP;
		return baseStep.multiply(x.divide(baseStep, 0, roundMode));
	}

	/**
	 * This function generalizes {@link #halfFromZero(double, double)}, where the "decision point" between two base step
	 * multiples is not exactly in the middle between them, but is specified as an arbitrary fraction.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>partFromZero(14.1, 2.2, 0.3) = 15.4</code>, because the surrounding multiples of 14.1 are 13.2 (2.2*6)
	 * and 15.4 (2.2*7), and the "0.3 decision point" between these multiples is 13.86
	 * <li><code>partFromZero(-8.3, 0.75, 0.05) = -9 </code>, because the surrounding multiples of -8.3 are -8.25
	 * (-11*0.75) and -9 (-12*0.75), and the "0.05 decision point" between these multiples is -8.2875.
	 * </ul>
	 * <p>
	 * The following relation is valid: <code>partFromZero(x, base, 0.5) = halfFromZero(x, base)</code>.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @param splitFraction
	 *            the relative position of "decision point" between the two consecutive integral multiples of
	 *            <b>baseStep</b>
	 * @return the nearest integral multiple of <b>baseStep</b>. If the value <b>x</b> lies directly on the
	 *         "decision point", the multiple with greater absolute value is returned
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite; or when the <code>splitFraction</code>
	 *             is not within interval (0,&nbsp;1).
	 */
	public static double partFromZero(double x, double baseStep, double splitFraction) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isFraction(splitFraction)) {
			throw new IllegalArgumentException(BAD_FRACTION);
		} else if (!isNormalNumber(x)) {
			return x;
		} else if (x >= 0) {
			return baseStep * Math.floor(x / baseStep + 1.0 - splitFraction);
		} else {
			return baseStep * Math.ceil(x / baseStep - 1.0 + splitFraction);
		}
	}

	/**
	 * This function generalizes {@link #halfTowardsZero(double, double)}, where the "decision point" between two base
	 * step multiples is not exactly in the middle between them, but is specified as an arbitrary fraction.
	 * <p>
	 * For detailed explanation, see {@link #partFromZero(double, double, double)}.
	 * 
	 * @param x
	 *            a number
	 * @param baseStep
	 *            the "precision" of rounding
	 * @param splitFraction
	 *            the relative position of "decision point" between the two consecutive integral multiples of
	 *            <b>baseStep</b>
	 * @return the nearest integral multiple of <b>baseStep</b>. If the value <b>x</b> lies directly on the
	 *         "decision point", the multiple with smaller absolute value is returned
	 * @throws IllegalArgumentException
	 *             when the <code>baseStep</code> is negative, zero or infinite; or when the <code>splitFraction</code>
	 *             is not within interval (0,&nbsp;1).
	 */
	public static double partTowardsZero(double x, double baseStep, double splitFraction) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isFraction(splitFraction)) {
			throw new IllegalArgumentException(BAD_FRACTION);
		} else if (!isNormalNumber(x)) {
			return x;
		} else if (x >= 0) {
			return baseStep * Math.ceil(x / baseStep - splitFraction);
		} else {
			return baseStep * Math.floor(x / baseStep + splitFraction);
		}
	}

	/**
	 * This function provides further generalization of rounding.
	 * 
	 * @param x
	 * @param baseStep
	 * @param splitFraction
	 * @param midpoint
	 * @return
	 */
	public static double partFromMidpoint(double x, double baseStep, double splitFraction, double midpoint) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isFraction(splitFraction)) {
			throw new IllegalArgumentException(BAD_FRACTION);
		} else if (!isNormalNumber(x)) {
			return x;
		} else if (x >= midpoint) {
			return baseStep * Math.floor(x / baseStep + 1.0 - splitFraction);
		} else {
			return baseStep * Math.ceil(x / baseStep - 1.0 + splitFraction);
		}
	}

	/**
	 * @param x
	 * @param baseStep
	 * @param splitFraction
	 * @param midpoint
	 * @return
	 */
	public static double partTowardsMidpoint(double x, double baseStep, double splitFraction, double midpoint) {
		if (!isPositiveNumber(baseStep)) {
			throw new IllegalArgumentException(NONPOSITIVE_BASE_STEP);
		} else if (!isFraction(splitFraction)) {
			throw new IllegalArgumentException(BAD_FRACTION);
		} else if (!isNormalNumber(x)) {
			return x;
		} else if (x >= midpoint) {
			return baseStep * Math.ceil(x / baseStep - splitFraction);
		} else {
			return baseStep * Math.floor(x / baseStep + splitFraction);
		}
	}

	/**
	 * @param x
	 * @return
	 */
	public static double halfFromZeroToEven(double x) {
		if ((0 == x) || !isNormalNumber(x)) {
			return x;
		}
		double offset = 0;
		double remainder = x;
		if ((x < Long.MIN_VALUE) || (Long.MAX_VALUE) < x) {
			remainder = Math.IEEEremainder(x, Long.MAX_VALUE - 1);
			offset = x - remainder;

		}
		final boolean floorIsEven = (0 == (1L & (long) Math.floor(remainder)));
		if (floorIsEven) {
			return offset + Math.ceil(remainder - HALF_FACTOR);
		} else {
			return offset + Math.floor(remainder + HALF_FACTOR);
		}
	}

	private static final double HALF_FACTOR = 0.5;

	private static final String BAD_FRACTION = "split fraction must be between 0.0 and 1.0";

	private static final String NONPOSITIVE_BASE_STEP = "base step must be a positive number";

	private Round() {
		throw new AssertionError("utility class, not to be instantiated");
	}

	private static boolean isPositiveNumber(double num) {
		return !(Double.isNaN(num) || Double.isInfinite(num) || num <= 0.0);
	}

	private static boolean isPositiveNumber(BigDecimal num) {
		return 0 < num.signum();
	}

	private static boolean isFraction(double num) {
		return !(Double.isNaN(num) || Double.isInfinite(num)) && (0.0 < num) && (num < 1.0);
	}

	private static boolean isNormalNumber(double num) {
		return !(Double.isNaN(num) || Double.isInfinite(num));
	}
}