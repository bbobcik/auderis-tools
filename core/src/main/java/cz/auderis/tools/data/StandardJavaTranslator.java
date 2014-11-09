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

package cz.auderis.tools.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code PrimitiveTranslator}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
final class StandardJavaTranslator {

	private static final StandardJavaTranslator INSTANCE = new StandardJavaTranslator();
	private final Map<Class<?>, PrimitiveTranslator> primitiveTranslatorMap;
	private final Map<Class<?>, Class<?>> primitiveToBoxedMap;
	private final Map<Class<?>, Class<?>> boxedToPrimitiveMap;

	static StandardJavaTranslator instance() {
		return INSTANCE;
	}

	boolean isPrimitiveOrBoxed(Class<?> targetType) {
		return primitiveTranslatorMap.containsKey(targetType);
	}

	Class<?> switchPrimitiveAndBoxedType(Class<?> type) {
		if (type.isPrimitive()) {
			return primitiveToBoxedMap.get(type);
		}
		return boxedToPrimitiveMap.get(type);
	}

	Object translatePrimitive(Object source, Class<?> resourceType) {
		final PrimitiveTranslator translator = primitiveTranslatorMap.get(resourceType);
		if (null == translator) {
			return null;
		}
		try {
			if ((null != source) && translator.getSupportedClasses().contains(source.getClass())) {
				// Skip 1:1 conversion
				return source;
			} else if (source instanceof String) {
				return translator.translateString((String) source);
			} else if (resourceType.isPrimitive()) {
				return translator.defaultValue();
			}
			return translator.translate(source);
		} catch (Exception e) {
			return translator.defaultValue();
		}
	}

	Object translateEnum(Object sourceValue, Class<?> returnType) {
		if (null == sourceValue) {
			return null;
		} else if (returnType.isAssignableFrom(sourceValue.getClass())) {
			return sourceValue;
		} else if (sourceValue instanceof String) {
			final String enumName = (String) sourceValue;
			for (Object enumObj : returnType.getEnumConstants()) {
				final Enum enumConst = (Enum) enumObj;
				if (enumName.equalsIgnoreCase(enumConst.name())) {
					return enumConst;
				}
			}
		}
		return null;
	}

	private StandardJavaTranslator() {
		primitiveTranslatorMap = new HashMap<Class<?>, PrimitiveTranslator>();
		primitiveToBoxedMap = new HashMap<Class<?>, Class<?>>();
		boxedToPrimitiveMap = new HashMap<Class<?>, Class<?>>();
		for (PrimitiveTranslator tx : PrimitiveTranslator.values()) {
			final List<Class<?>> supportedClasses = tx.getSupportedClasses();
			for (Class<?> supportedClass : supportedClasses) {
				primitiveTranslatorMap.put(supportedClass, tx);
			}
			final Class<?> primitiveType = supportedClasses.get(1);
			final Class<?> boxedType = supportedClasses.get(0);
			primitiveToBoxedMap.put(primitiveType, boxedType);
			boxedToPrimitiveMap.put(boxedType, primitiveType);
		}
	}


	enum PrimitiveTranslator {

		BOOLEAN {
			@Override
			public List<Class<?>> getSupportedClasses() {
				return Arrays.asList(Boolean.class, (Class<?>) Boolean.TYPE);
			}

			@Override
			public Object translateString(String source) {
				return Boolean.parseBoolean(source);
			}

			@Override
			public Object defaultValue() {
				return false;
			}
		},

		BYTE {
			@Override
			public List<Class<?>> getSupportedClasses() {
				return Arrays.asList(Byte.class, (Class<?>) Byte.TYPE);
			}

			@Override
			public Object translateString(String source) {
				return Byte.parseByte(source);
			}

			@Override
			public Object defaultValue() {
				return (byte) 0;
			}
		},

		SHORT {
			@Override
			public List<Class<?>> getSupportedClasses() {
				return Arrays.asList(Short.class, (Class<?>) Short.TYPE);
			}

			@Override
			public Object translateString(String source) {
				return Short.parseShort(source);
			}

			@Override
			public Object defaultValue() {
				return (short) 0;
			}
		},

		INT {
			@Override
			public List<Class<?>> getSupportedClasses() {
				return Arrays.asList(Integer.class, (Class<?>) Integer.TYPE);
			}

			@Override
			public Object translateString(String source) {
				return Integer.parseInt(source);
			}

			@Override
			public Object defaultValue() {
				return 0;
			}
		},

		LONG {
			@Override
			public List<Class<?>> getSupportedClasses() {
				return Arrays.asList(Long.class, (Class<?>) Long.TYPE);
			}

			@Override
			public Object translateString(String source) {
				return Long.parseLong(source);
			}

			@Override
			public Object defaultValue() {
				return 0L;
			}
		},

		FLOAT {
			@Override
			public List<Class<?>> getSupportedClasses() {
				return Arrays.asList(Float.class, (Class<?>) Float.TYPE);
			}

			@Override
			public Object translateString(String source) {
				return Float.parseFloat(source);
			}

			@Override
			public Object defaultValue() {
				return (float) 0;
			}
		},

		DOUBLE {
			@Override
			public List<Class<?>> getSupportedClasses() {
				return Arrays.asList(Double.class, (Class<?>) Double.TYPE);
			}

			@Override
			public Object translateString(String source) {
				return Double.parseDouble(source);
			}

			@Override
			public Object defaultValue() {
				return (double) 0;
			}
		},
		;

		public abstract List<Class<?>> getSupportedClasses();

		public abstract Object defaultValue();

		public abstract Object translateString(String source);

		public Object translate(Object source) {
			return null;
		}

	}

}
