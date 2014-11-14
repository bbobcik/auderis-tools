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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class SimpleDurationFormatTest {

	private Locale originalDefaultLocale;

	@Before
	public void saveDefaultLocale() {
		originalDefaultLocale = Locale.getDefault();
	}

	@After
	public void restoreDefaultLocale() {
		Locale.setDefault(originalDefaultLocale);
	}

	@Test
	public void shouldCorrectlyFormatSinglePartDurations() throws Exception {
		final SimpleDurationFormat fmt = SimpleDurationFormat.getInstance(Locale.ENGLISH);
		assertEquals("0 seconds", fmt.toHumanDuration(0L));
		assertEquals("1 second", fmt.toHumanDuration(1L, TimeUnit.SECONDS));
		for (int sec = 2; sec < 120; ++sec) {
			assertEquals(sec + " seconds", fmt.toHumanDuration(sec, TimeUnit.SECONDS));
		}
		for (int min = 2; min < 120; ++min) {
			assertEquals(min + " minutes", fmt.toHumanDuration(min, TimeUnit.MINUTES));
		}
		for (int hr = 2; hr < 48; ++hr) {
			assertEquals(hr + " hours", fmt.toHumanDuration(hr, TimeUnit.HOURS));
		}
		for (int dd = 2; dd < 1000; ++dd) {
			assertEquals(dd + " days", fmt.toHumanDuration(dd, TimeUnit.DAYS));
		}
	}

	@Test
	public void shouldFormatSubsecondDurationAsZero() throws Exception {
		final SimpleDurationFormat fmt = SimpleDurationFormat.getInstance(Locale.ENGLISH);
		for (long ms = 0L; ms < 1000L; ++ms) {
			assertEquals("0 seconds", fmt.toHumanDuration(ms, TimeUnit.MILLISECONDS));
		}
	}

	@Test
	public void shouldCorrectlyFormatDurationsInCzechLocale() throws Exception {
		final Locale czechLocale = Locale.forLanguageTag("cs");
		final SimpleDurationFormat fmt = SimpleDurationFormat.getInstance(czechLocale);
		assertEquals("0 sekund", fmt.toHumanDuration(0L));
		assertEquals("1 sekunda", fmt.toHumanDuration(1L, TimeUnit.SECONDS));
		assertEquals("2 sekundy", fmt.toHumanDuration(2L, TimeUnit.SECONDS));
		assertEquals("3 sekundy", fmt.toHumanDuration(3L, TimeUnit.SECONDS));
		assertEquals("4 sekundy", fmt.toHumanDuration(4L, TimeUnit.SECONDS));
		assertEquals("5 sekund", fmt.toHumanDuration(5L, TimeUnit.SECONDS));
		assertEquals("10 sekund", fmt.toHumanDuration(10L, TimeUnit.SECONDS));
		assertEquals("2 minuty 15 sekund", fmt.toHumanDuration(135L, TimeUnit.SECONDS));
		assertEquals("5 hodin 20 minut 59 sekund", fmt.toHumanDuration(19259L, TimeUnit.SECONDS));
		assertEquals("3 hodiny 0 minut 1 sekunda", fmt.toHumanDuration(10801L, TimeUnit.SECONDS));
	}

	@Test
	public void shouldFormatNeutralLocaleAsEnglish() throws Exception {
		// Define non-english default locale
		final Locale czechLocale = Locale.forLanguageTag("cs");
		Locale.setDefault(czechLocale);
		final SimpleDurationFormat enFmt = SimpleDurationFormat.getInstance(Locale.ENGLISH);
		final SimpleDurationFormat neutralFmt = SimpleDurationFormat.getLanguageNeutralInstance();
		for (long sec = 0L; sec < 13457L; sec += 41) {
			String enText = enFmt.toHumanDuration(sec, TimeUnit.SECONDS);
			String neutralText = neutralFmt.toHumanDuration(sec, TimeUnit.SECONDS);
			assertEquals(enText, neutralText);
		}
	}

	@Test
	public void shouldCorrectlyAppendToStringBuilder() throws Exception {
		final SimpleDurationFormat fmt = SimpleDurationFormat.getInstance(Locale.ENGLISH);
		final StringBuilder accum = new StringBuilder("#");
		fmt.toHumanDuration(1L, TimeUnit.SECONDS, accum);
		accum.append("/");
		fmt.toHumanDuration(183L, TimeUnit.SECONDS, accum);
		accum.append(".");
		assertEquals("#1 second/3 minutes 3 seconds.", accum.toString());
	}

}