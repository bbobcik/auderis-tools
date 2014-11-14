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

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * {@code ThreadLocalDateFormat} simplifies handling of {@link java.text.SimpleDateFormat}
 * in concurrent, multi-threaded environments. This class serves as a thin wrapper
 * that delegates formatting and parsing tasks to thread-local instances.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class ThreadLocalDateFormat extends DateFormat {

	private static final long serialVersionUID = -3298852618176961941L;

	/**
	 * Default format template used when default constructor is invoked.
	 */
	public static final String ISO_FORMAT_MASK = "yyyy-MM-dd HH:mm:ss";

	private final String template;
	private final Locale locale;
	private transient ThreadLocal<SimpleDateFormat> format;

	/**
	 * Creates a new instance using {@link #ISO_FORMAT_MASK} formatting
	 * template.
	 */
	public ThreadLocalDateFormat() {
		this(ISO_FORMAT_MASK);
	}

	/**
	 * Creates a new instance using the provided formatting template for the underlying
	 * {@link java.text.SimpleDateFormat}.
	 *
	 * @param template format template
	 */
	public ThreadLocalDateFormat(final String template) {
		this(template, null);
	}

	/**
	 * Creates a new instance using the provided formatting template and
	 * non-default {@link java.util.Locale} for the underlying
	 * {@link java.text.SimpleDateFormat}.
	 *
	 * @param template format template
	 * @param locale locale to be used
	 */
	public ThreadLocalDateFormat(final String template, final Locale locale) {
		super();
		if (null == template) {
			throw new NullPointerException();
		}
		this.template = template;
		this.locale = locale;
		initFormatStore();
	}

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
		return format.get().format(date, toAppendTo, pos);
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		return format.get().parse(source, pos);
	}

	@Override
	public ThreadLocalDateFormat clone() {
		final ThreadLocalDateFormat clone = (ThreadLocalDateFormat) super.clone();
		clone.initFormatStore();
		return clone;
	}

	private void initFormatStore() {
		final Locale usedLocale;
		if (null != locale) {
			usedLocale = locale;
		} else {
			usedLocale = Locale.getDefault();
		}
		this.format = new ThreadLocal<SimpleDateFormat>() {
			@Override
			protected SimpleDateFormat initialValue() {
				return new SimpleDateFormat(template, usedLocale);
			}
		};
	}

}
