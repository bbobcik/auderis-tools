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

import cz.auderis.tools.collection.iterator.Iterators;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MultiLevelMap<K, V> implements CascadingMap<K, V> {

	protected static final String ERR_ITEMS_NOT_ENTRIES = "all items must be map entries";

	protected final Map<K, V> currentLevel;
	protected final transient MultiLevelEntrySet entrySetView;
	protected final transient MultiLevelKeySet keySetView;
	protected Map<K, V> parent;

	public static <K1, V1> MultiLevelMap<K1, V1> create() {
		return new MultiLevelMap<K1, V1>(null, null);
	}

	public static <K1, V1> MultiLevelMap<K1, V1> createWithParent(Map<? extends K1, ? extends V1> parentMap) {
		return new MultiLevelMap<K1, V1>(null, parentMap);
	}

	public static <K1, V1> MultiLevelMap<K1, V1> createWithContentAndParent(Map<? extends K1, ? extends V1> content,
			Map<? extends K1, ? extends V1> parentMap) {
		return new MultiLevelMap<K1, V1>(content, parentMap);
	}

	protected MultiLevelMap(Map<? extends K, ? extends V> current, Map<? extends K, ? extends V> parent) {
		if (null == current) {
			currentLevel = new HashMap<K, V>();
		} else {
			currentLevel = new HashMap<K, V>(current);
		}
		if (null != parent) {
			@SuppressWarnings("unchecked")
			final Map<K, V> parentMap = (Map<K, V>) parent;
			this.parent = parentMap;
		}
		entrySetView = new MultiLevelEntrySet();
		keySetView = new MultiLevelKeySet();
	}

	@Override
	public Map<K, V> getParentMap() {
		return parent;
	}

	@Override
	public void setParentMap(Map<? extends K, ? extends V> prnt) {
		@SuppressWarnings("unchecked")
		final Map<K, V> parentMap = (Map<K, V>) prnt;
		this.parent = parentMap;
	}

	@Override
	public boolean hasParentMap() {
		return (null != parent);
	}

	@Override
	public void clearCurrentMap() {
		currentLevel.clear();
	}

	@Override
	public void clearParentMap() {
		if (null != parent) {
			parent.clear();
		}
	}

	@Override
	public void clear() {
		clearCurrentMap();
		try {
			clearParentMap();
		} catch (UnsupportedOperationException e) {
			// Ignore this exception
		}
	}

	@Override
	public boolean containsKey(Object key) {
		if (null == key) {
			throw new NullPointerException();
		} else if (currentLevel.containsKey(key)) {
			return true;
		} else if ((null != parent) && parent.containsKey(key)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		if (currentLevel.containsValue(value)) {
			return true;
		} else if ((null != parent) && parent.containsValue(value)) {
			return true;
		}
		return false;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return entrySetView;
	}

	@Override
	public V get(Object key) {
		if (null == key) {
			throw new NullPointerException();
		} else if (currentLevel.containsKey(key)) {
			return currentLevel.get(key);
		} else if ((null != parent) && parent.containsKey(key)) {
			return parent.get(key);
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		if (!currentLevel.isEmpty()) {
			return false;
		} else if (null == parent) {
			return true;
		}
		return parent.isEmpty();
	}

	@Override
	public V put(K key, V value) {
		if (null == key) {
			throw new NullPointerException();
		}
		final V oldValue = get(key);
		currentLevel.put(key, value);
		return oldValue;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> otherMap) {
		if (null == otherMap) {
			throw new NullPointerException();
		}
		final Set<? extends Map.Entry<? extends K, ? extends V>> entrySet = otherMap.entrySet();
		for (Map.Entry<? extends K, ? extends V> entry : entrySet) {
			final K key = entry.getKey();
			if (null == key) {
				throw new IllegalArgumentException("argument map contains null keys");
			}
		}
		for (Map.Entry<? extends K, ? extends V> entry : entrySet) {
			currentLevel.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V removeFromCurrentMap(Object key) {
		if (null == key) {
			throw new NullPointerException();
		}
		return currentLevel.remove(key);
	}

	@Override
	public V remove(Object key) {
		if (null == key) {
			throw new NullPointerException();
		}
		V oldValue = null;
		if ((null != parent) && parent.containsKey(key)) {
			oldValue = parent.remove(key);
		}
		if (currentLevel.containsKey(key)) {
			oldValue = currentLevel.remove(key);
		}
		return oldValue;
	}

	@Override
	public int size() {
		int size = currentLevel.size();
		if (null != parent) {
			Set<Entry<K, V>> entrySet = parent.entrySet();
			for (Entry<K, V> entry : entrySet) {
				final K key = entry.getKey();
				if (null == key) {
					// Ignore null keys
				} else if (currentLevel.containsKey(key)) {
					// Ignore items that are overriden
				} else {
					++size;
				}
			}
			return size;
		}

		int parentSize = (null == parent) ? 0 : parent.size();
		return parentSize + currentLevel.size();
	}

	@Override
	public Set<K> keySet() {
		return keySetView;
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	protected class MultiLevelEntrySet implements Set<Map.Entry<K, V>> {

		@Override
		public void clear() {
			MultiLevelMap.this.clear();
		}

		@Override
		public boolean contains(Object entryObj) {
			if (null == entryObj) {
				throw new NullPointerException();
			} else if (!(entryObj instanceof Map.Entry<?, ?>)) {
				throw new IllegalArgumentException("argument is not a map entry");
			}
			final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entryObj;
			final Object key = entry.getKey();
			if ((null == key) || !MultiLevelMap.this.containsKey(key)) {
				return false;
			}
			final Object mapValue = MultiLevelMap.this.get(key);
			return Objects.equals(entry.getValue(), mapValue);
		}

		@Override
		public boolean containsAll(Collection<?> entryObjs) {
			if (null == entryObjs) {
				throw new NullPointerException();
			}
			for (Object entryObj : entryObjs) {
				if ((null == entryObj) || !(entryObj instanceof Map.Entry<?,?>)) {
					throw new IllegalArgumentException(ERR_ITEMS_NOT_ENTRIES);
				}
				final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entryObj;
				final Object key = entry.getKey();
				if (null == key) {
					return false;
				}
				if (!MultiLevelMap.this.containsKey(key)) {
					return false;
				}
				final Object actualValue = MultiLevelMap.this.get(key);
				if (!Objects.equals(entry.getValue(), actualValue)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean isEmpty() {
			return MultiLevelMap.this.isEmpty();
		}

		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new EntryIterator();
		}

		@Override
		public boolean remove(Object entryObj) {
			if (null == entryObj) {
				throw new NullPointerException();
			} else if (!(entryObj instanceof Map.Entry<?,?>)) {
				throw new IllegalArgumentException("argument is not a map entry");
			}
			final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entryObj;
			final Object key = entry.getKey();
			final Object value = entry.getValue();
			boolean removed = false;
			if (currentLevel.containsKey(key)) {
				final Object actualValue = currentLevel.get(key);
				if (Objects.equals(value, actualValue)) {
					currentLevel.remove(key);
					removed = true;
				}
			}
			if (null != parent) {
				try {
					boolean removedFromParent = parent.entrySet().remove(entry);
					removed = removed || removedFromParent;
				} catch (Exception e) {
					// Exceptions ignored
				}
			}
			return removed;
		}

		@Override
		public boolean removeAll(Collection<?> entryObjs) {
			if (null == entryObjs) {
				throw new NullPointerException();
			}
			for (Object entryObj : entryObjs) {
				if ((null == entryObj) || !(entryObj instanceof Map.Entry<?, ?>)) {
					throw new IllegalArgumentException(ERR_ITEMS_NOT_ENTRIES);
				}
			}
			boolean removed = currentLevel.entrySet().removeAll(entryObjs);
			if (null != parent) {
				try {
					boolean removedFromParent = parent.entrySet().removeAll(entryObjs);
					removed = removed || removedFromParent;
				} catch (Exception e) {
					// Exceptions ignored
				}
			}
			return removed;
		}

		@Override
		public boolean retainAll(Collection<?> entryObjs) {
			if (null == entryObjs) {
				throw new NullPointerException();
			}
			for (Object entryObj : entryObjs) {
				if ((null == entryObj) || !(entryObj instanceof Map.Entry<?, ?>)) {
					throw new IllegalArgumentException(ERR_ITEMS_NOT_ENTRIES);
				}
			}
			boolean changed = currentLevel.entrySet().retainAll(entryObjs);
			if (null != parent) {
				try {
					boolean parentChanged = parent.entrySet().retainAll(entryObjs);
					changed = changed || parentChanged;
				} catch (Exception e) {
					// Ignore exceptions
				}
			}
			return changed;
		}

		@Override
		public int size() {
			return MultiLevelMap.this.size();
		}

		private Set<Map.Entry<K, V>> getSetSnapshot() {
			final Set<Map.Entry<K, V>> snapshot = MapEntries.copyFromMap(currentLevel);
			if (null != parent) {
				final Set<Map.Entry<K, V>> parentEntries = parent.entrySet();
				for (Map.Entry<K, V> parentEntry : parentEntries) {
					final K key = parentEntry.getKey();
					if ((null != key) && !currentLevel.containsKey(key)) {
						snapshot.add(parentEntry);
					}
				}
			}
			return snapshot;
		}

		@Override
		public Object[] toArray() {
			return getSetSnapshot().toArray();
		}

		@Override
		public <T> T[] toArray(T[] arr) {
			return getSetSnapshot().toArray(arr);
		}

		@Override
		public boolean add(Map.Entry<K, V> entry) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(Collection<? extends Map.Entry<K, V>> arg0) {
			throw new UnsupportedOperationException();
		}

	}

	protected class MultiLevelKeySet implements Set<K> {

		@Override
		public void clear() {
			entrySetView.clear();
		}

		@Override
		public boolean contains(Object key) {
			return MultiLevelMap.this.containsKey(key);
		}

		@Override
		public boolean containsAll(Collection<?> keys) {
			if (null == keys) {
				throw new NullPointerException();
			}
			for (Object k : keys) {
				if (null == k) {
					return false;
				} else if (!containsKey(k)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean isEmpty() {
			return MultiLevelMap.this.isEmpty();
		}

		@Override
		public Iterator<K> iterator() {
			return new KeyIterator();
		}

		@Override
		public boolean remove(Object key) {
			if (null == key) {
				throw new NullPointerException();
			}
			if (!MultiLevelMap.this.containsKey(key)) {
				return false;
			}
			MultiLevelMap.this.remove(key);
			return true;
		}

		@Override
		public boolean removeAll(Collection<?> keys) {
			if (null == keys) {
				throw new NullPointerException();
			}
			boolean changed = false;
			for (Object k : keys) {
				if ((null != k) && MultiLevelMap.this.containsKey(k)) {
					MultiLevelMap.this.remove(k);
					changed = true;
				}
			}
			return changed;
		}

		@Override
		public boolean retainAll(Collection<?> keys) {
			if (null == keys) {
				throw new NullPointerException();
			}
			final Set<?> retainedKeys;
			if (keys instanceof Set<?>) {
				retainedKeys = (Set<?>) keys;
			} else {
				retainedKeys = new HashSet<Object>(keys);
			}
			boolean changed = false;
			Iterator<K> currentLevelIterator = currentLevel.keySet().iterator();
			while (currentLevelIterator.hasNext()) {
				final K currentKey = currentLevelIterator.next();
				if (!retainedKeys.contains(currentKey)) {
					currentLevelIterator.remove();
					changed = true;
				}
			}
			if (null != parent) {
				Iterator<K> parentIterator = parent.keySet().iterator();
				try {
					while (parentIterator.hasNext()) {
						final K parentKey = parentIterator.next();
						if (!retainedKeys.contains(parentKey)) {
							parentIterator.remove();
							changed = true;
						}
					}
				} catch (Exception e) {
					// Ignore exceptions
				}
			}
			return changed;
		}

		@Override
		public int size() {
			return MultiLevelMap.this.size();
		}

		@Override
		public Object[] toArray() {
			if (null == parent) {
				return currentLevel.keySet().toArray();
			}
			final Set<Object> keyUnion = new HashSet<Object>(currentLevel.keySet());
			keyUnion.addAll(parent.keySet());
			return keyUnion.toArray();
		}

		@Override
		public <T> T[] toArray(T[] baseArray) {
			if (null == parent) {
				return currentLevel.keySet().toArray(baseArray);
			}
			final Set<Object> keyUnion = new HashSet<Object>(currentLevel.keySet());
			keyUnion.addAll(parent.keySet());
			return keyUnion.toArray(baseArray);
		}

		@Override
		public boolean add(K e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(Collection<? extends K> c) {
			throw new UnsupportedOperationException();
		}
	}

	protected class EntryIterator implements Iterator<Map.Entry<K, V>> {

		private final Iterator<Map.Entry<K, V>> currentIterator;
		private final Iterator<Map.Entry<K, V>> parentIterator;
		private boolean iteratingCurrentLevel;

		protected EntryIterator() {
			currentIterator = currentLevel.entrySet().iterator();
			if (null != parent) {
				Map<K, V> parentCopy = new HashMap<K, V>(parent);
				parentCopy.keySet().removeAll(currentLevel.keySet());
				parentIterator = parentCopy.entrySet().iterator();
			} else {
				parentIterator = Iterators.emptyIterator();
			}
			iteratingCurrentLevel = true;
		}

		@Override
		public boolean hasNext() {
			if (iteratingCurrentLevel) {
				if (currentIterator.hasNext()) {
					return true;
				}
				iteratingCurrentLevel = false;
			}
			return parentIterator.hasNext();
		}

		@Override
		public Map.Entry<K, V> next() {
			if (iteratingCurrentLevel) {
				return (Map.Entry<K, V>) currentIterator.next();
			}
			return (Map.Entry<K, V>) parentIterator.next();
		}

		@Override
		public void remove() {
			if (iteratingCurrentLevel) {
				currentIterator.remove();
			} else if (null != parentIterator) {
				parentIterator.remove();
			}
		}
	}

	protected class KeyIterator implements Iterator<K> {

		private final EntryIterator baseIterator;

		protected KeyIterator() {
			baseIterator = new EntryIterator();
		}

		@Override
		public boolean hasNext() {
			return baseIterator.hasNext();
		}

		@Override
		public K next() {
			return baseIterator.next().getKey();
		}

		@Override
		public void remove() {
			baseIterator.remove();
		}
	}

}
