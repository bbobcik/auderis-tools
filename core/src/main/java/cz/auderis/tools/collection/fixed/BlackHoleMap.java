package cz.auderis.tools.collection.fixed;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class BlackHoleMap<K,V> implements Map<K,V>, Serializable {
	private static final long serialVersionUID = 123L;

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public V get(Object key) {
		return null;
	}

	@Override
	public V put(K key, V value) {
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// Operation ignored
	}

	@Override
	public V remove(Object key) {
		return null;
	}

	@Override
	public void clear() {
		// Operation ignored
	}

	@Override
	public Set<K> keySet() {
		return new BlackHoleSet<K>();
	}

	@Override
	public Collection<V> values() {
		return new BlackHoleCollection<V>();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return new BlackHoleSet<Entry<K, V>>();
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof Map)) {
			return false;
		}
		final Map<?,?> other = (Map<?, ?>) obj;
		return other.isEmpty();
	}

	@Override
	public String toString() {
		return "{}";
	}

}
