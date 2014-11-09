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

import com.google.common.collect.ImmutableMap;
import cz.auderis.tools.data.annotation.ConfigurationEntries;
import cz.auderis.tools.data.annotation.ConfigurationEntryName;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * {@code StringDataObjectTest}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class DataObjectAnnotationTest {

	public static interface DataObject {
		String normalValue();
		@ConfigurationEntryName(name = "renamed.value") String renamedValue();
		@ConfigurationEntryName(name = "") String valueWithEmptyName();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.1.value"}) String aliasedValue();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.x.value", "alias.2.value"}) String aliasedValueB();
	}

	@ConfigurationEntries( /* no prefix specification */ )
	public static interface DataObject2 {
		String normalValue();
		@ConfigurationEntryName(name = "renamed.value") String renamedValue();
		@ConfigurationEntryName(name = "") String valueWithEmptyName();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.1.value"}) String aliasedValue();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.x.value", "alias.2.value"}) String aliasedValueB();
	}

	@ConfigurationEntries(prefix = "")
	public static interface DataObject3 {
		String normalValue();
		@ConfigurationEntryName(name = "renamed.value") String renamedValue();
		@ConfigurationEntryName(name = "") String valueWithEmptyName();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.1.value"}) String aliasedValue();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.x.value", "alias.2.value"}) String aliasedValueB();
	}

	@ConfigurationEntries(prefix = ConfigurationEntries.CLASS_NAME_PREFIX)
	public static interface PrefixedDataObject1 {
		String normalValue2();
		@ConfigurationEntryName(name = "renamed.value") String renamedValue2();
		@ConfigurationEntryName(name = "") String valueWithEmptyName2();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.1.value"}) String aliasedValue2();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.x.value", "alias.2.value"}) String aliasedValueB2();
	}

	@ConfigurationEntries(prefix = "le.prefix")
	public static interface PrefixedDataObject2 {
		String normalValue3();
		@ConfigurationEntryName(name = "renamed.value") String renamedValue3();
		@ConfigurationEntryName(name = "") String valueWithEmptyName3();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.1.value"}) String aliasedValue3();
		@ConfigurationEntryName(name = "nonexistent", alias = {"alias.x.value", "alias.2.value"}) String aliasedValueB3();
	}

	public static interface PrefixOverrideDataObject1 extends PrefixedDataObject1{
		// Empty body, all members inherited
	}

	public static interface AggregateDataObject extends DataObject, PrefixedDataObject2, PrefixOverrideDataObject1 {
		// Empty body, all members inherited
	}

	@Test
	public void shouldCorrectlyReturnNamedEntries() throws Exception {
		final String[][] points = {
				{ "norVal", "renVal", "emNamVal", "alias1", "alias2" },
				{ "1", "2", "3", "4", "5" },
		};
		for (String[] point : points) {
			final Map<String, String> dataSource = ImmutableMap.of(
					"normalValue", point[0],
					"renamed.value", point[1],
					"valueWithEmptyName", point[2],
					"alias.1.value", point[3],
					"alias.2.value", point[4]
			);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final DataObject testObject = ConfigurationData.createConfigurationObject(data, DataObject.class);
			assertEquals(point[0], testObject.normalValue());
			assertEquals(point[1], testObject.renamedValue());
			assertEquals(point[2], testObject.valueWithEmptyName());
			assertEquals(point[3], testObject.aliasedValue());
			assertEquals(point[4], testObject.aliasedValueB());
		}
	}

	@Test
	public void shouldCorrectlyReturnEntriesWithoutPrefix() throws Exception {
		final String[][] points = {
				{ "norVal", "renVal", "emNamVal", "alias1", "alias2" },
				{ "1", "2", "3", "4", "5" },
		};
		for (String[] point : points) {
			final Map<String, String> dataSource = ImmutableMap.of(
					"normalValue", point[0],
					"renamed.value", point[1],
					"valueWithEmptyName", point[2],
					"alias.1.value", point[3],
					"alias.2.value", point[4]
			);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			//
			final DataObject2 testObject2 = ConfigurationData.createConfigurationObject(
					data, DataObject2.class);
			assertEquals(point[0], testObject2.normalValue());
			assertEquals(point[1], testObject2.renamedValue());
			assertEquals(point[2], testObject2.valueWithEmptyName());
			assertEquals(point[3], testObject2.aliasedValue());
			assertEquals(point[4], testObject2.aliasedValueB());
			//
			final DataObject3 testObject3 = ConfigurationData.createConfigurationObject(data, DataObject3.class);
			assertEquals(point[0], testObject3.normalValue());
			assertEquals(point[1], testObject3.renamedValue());
			assertEquals(point[2], testObject3.valueWithEmptyName());
			assertEquals(point[3], testObject3.aliasedValue());
			assertEquals(point[4], testObject3.aliasedValueB());
		}
	}

	@Test
	public void shouldCorrectlyReturnEntriesWithImplicitPrefix() throws Exception {
		final String prefix = "PrefixedDataObject1.";
		final String[][] points = {
				{ "norVal", "renVal", "emNamVal", "alias1", "alias2" },
				{ "1", "2", "3", "4", "5" },
		};
		for (String[] point : points) {
			final Map<String, String> dataSource = ImmutableMap.of(
					prefix + "normalValue2", point[0],
					prefix + "renamed.value", point[1],
					prefix + "valueWithEmptyName2", point[2],
					prefix + "alias.1.value", point[3],
					prefix + "alias.2.value", point[4]
			);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final PrefixedDataObject1 testObject = ConfigurationData.createConfigurationObject(data, PrefixedDataObject1.class);
			assertEquals(point[0], testObject.normalValue2());
			assertEquals(point[1], testObject.renamedValue2());
			assertEquals(point[2], testObject.valueWithEmptyName2());
			assertEquals(point[3], testObject.aliasedValue2());
			assertEquals(point[4], testObject.aliasedValueB2());
		}
	}

	@Test
	public void shouldCorrectlyReturnEntriesWithExplicitPrefix() throws Exception {
		final String prefix = "le.prefix.";
		final String[][] points = {
				{ "norVal", "renVal", "emNamVal", "alias1", "alias2" },
				{ "1", "2", "3", "4", "5" },
		};
		for (String[] point : points) {
			final Map<String, String> dataSource = ImmutableMap.of(
					prefix + "normalValue3", point[0],
					prefix + "renamed.value", point[1],
					prefix + "valueWithEmptyName3", point[2],
					prefix + "alias.1.value", point[3],
					prefix + "alias.2.value", point[4]
			);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final PrefixedDataObject2 testObject = ConfigurationData.createConfigurationObject(data, PrefixedDataObject2.class);
			assertEquals(point[0], testObject.normalValue3());
			assertEquals(point[1], testObject.renamedValue3());
			assertEquals(point[2], testObject.valueWithEmptyName3());
			assertEquals(point[3], testObject.aliasedValue3());
			assertEquals(point[4], testObject.aliasedValueB3());
		}
	}

	@Test
	public void shouldCorrectlyReturnPrefixedEntriesFromChildInterface() throws Exception {
		final String prefix = "PrefixedDataObject1.";
		final String[][] points = {
				{ "norVal", "renVal", "emNamVal", "alias1", "alias2" },
				{ "1", "2", "3", "4", "5" },
		};
		for (String[] point : points) {
			final Map<String, String> dataSource = ImmutableMap.of(
					prefix + "normalValue2", point[0],
					prefix + "renamed.value", point[1],
					prefix + "valueWithEmptyName2", point[2],
					prefix + "alias.1.value", point[3],
					prefix + "alias.2.value", point[4]
			);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final PrefixOverrideDataObject1 testObject = ConfigurationData.createConfigurationObject(data, PrefixOverrideDataObject1.class);
			assertEquals(point[0], testObject.normalValue2());
			assertEquals(point[1], testObject.renamedValue2());
			assertEquals(point[2], testObject.valueWithEmptyName2());
			assertEquals(point[3], testObject.aliasedValue2());
			assertEquals(point[4], testObject.aliasedValueB2());
		}
	}

	@Test
	public void shouldCorrectlyReturnPrefixedEntriesFromAggregateInterface() throws Exception {
		final String prefix1 = "PrefixedDataObject1.";
		final String prefix2 = "le.prefix.";
		final String[][] points = {
				{ "norVal", "renVal", "emNamVal", "alias1", "alias2" },
				{ "1", "2", "3", "4", "5" },
		};
		for (String[] point : points) {
			final ImmutableMap.Builder<String, Object> dataSource = ImmutableMap.builder();
			dataSource.putAll(ImmutableMap.of(
					"normalValue", point[0] + "/noprefix",
					"renamed.value", point[1] + "/noprefix",
					"valueWithEmptyName", point[2] + "/noprefix",
					"alias.1.value", point[3] + "/noprefix",
					"alias.2.value", point[4] + "/noprefix"
			));
			dataSource.putAll(ImmutableMap.of(
					prefix1 + "normalValue2", point[0] + "/" + prefix1,
					prefix1 + "renamed.value", point[1] + "/" + prefix1,
					prefix1 + "valueWithEmptyName2", point[2] + "/" + prefix1,
					prefix1 + "alias.1.value", point[3] + "/" + prefix1,
					prefix1 + "alias.2.value", point[4] + "/" + prefix1
			));
			dataSource.putAll(ImmutableMap.of(
					prefix2 + "normalValue3", point[0] + "/" + prefix2,
					prefix2 + "renamed.value", point[1] + "/" + prefix2,
					prefix2 + "valueWithEmptyName3", point[2] + "/" + prefix2,
					prefix2 + "alias.1.value", point[3] + "/" + prefix2,
					prefix2 + "alias.2.value", point[4] + "/" + prefix2
			));
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource.build());
			final AggregateDataObject testObject = ConfigurationData.createConfigurationObject(data, AggregateDataObject.class);
			//
			assertEquals(point[0] + "/noprefix", testObject.normalValue());
			assertEquals(point[1] + "/noprefix", testObject.renamedValue());
			assertEquals(point[2] + "/noprefix", testObject.valueWithEmptyName());
			assertEquals(point[3] + "/noprefix", testObject.aliasedValue());
			assertEquals(point[4] + "/noprefix", testObject.aliasedValueB());
			//
			assertEquals(point[0] + "/" + prefix1, testObject.normalValue2());
			assertEquals(point[1] + "/" + prefix1, testObject.renamedValue2());
			assertEquals(point[2] + "/" + prefix1, testObject.valueWithEmptyName2());
			assertEquals(point[3] + "/" + prefix1, testObject.aliasedValue2());
			assertEquals(point[4] + "/" + prefix1, testObject.aliasedValueB2());
			//
			assertEquals(point[0] + "/" + prefix2, testObject.normalValue3());
			assertEquals(point[1] + "/" + prefix2, testObject.renamedValue3());
			assertEquals(point[2] + "/" + prefix2, testObject.valueWithEmptyName3());
			assertEquals(point[3] + "/" + prefix2, testObject.aliasedValue3());
			assertEquals(point[4] + "/" + prefix2, testObject.aliasedValueB3());
		}
	}

}
