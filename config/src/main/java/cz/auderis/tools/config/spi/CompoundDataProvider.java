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

package cz.auderis.tools.config.spi;

import cz.auderis.tools.config.ConfigurationDataProvider;

import java.util.Arrays;

/**
 * {@code CascadingDataProvider}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class CompoundDataProvider implements ConfigurationDataProvider {

	private final ConfigurationDataProvider[] delegates;

	public CompoundDataProvider(ConfigurationDataProvider... providers) {
		if (null == providers) {
			throw new NullPointerException();
		} else if (0 == providers.length) {
			throw new IllegalArgumentException("no providers were specified");
		}
		for (ConfigurationDataProvider p : providers) {
			if (null == p) {
				throw new IllegalArgumentException("null provider specified");
			}
		}
		this.delegates = Arrays.copyOf(providers, providers.length);
	}


	@Override
	public boolean containsKey(String key) {
		if (null == key) {
			throw new NullPointerException();
		}
		for (ConfigurationDataProvider delegate : delegates) {
			if (delegate.containsKey(key)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object getRawObject(String key) {
		if (null == key) {
			throw new NullPointerException();
		}
		for (ConfigurationDataProvider delegate : delegates) {
			if (delegate.containsKey(key)) {
				return delegate.getRawObject(key);
			}
		}
		return null;
	}

}
