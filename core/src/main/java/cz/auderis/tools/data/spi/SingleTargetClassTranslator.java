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

package cz.auderis.tools.data.spi;

import cz.auderis.tools.data.DataTranslator;

import java.lang.reflect.AnnotatedElement;

/**
 * {@code SingleTargetClassTranslator}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public abstract class SingleTargetClassTranslator implements DataTranslator {

	protected final Class supportedClass;

	protected SingleTargetClassTranslator(Class supportedClass) {
		this.supportedClass = supportedClass;
	}

	protected abstract Object translate(Object source);

	@SuppressWarnings("unchecked")
	@Override
	public int getTargetClassSupportPriority(Class targetClass) {
		if (null == targetClass) {
			throw new NullPointerException();
		} else if (supportedClass.isAssignableFrom(targetClass)) {
			return PRIORITY_NORMAL_SUPPORT;
		}
		return PRIORITY_NOT_SUPPORTED;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object translateToClass(Object source, Class targetClass, AnnotatedElement context, Object[] args) {
		if (null == targetClass) {
			throw new NullPointerException();
		} else if (!supportedClass.isAssignableFrom(targetClass)) {
			throw new IllegalArgumentException("target class not supported by " + getId());
		} else if ((null == source) || supportedClass.isAssignableFrom(source.getClass())) {
			return source;
		}
		final Object result = translate(source);
		return result;
	}

	@Override
	public String toString() {
		return getId();
	}

}
