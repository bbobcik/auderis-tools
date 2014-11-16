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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Boxed numbers.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public final class BoxedNumbers {

	/**
	 * The constant INTEGER_CLASSES.
	 */
	public static final Set<Class<? extends Number>> INTEGER_CLASSES;
	/**
	 * The constant FLOAT_CLASSES.
	 */
	public static final Set<? extends Class<?>> FLOAT_CLASSES;

	static {
		final Set<Class<? extends Number>> intClasses = new HashSet<Class<? extends Number>>(4);
		final Set<Class<? extends Number>> floatClasses = new HashSet<Class<? extends Number>>(2);
		intClasses.addAll(Arrays.asList(Byte.class, Short.class, Integer.class, Long.class));
		floatClasses.addAll(Arrays.asList(Float.class, Double.class));
		INTEGER_CLASSES = Collections.unmodifiableSet(intClasses);
		FLOAT_CLASSES = Collections.unmodifiableSet(floatClasses);
	}

	/**
	 * Is boxed number class.
	 *
	 * @param cls the cls
	 * @return the boolean
	 */
	public static boolean isBoxedNumberClass(Class<?> cls) {
		if (null == cls) {
			throw new NullPointerException();
		}
		return INTEGER_CLASSES.contains(cls) || FLOAT_CLASSES.contains(cls);
	}

	/**
	 * Is boxed number.
	 *
	 * @param value the value
	 * @return the boolean
	 */
	public static boolean isBoxedNumber(Object value) {
		if (null == value) {
			return false;
		}
		return isBoxedNumberClass(value.getClass());
	}

	/**
	 * Is convertible.
	 *
	 * @param fromClass the from class
	 * @param toClass the to class
	 * @return the boolean
	 */
	public static boolean isConvertible(Class<?> fromClass, Class<?> toClass) {
		if ((null == fromClass) || (null == toClass)) {
			throw new NullPointerException();
		}
		if (INTEGER_CLASSES.contains(fromClass) && INTEGER_CLASSES.contains(toClass)) {
			return true;
		} else if (FLOAT_CLASSES.contains(fromClass) && FLOAT_CLASSES.contains(toClass)) {
			return true;
		}
		return false;
	}

	/**
	 * Convert safely.
	 *
	 * @param <T>  the type parameter
	 * @param value the value
	 * @param toClass the to class
	 * @return the t
	 */
	public static <T> T convertSafely(Object value, Class<T> toClass) {
		if (null == toClass) {
			throw new NullPointerException();
		} else if (null == value) {
			return null;
		}
		final Class<?> fromClass = value.getClass();
		if (!isConvertible(fromClass, toClass)) {
			return null;
		}
		if (INTEGER_CLASSES.contains(fromClass) && INTEGER_CLASSES.contains(toClass)) {
			return fromLong(toLong(value, fromClass), toClass);
		} else if (FLOAT_CLASSES.contains(fromClass) && FLOAT_CLASSES.contains(toClass)) {
			return fromDouble(toDouble(value, fromClass), toClass);
		}
		throw new IllegalArgumentException("unable to convert from " + fromClass + " to " + toClass);
	}

	private static long toLong(Object value, Class<?> fromClass) {
		final long result;
		if (fromClass.equals(Byte.class)) {
			result = ((Byte) value).longValue();
		} else if (fromClass.equals(Short.class)) {
			result = ((Short) value).longValue();
		} else if (fromClass.equals(Integer.class)) {
			result = ((Integer) value).longValue();
		} else {
			result = ((Long) value).longValue();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static <T> T fromLong(long value, Class<T> toClass) {
		T result;
		if (toClass.equals(Byte.class)) {
			result = (T) Byte.valueOf((byte) value);
		} else if (toClass.equals(Short.class)) {
			result = (T) Short.valueOf((short) value);
		} else if (toClass.equals(Integer.class)) {
			result = (T) Integer.valueOf((int) value);
		} else {
			result = (T) Long.valueOf(value);
		}
		return result;
	}

	private static double toDouble(Object value, Class<?> fromClass) {
		final double result;
		if (fromClass.equals(Float.class)) {
			result = ((Float) value).doubleValue();
		} else {
			result = ((Double) value).doubleValue();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static <T> T fromDouble(double value, Class<T> toClass) {
		T result;
		if (toClass.equals(Float.class)) {
			result = (T) Float.valueOf((float) value);
		} else {
			result = (T) Double.valueOf(value);
		}
		return result;
	}

	private BoxedNumbers() {
		throw new AssertionError();
	}

}
