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

package cz.auderis.tools.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that simplifies common reflective operations
 * performed on classes and their members.
 *
 */
public final class ReflectionUtils {

	/**
	 * Obtains a list of all normal non-final fields
	 * defined in the target class. Normal field denotes
	 * a field that is neither static nor synthetic;
	 * {@link cz.auderis.tools.lang.DefaultMemberFilter} is
	 * used as the filtering predicate.
	 *
	 * @param cls target class
	 * @return list of normal non-final fields defined in the target class
	 * @see cz.auderis.tools.lang.DefaultMemberFilter
	 */
	public static List<Field> getFields(Class<?> cls) {
		return getFields(cls, DEFAULT_FILTER);
	}

	/**
	 * Gets fields.
	 *
	 * @param cls the cls
	 * @param filter the filter
	 * @return the fields
	 */
	public static List<Field> getFields(Class<?> cls, MemberFilter filter) {
		if (null == cls) {
			throw new NullPointerException();
		} else if (null == filter) {
			filter = DEFAULT_FILTER;
		}
		final List<Field> result = new ArrayList<Field>();
		while (null != cls) {
			final Field[] classFields = cls.getDeclaredFields();
			for (Field f : classFields) {
				if (!filter.accept(f, cls)) {
					continue;
				}
				result.add(f);
			}
			cls = cls.getSuperclass();
		}
		return result;
	}

	/**
	 * Gets normal field.
	 *
	 * @param cls the cls
	 * @param fieldName the field name
	 * @return the normal field
	 */
	public static Field getNormalField(Class<?> cls, String fieldName) {
		if ((null == cls) || (null == fieldName)) {
			throw new NullPointerException();
		}
		while (null != cls) {
			try {
				final Field f = cls.getDeclaredField(fieldName);
				if ((null != f) && !f.isSynthetic()) {
					final int modifiers = f.getModifiers();
					if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
						return f;
					}
				}
			} catch (Exception e) {
				// Ignore and proceed
			}
			cls = cls.getSuperclass();
		}
		return null;
	}

	private ReflectionUtils() {
		throw new AssertionError();
	}

	private static final MemberFilter DEFAULT_FILTER = new DefaultMemberFilter();

}
