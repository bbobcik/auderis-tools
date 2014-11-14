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

import java.util.ListResourceBundle;

/**
 * Default localization data for {@link cz.auderis.tools.time.SimpleDurationFormat}.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public final class DurationFormatResource extends ListResourceBundle {

	/**
	 * Localization resource bundle key used to obtain separator
	 * of duration parts. Default: a single space ({@literal \u0020})
	 */
	public static final String PART_SEPARATOR_KEY = "PART_SEPARATOR";
	/**
	 * Localization resource bundle key used to obtain representation
	 * of zero-length duration.
	 * <p/>
	 * In property files, it should be declared as {@code 0\u0020}seconds}.
	 *
	 */
	public static final String ZERO_TIME_KEY = "0 seconds";


	private static final String[][] LOCALIZATION = {
			{ PART_SEPARATOR_KEY, " " },
			{ ZERO_TIME_KEY, "0 seconds" },
			{ "days", "{0,choice,0#0 days|1#1 day|1<{0} days}" },
			{ "hours", "{0,choice,0#0 hours|1#1 hour|1<{0} hours}" },
			{ "minutes", "{0,choice,0#0 minutes|1#1 minute|1<{0} minutes}" },
			{ "seconds", "{0,choice,0#0 seconds|1#1 second|1<{0} seconds}" },
	};

	@Override
	protected Object[][] getContents() {
		return LOCALIZATION;
	}

}
