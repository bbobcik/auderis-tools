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
import cz.auderis.tools.data.annotation.ConfigurationEntryName;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * {@code StringDataObjectTest}
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class PluginDataObjectTest {

	public static interface CorePluginDataObject {
		Pattern thePattern();
		InetAddress inetAddress();
		@ConfigurationEntryName(name = "addr.ipv4") Inet4Address inetAddressIPv4();
		@ConfigurationEntryName(name = "addr.ipv6") Inet6Address inetAddressIPv6();
		UUID theUUID();
	}

	public static interface MultiPluginDataObject {
		TestClassA resultA();
		TestClassB resultB();
	}


	@Test
	public void shouldCorrectlyReturnValueViaPlugin() throws Exception {
		final Object[][] points = {
				{ "a\\[\\d{3}\\]", "8.8.8.8", "8.4.8.4", "2001:db8:85a3:0:0:8a2e:370:7334", "f669dd2a-6722-11e4-b116-123b93f75cba", "xa[001]x" },
		};
		for (Object[] point : points) {
			final Map<String, ?> dataSource = ImmutableMap.of(
					"thePattern", point[0],
					"inetAddress", point[1],
					"addr.ipv4", point[2],
					"addr.ipv6", point[3],
					"theUUID", point[4]
			);
			final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
			final CorePluginDataObject testObject = ConfigurationData.createConfigurationObject(data, CorePluginDataObject.class);
			assertTrue(testObject.thePattern() instanceof Pattern);
			assertTrue(testObject.inetAddress() instanceof InetAddress);
			assertTrue(testObject.inetAddressIPv4() instanceof InetAddress);
			assertTrue(testObject.inetAddressIPv4() instanceof Inet4Address);
			assertTrue(testObject.inetAddressIPv6() instanceof InetAddress);
			assertTrue(testObject.inetAddressIPv6() instanceof Inet6Address);
			assertTrue(testObject.theUUID() instanceof UUID);
			//
			assertTrue(testObject.thePattern().matcher((CharSequence) point[5]).find());
			assertEquals(point[1], testObject.inetAddress().getHostAddress());
			assertEquals(point[2], testObject.inetAddressIPv4().getHostAddress());
			assertEquals(point[3], testObject.inetAddressIPv6().getHostAddress());
			assertEquals(((String) point[4]).toUpperCase(), testObject.theUUID().toString().toUpperCase());
		}
	}

	@Test
	public void shouldUseMultipleCandidatePluginsInCorrectOrder() throws Exception {
		Map<String, ?> dataSource = ImmutableMap.of(
				"resultA", this,
				"resultB", this
		);
		final ConfigurationDataProvider data = ConfigurationData.getMapDataProvider(dataSource);
		final MultiPluginDataObject testObject = ConfigurationData.createConfigurationObject(data, MultiPluginDataObject.class);
		assertNotNull(testObject.resultA());
		assertNotNull(testObject.resultB());
		//
		assertEquals(HighPriorityTranslator.ID, testObject.resultA().source);
		assertEquals(LowPriorityTranslator.ID, testObject.resultB().source);
	}


	public static final class TestClassA {
		public final String source;

		public TestClassA(String source) {
			this.source = source;
		}
	}

	public static final class TestClassB {
		public final String source;

		public TestClassB(String source) {
			this.source = source;
		}
	}

	public static final class LowPriorityTranslator implements DataTranslator {

		public static final String ID = "test translator (A+B, low priority)";

		@Override
		public String getId() {
			return ID;
		}

		@Override
		public int getTargetClassSupportPriority(Class<?> targetClass) {
			if ((targetClass == TestClassA.class) || (targetClass == TestClassB.class)) {
				return 5;
			}
			return PRIORITY_NOT_SUPPORTED;
		}

		@Override
		public Object translateToClass(Object source, Class<?> targetClass, DataTranslatorContext context) {
			if (targetClass == TestClassA.class) {
				return new TestClassA(getId());
			} else if (targetClass == TestClassB.class) {
				return new TestClassB(getId());
			}
			return null;
		}
	}

	public static final class MidPriorityTranslator implements DataTranslator {

		public static final String ID = "test translator (A only, middle priority)";

		@Override
		public String getId() {
			return ID;
		}

		@Override
		public int getTargetClassSupportPriority(Class<?> targetClass) {
			if (targetClass == TestClassA.class) {
				return 10;
			}
			return PRIORITY_NOT_SUPPORTED;
		}

		@Override
		public Object translateToClass(Object source, Class<?> targetClass, DataTranslatorContext context) {
			if (targetClass == TestClassA.class) {
				return new TestClassA(getId());
			}
			return null;
		}
	}


	public static final class HighPriorityTranslator implements DataTranslator {

		public static final String ID = "test translator (A only, high priority)";

		@Override
		public String getId() {
			return ID;
		}

		@Override
		public int getTargetClassSupportPriority(Class<?> targetClass) {
			if ((targetClass == TestClassA.class) || (targetClass == TestClassB.class)) {
				return 20;
			}
			return PRIORITY_NOT_SUPPORTED;
		}

		@Override
		public Object translateToClass(Object source, Class<?> targetClass, DataTranslatorContext context) {
			if (targetClass == TestClassA.class) {
				return new TestClassA(getId());
			}
			return null;
		}
	}

}
