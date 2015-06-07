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
import cz.auderis.tools.config.annotation.ConfigurationEntryName;
import cz.auderis.tools.config.annotation.DefaultConfigurationEntryValue;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PrimitiveDataObjectTest {


	public static interface PrimitiveDataObject {

		@ConfigurationEntryName(name = "boolItem")
		Boolean boolItemObj();
		boolean boolItem();

		@ConfigurationEntryName(name = "byteItem")
		Byte byteItemObj();
		byte byteItem();

		@ConfigurationEntryName(name = "shortItem")
		Short shortItemObj();
		short shortItem();

		@ConfigurationEntryName(name = "intItem")
		Integer intItemObj();
		int intItem();

		@ConfigurationEntryName(name = "longItem")
		Long longItemObj();
		long longItem();

		@ConfigurationEntryName(name = "floatItem")
		Float floatItemObj();
		float floatItem();

		@ConfigurationEntryName(name = "doubleItem")
		Double doubleItemObj();
		double doubleItem();
	}

	public static interface PrimitiveDataObjectWithDefaults {
		@DefaultConfigurationEntryValue("true") boolean boolItem();
		@DefaultConfigurationEntryValue("12") byte byteItem();
		@DefaultConfigurationEntryValue("123") short shortItem();
		@DefaultConfigurationEntryValue("1234") int intItem();
		@DefaultConfigurationEntryValue("12345") long longItem();
		@DefaultConfigurationEntryValue("123.45") float floatItem();
		@DefaultConfigurationEntryValue("12345.6789") double doubleItem();
	}


	private static final double DELTA_DOUBLE = 0.00000001D;
	private static final float DELTA_FLOAT   = 0.00000001F;


	@Test
	public void shouldReturnCorrectBoolean() throws Exception {
		final boolean[] points = {true, false};
		for (boolean point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("boolItem", point);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue(point == testObject.boolItem());
			assertTrue(point == testObject.boolItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectBooleanFromText() throws Exception {
		final boolean[] points = {true, false};
		for (boolean point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("boolItem", Boolean.toString(point));
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue(point == testObject.boolItem());
			assertTrue(point == testObject.boolItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectByte() throws Exception {
		final int[] points = {0, 1, -1, Byte.MIN_VALUE, Byte.MAX_VALUE};
		for (int point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("byteItem", (byte) point);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(
					data, PrimitiveDataObject.class, getClass().getClassLoader()
			);
			assertTrue((byte) point == testObject.byteItem());
			assertTrue((byte) point == testObject.byteItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectByteFromText() throws Exception {
		final int[] points = {0, 1, -1, Byte.MIN_VALUE, Byte.MAX_VALUE};
		for (int point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("byteItem", Byte.toString((byte) point));
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue((byte) point == testObject.byteItem());
			assertTrue((byte) point == testObject.byteItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectShort() throws Exception {
		final int[] points = {0, 1, -1, Byte.MIN_VALUE, Byte.MAX_VALUE};
		for (int point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("shortItem", (short) point);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue((short) point == testObject.shortItem());
			assertTrue((short) point == testObject.shortItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectShortFromText() throws Exception {
		final int[] points = {0, 1, -1, Byte.MIN_VALUE, Byte.MAX_VALUE};
		for (int point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("shortItem", Short.toString((short) point));
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue((short) point == testObject.shortItem());
			assertTrue((short) point == testObject.shortItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectInt() throws Exception {
		final int[] points = {0, 1, -1, 256, -256, Integer.MIN_VALUE, Integer.MAX_VALUE};
		for (int point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("intItem", point);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue(point == testObject.intItem());
			assertTrue(point == testObject.intItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectIntFromText() throws Exception {
		final int[] points = {0, 1, -1, 256, -256, Integer.MIN_VALUE, Integer.MAX_VALUE};
		for (int point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("intItem", Integer.toString(point));
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue(point == testObject.intItem());
			assertTrue(point == testObject.intItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectLong() throws Exception {
		final long[] points = {0L, 1L, -1L, 256L, -256L, Long.MIN_VALUE, Long.MAX_VALUE};
		for (long point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("longItem", point);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue(point == testObject.longItem());
			assertTrue(point == testObject.longItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectLongFromText() throws Exception {
		final long[] points = {0L, 1L, -1L, 256L, -256L, Long.MIN_VALUE, Long.MAX_VALUE};
		for (long point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("longItem", Long.toString(point));
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue(point == testObject.longItem());
			assertTrue(point == testObject.longItemObj());
		}
	}

	@Test
	public void shouldReturnCorrectDouble() throws Exception {
		final double[] points = {0.0D, 1.0D, -1.0D, Math.PI, -Math.PI, Double.MAX_VALUE, Double.MIN_VALUE};
		for (double point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("doubleItem", point);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue(Double.doubleToLongBits(point) == Double.doubleToLongBits(testObject.doubleItem()));
			assertTrue(Double.doubleToLongBits(point) == Double.doubleToLongBits(testObject.doubleItemObj()));
		}
	}

	@Test
	public void shouldReturnCorrectDoubleFromText() throws Exception {
		final double[] points = {0.0D, 1.0D, -1.0D, Math.PI, -Math.PI, Double.MAX_VALUE, Double.MIN_VALUE};
		for (double point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("doubleItem", Double.toString(point));
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertEquals(point, testObject.doubleItem(), DELTA_DOUBLE);
			assertEquals(point, testObject.doubleItemObj(), DELTA_DOUBLE);
		}
	}

	@Test
	public void shouldReturnCorrectFloat() throws Exception {
		final float[] points = {0.0F, 1.0F, -1.0F, (float) Math.PI, (float) -Math.PI, Float.MAX_VALUE, Float.MIN_VALUE};
		for (float point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("floatItem", point);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertTrue(Float.floatToIntBits(point) == Float.floatToIntBits(testObject.floatItem()));
			assertTrue(Float.floatToIntBits(point) == Float.floatToIntBits(testObject.floatItemObj()));
		}
	}

	@Test
	public void shouldReturnCorrectFloatFromText() throws Exception {
		final float[] points = {0.0F, 1.0F, -1.0F, (float) Math.PI, (float) -Math.PI, Float.MAX_VALUE, Float.MIN_VALUE};
		for (float point : points) {
			final ImmutableMap<String, ?> dataMap = ImmutableMap.of("floatItem", Float.toString(point));
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataMap);
			final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
			assertEquals(point, testObject.floatItem(), DELTA_FLOAT);
			assertEquals(point, testObject.floatItemObj(), DELTA_FLOAT);
		}
	}

	@Test
	public void shouldReturnDefaultsForUndefinedKeys() throws Exception {
		final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(Collections.<String, Object>emptyMap());
		final PrimitiveDataObject testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObject.class);
		assertTrue(false == testObject.boolItem());
		assertTrue(null == testObject.boolItemObj());
		//
		assertTrue((byte) 0 == testObject.byteItem());
		assertTrue(null == testObject.byteItemObj());
		//
		assertTrue((short) 0 == testObject.shortItem());
		assertTrue(null == testObject.shortItemObj());
		//
		assertTrue(0 == testObject.intItem());
		assertTrue(null == testObject.intItemObj());
		//
		assertTrue(0L == testObject.longItem());
		assertTrue(null == testObject.longItemObj());
		//
		assertTrue(0F == testObject.floatItem());
		assertTrue(null == testObject.floatItemObj());
		//
		assertTrue(0D == testObject.doubleItem());
		assertTrue(null == testObject.doubleItemObj());
	}

	@Test
	public void shouldReturnAnnotatedDefaultsForUndefinedKeys() throws Exception {
		final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(Collections.<String, Object>emptyMap());
		final PrimitiveDataObjectWithDefaults testObject = ConfigurationData.createConfigurationObject(data, PrimitiveDataObjectWithDefaults.class);
		assertTrue(true == testObject.boolItem());
		//
		assertTrue((byte) 12 == testObject.byteItem());
		assertTrue((short) 123 == testObject.shortItem());
		assertTrue(1234 == testObject.intItem());
		assertTrue(12345L == testObject.longItem());
		//
		assertEquals(123.45F, testObject.floatItem(), DELTA_FLOAT);
		assertEquals(12345.6789D, testObject.doubleItem(), DELTA_DOUBLE);
	}

}