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

package cz.auderis.tools.math;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class RoundTest {

	@Test
	public void shouldFloorIntegralBigDecimals() throws Exception {
		for (long x = -1000L; x <= 1000L; ++x) {
			final BigDecimal input = BigDecimal.valueOf(x);
			final BigDecimal result = Round.floor(input);
			assertEquals(0, result.compareTo(input));
		}
	}

	@Test
	public void shouldFloorPositiveBigDecimals() throws Exception {
		final String[][] dataPoints = {
				{ "1.1", "1" },
				{ "1.3", "1" },
				{ "1.499999", "1" },
				{ "1.5", "1" },
				{ "1.500001", "1" },
				{ "1.7", "1" },
				{ "1.9999999999999999999999999", "1" },
				{ "2.0000000000000000000000001", "2" },
		};
		for (String[] dataPoint : dataPoints) {
			final BigDecimal value = new BigDecimal(dataPoint[0]);
			final BigDecimal expected = new BigDecimal(dataPoint[1]);
			final BigDecimal result = Round.floor(value);
			assertEquals("floor(" + value + ")==" + expected, 0, result.compareTo(expected));
		}
	}

	@Test
	public void shouldFloorNegativeBigDecimals() throws Exception {
		final String[][] dataPoints = {
				{ "-1.1", "-2" },
				{ "-1.3", "-2" },
				{ "-1.499999", "-2" },
				{ "-1.5", "-2" },
				{ "-1.500001", "-2" },
				{ "-1.7", "-2" },
				{ "-1.9999999999999999999999999", "-2" },
				{ "-2.0000000000000000000000001", "-3" },
		};
		for (String[] dataPoint : dataPoints) {
			final BigDecimal value = new BigDecimal(dataPoint[0]);
			final BigDecimal expected = new BigDecimal(dataPoint[1]);
			final BigDecimal result = Round.floor(value);
			assertEquals("floor(" + value + ")==" + expected, 0, result.compareTo(expected));
		}
	}

}