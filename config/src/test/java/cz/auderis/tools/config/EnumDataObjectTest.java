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

import com.google.common.collect.ImmutableMap;
import cz.auderis.tools.config.annotation.DefaultConfigurationEntryValue;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * {@code StringDataObjectTest}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class EnumDataObjectTest {

	public static enum SimpleEnum {
		ONE, TWO, THREE
	}

	public static enum ComplexEnum {
		IDLE(0) {
			@Override public String getId() { return "Idle"; }
		},

		BUSY(10) {
			@Override public String getId() { return "Working"; }
		},

		ERROR(-1) {
			@Override public String getId() { return "Problem"; }
		};

		private final int x;

		private ComplexEnum(int x) {
			this.x = x;
		}

		public int getLevel() {
			return x;
		}

		public abstract String getId();
	}


	public static interface EnumDataObject {

		SimpleEnum basicValue();
		SimpleEnum basicValueFromText();

		@DefaultConfigurationEntryValue("THREE")
		SimpleEnum basicValueWithDefault();

		ComplexEnum complexValue();
		ComplexEnum complexValueFromText();

		@DefaultConfigurationEntryValue("BUSY")
		ComplexEnum complexValueWithDefault();

		TimeUnit systemEnumValue();
		TimeUnit systemEnumValueFromText();

		@DefaultConfigurationEntryValue("HOURS")
		TimeUnit systemEnumValueWithDefault();

	}


	@Test
	public void shouldCorrectlyReturnSimpleEnum() throws Exception {
		final SimpleEnum[] points = SimpleEnum.values();
		for (SimpleEnum point : points) {
			final Map<String, ?> dataSource = ImmutableMap.of(
					"basicValue", point,
					"basicValueFromText", point.name()
			);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final EnumDataObject testObject = ConfigurationData.createConfigurationObject(data, EnumDataObject.class);
			assertEquals(point, testObject.basicValue());
			assertEquals(point, testObject.basicValueFromText());
		}
	}

	@Test
	public void shouldCorrectlyReturnComplexEnum() throws Exception {
		final ComplexEnum[] points = ComplexEnum.values();
		for (ComplexEnum point : points) {
			final Map<String, ?> dataSource = ImmutableMap.of(
					"complexValue", point,
					"complexValueFromText", point.name()
			);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final EnumDataObject testObject = ConfigurationData.createConfigurationObject(data, EnumDataObject.class);
			assertEquals(point, testObject.complexValue());
			assertEquals(point, testObject.complexValueFromText());
		}
	}

	@Test
	public void shouldCorrectlyReturnSystemEnum() throws Exception {
		final TimeUnit[] points = TimeUnit.values();
		for (TimeUnit point : points) {
			final Map<String, ?> dataSource = ImmutableMap.of(
					"systemEnumValue", point,
					"systemEnumValueFromText", point.name()
			);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final EnumDataObject testObject = ConfigurationData.createConfigurationObject(data, EnumDataObject.class);
			assertEquals(point, testObject.systemEnumValue());
			assertEquals(point, testObject.systemEnumValueFromText());
		}
	}

	@Test
	public void shouldReturnAnnotatedDefaults() throws Exception {
		final ConfigurationDataProvider noData = ConfigurationData.getMapDataProvider(Collections.<String, Object>emptyMap());
		final EnumDataObject testObject = ConfigurationData.createConfigurationObject(noData, EnumDataObject.class);
		assertEquals(SimpleEnum.THREE, testObject.basicValueWithDefault());
		assertEquals(ComplexEnum.BUSY, testObject.complexValueWithDefault());
		assertEquals(TimeUnit.HOURS, testObject.systemEnumValueWithDefault());
	}

}
