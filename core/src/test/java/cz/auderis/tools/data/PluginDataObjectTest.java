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
			final CorePluginDataObject testObject = ConfigurationData.createConfigurationObject(
					data, CorePluginDataObject.class, getClass().getClassLoader()
			);
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

}
