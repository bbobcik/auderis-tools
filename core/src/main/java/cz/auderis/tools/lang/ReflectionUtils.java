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
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public static List<Field> getNormalFields(Class<?> cls) {
		return getFields(cls, DefaultMemberFilter.NORMAL_FIELDS);
	}

	/**
	 * Gets fields.
	 *
	 * @param cls the cls
	 * @param filters one or more filters
	 * @return the fields
	 */
	public static List<Field> getFields(Class<?> cls, MemberFilter... filters) {
		if (null == cls) {
			throw new NullPointerException();
		} else if (null == filters) {
			filters = EMPTY_FILTER_ARRAY;
		}
		final List<Field> result = new ArrayList<Field>();
		Class<?> currCls = cls;
		while (null != currCls) {
			final Field[] classFields = currCls.getDeclaredFields();
			FIELD_ITERATION:
			for (Field f : classFields) {
				for (MemberFilter filter : filters) {
					if (!filter.accept(f, currCls, cls)) {
						continue FIELD_ITERATION;
					}
				}
				result.add(f);
			}
			currCls = currCls.getSuperclass();
		}
		return result;
	}

	public static List<Method> getNormalMethods(Class<?> cls) {
		return getMethods(cls, DefaultMemberFilter.NORMAL_METHODS);
	}

	public static List<Method> getMethods(Class<?> cls, MemberFilter... filters) {
		if (null == cls) {
			throw new NullPointerException();
		} else if (null == filters) {
			filters = EMPTY_FILTER_ARRAY;
		}
		final List<Method> result = new ArrayList<Method>();
		Class<?> currCls = cls;
		while (null != currCls) {
			final Method[] classMethods = currCls.getDeclaredMethods();
			METHOD_ITERATION:
			for (Method m : classMethods) {
				for (MemberFilter filter : filters) {
					if (!filter.accept(m, currCls, cls)) {
						continue METHOD_ITERATION;
					}
				}
				result.add(m);
			}
			currCls = currCls.getSuperclass();
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

	public static Set<String> getMemberNames(Collection<? extends Member> memberCollection) {
		if (null == memberCollection) {
			throw new NullPointerException();
		} else if (memberCollection.isEmpty()) {
			return Collections.emptySet();
		}
		final Set<String> result = new HashSet<String>(memberCollection.size());
		for (Member member : memberCollection) {
			if (null != member) {
				result.add(member.getName());
			}
		}
		return result;
	}

	private ReflectionUtils() {
		throw new AssertionError();
	}

	private static final MemberFilter[] EMPTY_FILTER_ARRAY = { };

}
