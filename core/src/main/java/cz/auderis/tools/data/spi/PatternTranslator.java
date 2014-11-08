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

import java.util.regex.Pattern;

/**
 * {@code PatternTranslator}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class PatternTranslator extends SingleTargetClassTranslator {

	public PatternTranslator() {
		super(Pattern.class);
	}

	@Override
	public String getId() {
		return "pattern translator";
	}

	@Override
	protected Object translate(Object source) {
		if (source instanceof String) {
			try {
				final Pattern pattern = Pattern.compile((String) source);
				return pattern;
			} catch (Exception e) {
				// Exception consumed
			}
		}
		return null;
	}

}
