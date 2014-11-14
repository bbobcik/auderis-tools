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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class LazyMap<K, V> implements Map<K, V>, Serializable {

	private static final long serialVersionUID = 1L;
	private Map<K, V> baseMap;

	public LazyMap() {
		baseMap = null;
	}

	public Map<K, V> getMap() {
		if ((null != baseMap) && baseMap.isEmpty()) {
			return null;
		}
		return baseMap;
	}

	@Override
	public void clear() {
		if (null != baseMap) {
			baseMap.clear();
		}
	}

	@Override
	public boolean isEmpty() {
		return (null == baseMap) || baseMap.isEmpty();
	}

	@Override
	public int size() {
		return (null != baseMap) ? baseMap.size() : 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return (null != baseMap) && baseMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object val) {
		return (null != baseMap) && baseMap.containsValue(val);
	}

	@Override
	public V get(Object key) {
		if (null == baseMap) {
			return null;
		}
		return baseMap.get(key);
	}

	@Override
	public V put(K key, V val) {
		createLazyMap();
		return baseMap.put(key, val);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> otherMap) {
		if ((null == otherMap) || otherMap.isEmpty()) {
			return;
		}
		createLazyMap();
		baseMap.putAll(otherMap);
	}


	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		if (null == baseMap) {
			return Collections.emptySet();
		}
		return baseMap.entrySet();
	}

	@Override
	public Set<K> keySet() {
		if (null == baseMap) {
			return Collections.emptySet();
		}
		return baseMap.keySet();
	}

	@Override
	public V remove(Object key) {
		if (null == baseMap) {
			return null;
		}
		return baseMap.remove(key);
	}

	@Override
	public Collection<V> values() {
		if (null == baseMap) {
			return Collections.emptySet();
		}
		return baseMap.values();
	}

	private void createLazyMap() {
		if (null == baseMap) {
			baseMap = new HashMap<K, V>();
		}
	}

	@Override
	public int hashCode() {
		if (null == baseMap) {
			return 7;
		}
		return 3 * baseMap.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if ((null == obj) || !(obj instanceof LazyMap)) {
			return false;
		}
		final LazyMap<?, ?> other = (LazyMap<?, ?>) obj;
		if (!Objects.equals(this.baseMap, other.baseMap)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		if (null == baseMap) {
			return "{}";
		}
		return baseMap.toString();
	}

}
