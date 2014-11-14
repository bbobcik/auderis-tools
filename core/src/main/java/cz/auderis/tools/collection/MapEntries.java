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

package cz.auderis.tools.collection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class MapEntries {

	public static <K, V> Set<Map.Entry<K, V>> copyFromMap(Map<? extends K, ? extends V> sourceMap) {
		if (null == sourceMap) {
			throw new NullPointerException();
		}
		final Set<? extends Map.Entry<? extends K, ? extends V>> sourceEntries = sourceMap.entrySet();
		return copy(sourceEntries);
	}

	public static <K, V> Set<Map.Entry<K, V>> copy(Iterable<? extends Map.Entry<? extends K, ? extends V>> sourceEntries) {
		if (null == sourceEntries) {
			throw new NullPointerException();
		}
		final Set<Map.Entry<K, V>> result = new HashSet<Map.Entry<K, V>>();
		for (final Map.Entry<? extends K, ? extends V> entry : sourceEntries) {
			if (null == entry) {
				// null entries silently ignored
			} else if (null == entry.getKey()) {
				throw new IllegalArgumentException("one of source map entries has null key");
			} else {
				final SimpleMapEntry<K, V> entryCopy = SimpleMapEntry.copyOf(entry);
				result.add(entryCopy);
			}
		}
		return result;
	}

	public static <K, V> void copyFromMap(Map<? extends K, ? extends V> sourceMap, Collection<? super Map.Entry<K, V>> target) {
		if ((null == sourceMap) || (null == target)) {
			throw new NullPointerException();
		}
		copy(sourceMap.entrySet(), target);
	}

	public static <K, V> void copy(Iterable<? extends Map.Entry<? extends K, ? extends V>> source, Collection<? super Map.Entry<K, V>> target) {
		if ((null == source) || (null == target)) {
			throw new NullPointerException();
		}
		for (final Map.Entry<? extends K, ? extends V> entry : source) {
			if (null == entry) {
				// null entries silently ignored
			} else if (null == entry.getKey()) {
				throw new IllegalArgumentException("one of source map entries has null key");
			} else {
				final SimpleMapEntry<K, V> entryCopy = SimpleMapEntry.copyOf(entry);
				target.add(entryCopy);
			}
		}
	}

	private MapEntries() {
		throw new AssertionError();
	}

}
