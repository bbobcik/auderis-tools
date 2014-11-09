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

import cz.auderis.tools.data.DataTranslatorContext;

import java.net.InetAddress;

/**
 * {@code InetAddressTranslator}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class InetAddressTranslator extends SingleTargetClassTranslator {

	public InetAddressTranslator() {
		super(InetAddress.class);
	}

	@Override
	public String getId() {
		return "inet address translator";
	}

	@Override
	protected Object translate(Object source, DataTranslatorContext context) {
		if (source instanceof String) {
			final String textAddr = (String) source;
			try {
				final InetAddress result = InetAddress.getByName(textAddr);
				return result;
			} catch (Exception e) {
				// Exception consumed
			}
		}
		return null;
	}

}
