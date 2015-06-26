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
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Utility class simplifying operations with collections of Map.Entry records
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public final class MapEntries {

	/**
	 * Copy from map.
	 *
	 * @param <K>  the type parameter
	 * @param <V>  the type parameter
	 * @param sourceMap the source map
	 * @return the set
	 */
	public static <K, V> Set<Map.Entry<K, V>> copyFromMap(Map<? extends K, ? extends V> sourceMap) {
		if (null == sourceMap) {
			throw new NullPointerException();
		}
		final Set<? extends Map.Entry<? extends K, ? extends V>> sourceEntries = sourceMap.entrySet();
		return copy(sourceEntries);
	}

	/**
	 * Copy set.
	 *
	 * @param <K>  the type parameter
	 * @param <V>  the type parameter
	 * @param sourceEntries the source entries
	 * @return the set
	 */
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

	/**
	 * Copy from map.
	 *
	 * @param <K>  the type parameter
	 * @param <V>  the type parameter
	 * @param sourceMap the source map
	 * @param target the target
	 */
	public static <K, V> void copyFromMap(Map<? extends K, ? extends V> sourceMap, Collection<? super Map.Entry<K, V>> target) {
		if ((null == sourceMap) || (null == target)) {
			throw new NullPointerException();
		}
		copy(sourceMap.entrySet(), target);
	}

	/**
	 * Copy void.
	 *
	 * @param <K>  the type parameter
	 * @param <V>  the type parameter
	 * @param source the source
	 * @param target the target
	 */
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

	public static <K, V> Comparator<? extends Map.Entry<K, V>> comparator(Comparator<K> keyCmp) {
		if (null == keyCmp) {
			throw new NullPointerException();
		}
		return new ComparatorImpl<K, V>(keyCmp, null);
	}

	public static <K, V> Comparator<? extends Map.Entry<K, V>> comparator(Comparator<K> keyCmp, Comparator<V> valueCmp) {
		if ((null == keyCmp) && (null == valueCmp)) {
			throw new NullPointerException();
		}
		return new ComparatorImpl<K, V>(keyCmp, valueCmp);
	}

	public static <K, V> Comparator<? extends Map.Entry<K, V>> valueComparator(Comparator<V> valueCmp) {
		if (null == valueCmp) {
			throw new NullPointerException();
		}
		return new ComparatorImpl<K, V>(null, valueCmp);
	}


	private MapEntries() {
		throw new AssertionError();
	}

	static final class ComparatorImpl<K, V> implements Comparator<Map.Entry<K, V>> {
		private final Comparator<K> keyComparator;
		private final Comparator<V> valueComparator;

		ComparatorImpl(Comparator<K> keyComparator, Comparator<V> valueComparator) {
			this.keyComparator = keyComparator;
			this.valueComparator = valueComparator;
		}

		@Override
		public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
			if ((null == e1) || (null == e2)) {
				throw new NullPointerException();
			}
			if (null != keyComparator) {
				final int cmp = keyComparator.compare(e1.getKey(), e2.getKey());
				if (0 != cmp) {
					return cmp;
				}
			}
			if (null != valueComparator) {
				final int cmp = valueComparator.compare(e1.getValue(), e2.getValue());
				return cmp;
			}
			return 0;
		}
	}

}
