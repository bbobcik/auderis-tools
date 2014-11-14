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

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * {@code SimpleDurationFormat} is a class for formatting human-readable, localized
 * duration information.
 *
 * <p>In order to improve readability, the leading (i.e. most significant) time unit has typically
 * value greater than 1:
 * <table summary="">
 *     <thead>
 *         <tr>
 *             <th>Duration in seconds</th>
 *             <th>Typical formatted representation</th>
 *         </tr>
 *     </thead>
 *     <tbody>
 *         <tr><td>59</td><td>59 seconds</td></tr>
 *         <tr><td>60</td><td>60 seconds</td></tr>
 *         <tr><td>61</td><td>61 seconds</td></tr>
 *         <tr><td>119</td><td>119 seconds</td></tr>
 *         <tr><td>120</td><td>2 minutes</td></tr>
 *         <tr><td>121</td><td>2 minutes 1 second</td></tr>
 *     </tbody>
 * </table>
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public abstract class SimpleDurationFormat implements Serializable {

	/**
	 * Obtains an instance of {@code SimpleDurationFormat} for the default locale.
	 *
	 * @return instance for default locale
	 * @see java.util.Locale#getDefault()
	 */
	public static SimpleDurationFormat getInstance() {
		return getInstance(Locale.getDefault());
	}

	/**
	 * Obtains an instance of {@code SimpleDurationFormat} for the "undefined" locale,
	 * specified in JDK by an "und" language tag.
	 *
	 * @return instance for the neutral locale
	 * @see java.util.Locale#forLanguageTag(String)
	 */
	public static SimpleDurationFormat getLanguageNeutralInstance() {
		return getInstance(Locale.forLanguageTag("und"));
	}

	/**
	 * Obtains an instance of {@code SimpleDurationFormat} for the provided locale.
	 * If no localization data for the given locale are found, the normal fall-back
	 * mechanism of {@link java.util.ResourceBundle} is applied.
	 *
	 * @param locale the locale
	 * @return instance for the requested locale
	 * @throws java.lang.NullPointerException if {@code locale} is {@code null}
	 */
	public static SimpleDurationFormat getInstance(Locale locale) {
		if (null == locale) {
			throw new NullPointerException();
		}
		SimpleDurationFormat result;
		synchronized (FORMAT_CACHE) {
			result = FORMAT_CACHE.get(locale);
			if ((null == result) && (locale.equals(Locale.getDefault()) || FORMAT_CACHE.size() < CACHE_CAPACITY)) {
				result = new LocalizedDurationFormat(locale);
				FORMAT_CACHE.put(locale, result);
			}
		}
		if (null == result) {
			// Create formatter that is not cached
			result = new LocalizedDurationFormat(locale);
		}
		return result;
	}

	/**
	 * Translates the duration specified in milliseconds to the appropriate
	 * human-readable representation and appends it to the {@code target}.
	 *
	 * <p>If the checked {@link java.io.IOException} is deemed unnecessary (e.g. while
	 * appending to a {@link java.lang.StringBuilder}), the sibling method
	 * {@link #toHumanDurationSafe(long, Appendable)} may be more appropriate.
	 *
	 * @param millis duration given in milliseconds
	 * @param target object to which the formatted duration is appended
	 * @return the provided {@code target} reference
	 * @throws java.lang.NullPointerException if {@code target} is {@code null}
	 * @throws java.lang.IllegalArgumentException if {@code millis} is negative
	 * @throws java.io.IOException if appending to the {@code target} caused an error
	 */
	public abstract Appendable toHumanDuration(long millis, Appendable target) throws IOException;

	/**
	 * Translates the duration specified in milliseconds to the appropriate
	 * human-readable representation and appends it to the {@code target}.
	 *
	 * <p>This method differs from {@link #toHumanDuration(long, Appendable)}
	 * in that the potential {@link java.io.IOException} is suppressed (and omitted
	 * from method signature).
	 *
	 * @param millis duration given in milliseconds
	 * @param target object to which the formatted duration is appended
	 * @return the provided {@code target} reference
	 * @throws java.lang.NullPointerException if {@code target} is {@code null}
	 * @throws java.lang.IllegalArgumentException if {@code millis} is negative
	 */
	public abstract Appendable toHumanDurationSafe(long millis, Appendable target);

	/**
	 * Returns the duration specified in milliseconds in the appropriate
	 * human-readable representation.
	 *
	 * @param millis duration given in milliseconds
	 * @return human-readable representation of the duration
	 * @throws java.lang.IllegalArgumentException if {@code millis} is negative
	 */
	public final String toHumanDuration(long millis) {
		final StringBuilder builder = new StringBuilder();
		try {
			toHumanDuration(millis, builder);
		} catch (IOException e) {
			builder.setLength(0);
		}
		return builder.toString();
	}

	/**
	 * Translates the duration specified in an arbitrary time unit to the appropriate
	 * human-readable representation and appends it to the {@code target}.
	 *
	 * <p>If the checked {@link java.io.IOException} is deemed unnecessary (e.g. while
	 * appending to a {@link java.lang.StringBuilder}), the sibling method
	 * {@link #toHumanDurationSafe(long, java.util.concurrent.TimeUnit, Appendable)}
	 * may be more appropriate.
	 *
	 * @param duration duration given in {@code unit} time unit
	 * @param unit time unit
	 * @param target object to which the formatted duration is appended
	 * @return the provided {@code target} reference
	 * @throws java.lang.NullPointerException if {@code unit} or {@code target} is {@code null}
	 * @throws java.lang.IllegalArgumentException if {@code duration} is negative
	 * @throws java.io.IOException if appending to the {@code target} caused an error
	 */
	public final Appendable toHumanDuration(long duration, TimeUnit unit, Appendable target) throws IOException {
		if (null == unit) {
			throw new NullPointerException();
		}
		return toHumanDuration(unit.toMillis(duration), target);
	}

	/**
	 * Translates the duration specified in an arbitrary time unit to the appropriate
	 * human-readable representation and appends it to the {@code target}.
	 *
	 * <p>This method differs from {@link #toHumanDuration(long, java.util.concurrent.TimeUnit, Appendable)}
	 * in that the potential {@link java.io.IOException} is suppressed (and omitted from method
	 * signature).
	 *
	 * @param duration duration given in {@code unit} time unit
	 * @param unit time unit
	 * @param target object to which the formatted duration is appended
	 * @return the provided {@code target} reference
	 * @throws java.lang.NullPointerException if {@code unit} or {@code target} is {@code null}
	 * @throws java.lang.IllegalArgumentException if {@code duration} is negative
	 */
	public final Appendable toHumanDurationSafe(long duration, TimeUnit unit, Appendable target) {
		if (null == unit) {
			throw new NullPointerException();
		}
		return toHumanDurationSafe(unit.toMillis(duration), target);
	}

	/**
	 * Returns the duration specified in milliseconds in the appropriate
	 * human-readable representation.
	 *
	 * @param duration duration given in {@code unit} time unit
	 * @param unit time unit
	 * @return human-readable representation of the duration
	 * @throws java.lang.IllegalArgumentException if {@code duration} is negative
	 */
	public final String toHumanDuration(long duration, TimeUnit unit) {
		if (null == unit) {
			throw new NullPointerException();
		}
		return toHumanDuration(unit.toMillis(duration));
	}

	/**
	 * The default constructor has {@code protected} visibility
	 */
	protected SimpleDurationFormat() {
		// Not to be instantiated directly
	}

	/**
	 * An array of {@link java.util.concurrent.TimeUnit}s (ordered
	 * from the most significant to the least significant) that
	 * are used for duration formatting.
	 */
	static final TimeUnit[] DURATION_UNITS = {
			TimeUnit.DAYS,
			TimeUnit.HOURS,
			TimeUnit.MINUTES,
			TimeUnit.SECONDS };

	protected static final Map<TimeUnit, String> LABEL_BY_UNIT;

	private static final int CACHE_CAPACITY = 4;
	private static final Map<Locale, SimpleDurationFormat> FORMAT_CACHE;

	static {
		LABEL_BY_UNIT = new EnumMap<TimeUnit, String>(TimeUnit.class);
		LABEL_BY_UNIT.put(TimeUnit.DAYS, "days");
		LABEL_BY_UNIT.put(TimeUnit.HOURS, "hours");
		LABEL_BY_UNIT.put(TimeUnit.MINUTES, "minutes");
		LABEL_BY_UNIT.put(TimeUnit.SECONDS, "seconds");
		LABEL_BY_UNIT.put(TimeUnit.MILLISECONDS, "milliseconds");
		FORMAT_CACHE = new HashMap<Locale, SimpleDurationFormat>();
	}

	protected static class LocalizedDurationFormat extends SimpleDurationFormat {
		private static final long serialVersionUID = 1L;

		private static final String SPACE = "\u0020";
		private final Map<TimeUnit, String> templateByUnit;
		private String separator;
		private String zeroTime;
		private TimeUnit leastDefinedUnit;

		protected LocalizedDurationFormat(Locale locale) {
			super();
			templateByUnit = new EnumMap<TimeUnit, String>(TimeUnit.class);
			loadResources(locale);
		}

		@Override
		public Appendable toHumanDuration(long millis, Appendable target) throws IOException {
			if (null == target) {
				throw new NullPointerException();
			} else if (millis < 0L) {
				throw new IllegalArgumentException("invalid duration");
			}
			if (0 == millis) {
				target.append(zeroTime);
				return target;
			}
			long remainingMillis = millis;
			String partSeparator = null;
			for (TimeUnit durationUnit : DURATION_UNITS) {
				final String template = templateByUnit.get(durationUnit);
				if (null == template) {
					continue;
				}
				final long duration = durationUnit.convert(remainingMillis, TimeUnit.MILLISECONDS);
				if ((null == partSeparator) && (duration < 2L) && (durationUnit != leastDefinedUnit)) {
					// The formatting did not started yet and the value for the leading part is too small
					continue;
				} else if (null != partSeparator) {
					target.append(partSeparator);
				} else {
					partSeparator = separator;
				}
				final String unitPart = MessageFormat.format(template, duration);
				target.append(unitPart);
				remainingMillis -= durationUnit.toMillis(duration);
				if ((0 == remainingMillis) || (durationUnit == leastDefinedUnit)) {
					break;
				}
			}
			return target;
		}

		@Override
		public Appendable toHumanDurationSafe(long millis, Appendable target) {
			if (null == target) {
				throw new NullPointerException();
			} else if (millis < 0L) {
				throw new IllegalArgumentException("invalid duration");
			}
			try {
				return toHumanDuration(millis, target);
			} catch (IOException e) {
				return null;
			}
		}

		private void loadResources(Locale locale) {
			final ResourceBundle res = findBundle(locale);
			separator = getResourceString(res, DurationFormatResource.PART_SEPARATOR_KEY, SPACE);
			zeroTime = getResourceString(res, DurationFormatResource.ZERO_TIME_KEY, DurationFormatResource.ZERO_TIME_KEY);
			leastDefinedUnit = null;
			for (TimeUnit timeUnit : DURATION_UNITS) {
				final String resourceKey = LABEL_BY_UNIT.get(timeUnit);
				String template = getResourceString(res, resourceKey, null);
				if (null == template) {
					template = "{0} " + resourceKey;
				}
				final boolean unitIsUsed = !template.trim().isEmpty();
				final boolean isLastUnit = (timeUnit == DURATION_UNITS[DURATION_UNITS.length - 1]);
				if (unitIsUsed) {
					templateByUnit.put(timeUnit, template);
					leastDefinedUnit = timeUnit;
				} else if ((null == leastDefinedUnit) &&  isLastUnit) {
					// No templates were defined at all - need fallback template
					templateByUnit.put(timeUnit, "{0} " + resourceKey);
					leastDefinedUnit = timeUnit;
				}
			}
		}

		private ResourceBundle findBundle(Locale locale) {
			// First try to load resources using caller's classloader
			final String simpleBundleName = DurationFormatResource.class.getSimpleName();
			try {
				return ResourceBundle.getBundle(simpleBundleName, locale);
			} catch (MissingResourceException e) {
				// Proceed to next fallback
			}
			// Using classloader related to this class is the first fallback method
			final ClassLoader localClassloader = getClass().getClassLoader();
			try {
				return ResourceBundle.getBundle(simpleBundleName, locale, localClassloader);
			} catch (MissingResourceException e) {
				// Proceed to next fallback
			}
			// Use fully qualified package name
			final String qualifiedBundleName = DurationFormatResource.class.getName();
			try {
				return ResourceBundle.getBundle(qualifiedBundleName, locale);
			} catch (MissingResourceException e) {
				// Proceed to next fallback
			}
			try {
				return ResourceBundle.getBundle(qualifiedBundleName, locale, localClassloader);
			} catch (MissingResourceException e) {
				// Proceed to next fallback
			}
			// Last fallback - return default bundle
			return new DurationFormatResource();
		}

		private static String getResourceString(ResourceBundle resources, String key, String defaultValue) {
			String result;
			if (resources.containsKey(key)) {
				result = resources.getString(key);
			} else {
				result = defaultValue;
			}
			return result;
		}

	}

}
