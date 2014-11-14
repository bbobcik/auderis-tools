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

package cz.auderis.tools.config;

import cz.auderis.tools.config.annotation.ConfigurationEntryName;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * {@code ConfigurationEntryNamingTest}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class ConfigurationEntryNamingTest {

	public static interface TestDataObject {
		// Getter names
		String getName();
		boolean isEnabled();
		int getCount();

		// Plain names
		String getterName();
		boolean isystemActive();
		int isDelta();
		Boolean isArmed();

		// Override
		@ConfigurationEntryName(name = "getId") String getId();
	}

	@Test
	public void shouldCorrectlyResolveGetterNames() throws Exception {
		Object[][] dataPoints = {
				{ "a", false, 5 },
				{ "x", true, 10 },
		};
		for (Object[] dataPoint : dataPoints) {
			Map<String, Object> dataSource = new HashMap<String, Object>();
			dataSource.put("name", dataPoint[0]);
			dataSource.put("enabled", dataPoint[1]);
			dataSource.put("count", dataPoint[2]);
			dataSource.put("getName", "X" + dataPoint[0]);
			dataSource.put("isEnabled", !((Boolean) dataPoint[1]));
			dataSource.put("getCount", 1+((Integer) dataPoint[2]));
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final TestDataObject testObject = ConfigurationData.createConfigurationObject(data, TestDataObject.class);
			assertEquals(dataPoint[0], testObject.getName());
			assertEquals((boolean) (Boolean) dataPoint[1], testObject.isEnabled());
			assertEquals((int) (Integer) dataPoint[2], testObject.getCount());
		}
	}

	@Test
	public void shouldCorrectlyResolveNonGetterNames() throws Exception {
		Object[][] dataPoints = {
				{ "a", false, 5, false, "9988" },
				{ "x", true, 10, true, "abcd" },
		};
		for (Object[] dataPoint : dataPoints) {
			Map<String, Object> dataSource = new HashMap<String, Object>();
			dataSource.put("getterName", dataPoint[0]);
			dataSource.put("isystemActive", dataPoint[1]);
			dataSource.put("isDelta", dataPoint[2]);
			dataSource.put("armed", dataPoint[3]);
			dataSource.put("getId", dataPoint[4]);
			dataSource.put("id", "XXX" + dataPoint[4]);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final TestDataObject testObject = ConfigurationData.createConfigurationObject(data, TestDataObject.class);
			assertEquals(dataPoint[0], testObject.getterName());
			assertEquals((boolean) (Boolean) dataPoint[1], testObject.isystemActive());
			assertEquals((int) (Integer) dataPoint[2], testObject.isDelta());
			assertNull(testObject.isArmed());
			assertEquals(dataPoint[4], testObject.getId());
		}
	}

}
