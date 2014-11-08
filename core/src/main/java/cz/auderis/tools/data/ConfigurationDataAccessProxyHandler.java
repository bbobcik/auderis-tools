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

import cz.auderis.tools.data.annotation.ConfigurationEntryName;
import cz.auderis.tools.data.annotation.ConfigurationEntryPrefix;
import cz.auderis.tools.data.annotation.DefaultConfigurationEntryValue;

import java.lang.ref.SoftReference;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * {@code ResourceProxyHandler}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class ConfigurationDataAccessProxyHandler implements InvocationHandler {

	private static final String GETTER_PREFIX = "get";
	private static final Object NULL_CACHE_ENTRY = new Object();

	private final ConfigurationDataProvider dataProvider;
	private final ConcurrentMap<Method, SoftReference<Object>> cache;

	public ConfigurationDataAccessProxyHandler(ConfigurationDataProvider dataProvider) {
		if (null == dataProvider) {
			throw new NullPointerException();
		}
		this.dataProvider = dataProvider;
		this.cache = new ConcurrentHashMap<Method, SoftReference<Object>>();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// Try to reuse cached value
		if ((null == args) && cache.containsKey(method)) {
			final SoftReference<Object> resultRef = cache.get(method);
			final Object result = resultRef.get();
			if (result == NULL_CACHE_ENTRY) {
				return null;
			} else if (null != result) {
				return result;
			}
			cache.remove(method, resultRef);
		}
		// Compute result
		final String keyName = getResourceKey(method);
		final Object sourceValue;
		if (dataProvider.containsKey(keyName)) {
			sourceValue = dataProvider.getRawObject(keyName);
		} else {
			sourceValue = getDefaultSourceValue(method);
		}
		final Object result = translateObject(sourceValue, method, args);
		// Cache result (only for no-argument methods)
		if (null == args) {
			final Object cachedValue = (null != result) ? result : NULL_CACHE_ENTRY;
			cache.put(method, new SoftReference<Object>(cachedValue));
		}
		return result;
	}

	private Object getDefaultSourceValue(Method method) {
		final DefaultConfigurationEntryValue defaultValAnnotation = method.getAnnotation(DefaultConfigurationEntryValue.class);
		String result = (null != defaultValAnnotation) ? defaultValAnnotation.value() : null;
		if ((null != result) && result.isEmpty()) {
			result = null;
		}
		return result;
	}

	private Object translateObject(Object sourceValue, Method method, Object[] args) {
		final Class<?> returnType = method.getReturnType();
		if (String.class == returnType) {
			final String textResult = translateToString(sourceValue, args);
			return textResult;
		}
		final StandardJavaTranslator standardTranslator = StandardJavaTranslator.instance();
		if (standardTranslator.isPrimitiveOrBoxed(returnType)) {
			final Object primitiveResult = standardTranslator.translatePrimitive(sourceValue, returnType);
			return primitiveResult;
		}
		if (returnType.isEnum()) {
			final Object enumResult = standardTranslator.translateEnum(sourceValue, returnType);
			return enumResult;
		}
		final Object pluginResult = tryPluginTranslator(sourceValue, returnType, method, args);
		if (null != pluginResult) {
			return pluginResult;
		}
		final Object constructedResult = tryConstruct(sourceValue, returnType, args);
		if (null != constructedResult) {
			return constructedResult;
		}
		// Try special cases
		if (null != sourceValue) {
			if ((Class.class == returnType) && (sourceValue instanceof String)) {
				try {
					final Class<?> classResult = Class.forName((String) sourceValue);
					return classResult;
				} catch (ClassNotFoundException e) {
					// Exception ignored
				}
			}
		}
		return null;
	}

	private String translateToString(Object sourceValue, Object[] args) {
		if (null == sourceValue) {
			return "";
		} else if (sourceValue instanceof String) {
			if (null != args) {
				try {
					return MessageFormat.format((String) sourceValue, args);
				} catch (Exception e) {
					// Fall through
				}
			}
			return (String) sourceValue;
		}
		return sourceValue.toString();
	}

	private Object tryPluginTranslator(Object sourceValue, Class targetClass, AnnotatedElement context, Object[] args) {
		final ServiceLoader<DataTranslator> translators = ServiceLoader.load(DataTranslator.class);
		final Iterator<DataTranslator> translatorIterator = translators.iterator();
		DataTranslator selectedTranslator = null;
		int selectedPriority = DataTranslator.PRIORITY_NOT_SUPPORTED;
		while (true) {
			boolean hasNext;
			try {
				hasNext = translatorIterator.hasNext();
			} catch (ServiceConfigurationError e) {
				hasNext = false;
			}
			if (!hasNext) {
				break;
			}
			try {
				final DataTranslator translator = translatorIterator.next();
				final int supportPriority = translator.getTargetClassSupportPriority(targetClass);
				if (supportPriority > selectedPriority) {
					selectedPriority = supportPriority;
					selectedTranslator = translator;
				}
			} catch (Exception e) {
				// Silently ignored
			}
		}
		if (null != selectedTranslator) {
			try {
				final Object result = selectedTranslator.translateToClass(sourceValue, targetClass, context, args);
				return result;
			} catch (Exception e) {
				// Silently ignored
			}
		}
		return null;
	}

	private Object tryConstruct(Object sourceValue, Class<?> returnType, Object[] args) {
		if (null == sourceValue) {
			return null;
		}
		try {
			if (null == args) {
				final Constructor<?> constructor = returnType.getConstructor(sourceValue.getClass());
				final Object result = constructor.newInstance(sourceValue);
				return result;
			}
			// More complex case - append method arguments
			final Class[] argumentTypes = new Class[1 + args.length];
			argumentTypes[0] = sourceValue.getClass();
			for (int i=0; i<args.length; ++i) {
				final Object arg = args[i];
				final Class argClass;
				if (null == arg) {
					argClass = null;
				} else {
					argClass = arg.getClass();
				}
				argumentTypes[i + 1] = argClass;
			}
			// Enumerate all constructors and try to find the one matching the argument list
			final Constructor<?>[] constructors = returnType.getConstructors();
			for (Constructor<?> constructor : constructors) {
				final Class<?>[] parameterTypes = constructor.getParameterTypes();
				final boolean paramMatch = matchParameters(parameterTypes, argumentTypes);
				if (paramMatch) {
					final Object[] extArgs = new Object[1 + args.length];
					extArgs[0] = sourceValue;
					for (int i=0; i<args.length; ++i) {
						extArgs[i + 1] = args[0];
					}
					final Object result = constructor.newInstance(extArgs);
					return result;
				}
			}
		} catch (Exception e) {
			// Silently ignored
		}
		return null;
	}

	private boolean matchParameters(Class<?>[] parameterTypes, Class[] argumentTypes) {
		if (parameterTypes.length != argumentTypes.length) {
			return false;
		}
		for (int i=0; i<parameterTypes.length; ++i) {
			final Class<?> paramType = parameterTypes[i];
			final Class<?> argType = argumentTypes[i];
			if (null == argType) {
				// Wildcard - null can be used for all non-primitive types
				if (paramType.isPrimitive()) {
					return false;
				}
			} else if (!paramType.isAssignableFrom(argType)) {
				return false;
			}
		}
		return true;
	}

	private String getResourceKey(Method method) {
		final String keyPrefix = getResourceKeyPrefix(method);
		final ConfigurationEntryName nameAnnotation = method.getAnnotation(ConfigurationEntryName.class);
		if (null != nameAnnotation) {
			final String keyName = nameAnnotation.name();
			final boolean keyNameDefined = (null != keyName) && !keyName.isEmpty();
			if (keyNameDefined && dataProvider.containsKey(keyPrefix + keyName)) {
				return keyPrefix + keyName;
			}
			final String[] keyAliases = nameAnnotation.alias();
			if ((null != keyAliases) && (0 != keyAliases.length)) {
				// Some aliases are defined
				for (String alias : keyAliases) {
					if ((null != alias) && !alias.isEmpty() && dataProvider.containsKey(keyPrefix + alias)) {
						return keyPrefix + alias;
					}
				}
			}
			// If the explicit name was defined, use it in all cases (i.e. don't use the implicit name)
			if (keyNameDefined) {
				return keyPrefix + keyName;
			}
		}
		// There is no annotation present or the name is empty, derive name from method
		final String methodName = method.getName();
		final String keyName = trimOptionalGetterPrefix(methodName);
		return keyPrefix + keyName;
	}

	private String getResourceKeyPrefix(Method method) {
		final Class<?> declaringClass = method.getDeclaringClass();
		final ConfigurationEntryPrefix prefixAnnotation = declaringClass.getAnnotation(ConfigurationEntryPrefix.class);
		if (null == prefixAnnotation) {
			return "";
		}
		final String explicitPrefix = prefixAnnotation.value();
		StringBuilder resultPrefix = new StringBuilder();
		if ((null == explicitPrefix) || explicitPrefix.trim().isEmpty()) {
			resultPrefix.append(declaringClass.getSimpleName());
		} else {
			resultPrefix.append(explicitPrefix);
		}
		if (resultPrefix.charAt(resultPrefix.length() - 1) != '.') {
			resultPrefix.append('.');
		}
		return resultPrefix.toString();
	}

	private String trimOptionalGetterPrefix(String methodName) {
		if (!methodName.startsWith(GETTER_PREFIX)) {
			return methodName;
		}
		final String propertyName = methodName.substring(GETTER_PREFIX.length());
		final char firstChar = propertyName.charAt(0);
		if (!Character.isUpperCase(firstChar)) {
			// This is probably not a getter
			return methodName;
		}
		return Character.toLowerCase(firstChar) + propertyName.substring(1);
	}

}
