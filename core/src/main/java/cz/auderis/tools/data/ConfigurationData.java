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

import cz.auderis.tools.data.spi.CompoundDataProvider;
import cz.auderis.tools.data.spi.MapBasedDataProvider;
import cz.auderis.tools.data.spi.SystemPropertyDataProvider;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * {@code ConfigurationData}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class ConfigurationData {

	public static ConfigurationDataProvider getSystemPropertyDataProvider() {
		return SystemPropertyDataProvider.instance();
	}

	public static ConfigurationDataProvider getMapDataProvider(Map<String, ?> dataSource) {
		return new MapBasedDataProvider(dataSource);
	}

	public static ConfigurationDataProvider getCompoundDataProvider(ConfigurationDataProvider... providers) {
		return new CompoundDataProvider(providers);
	}

	@SuppressWarnings("unchecked")
	public static <T> T createConfigurationObject(ConfigurationDataProvider dataProvider, Class<T> targetClass, ClassLoader clsLoader) {
		if ((null == dataProvider) || (null == targetClass)) {
			throw new NullPointerException();
		}
		final Class<?>[] interfaces = { targetClass };
		final ConfigurationDataAccessProxyHandler proxyHandler = new ConfigurationDataAccessProxyHandler(dataProvider);
		final T proxy = (T) Proxy.newProxyInstance(clsLoader, interfaces, proxyHandler);
		return proxy;
	}

	public static <T> T createConfigurationObject(ConfigurationDataProvider dataProvider, Class<T> targetClass) {
		return createConfigurationObject(dataProvider, targetClass, null);
	}

}
