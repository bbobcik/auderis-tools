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

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Map;

/**
 * The type Simple map entry.
 * @param <K>  the type parameter
 * @param <V>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public final class SimpleMapEntry<K, V> implements Map.Entry<K, V>, Serializable {
	private static final long serialVersionUID = 1L;

	private K key;
	private V value;
	private transient int keyHashCode;

	/**
	 * Instantiates a new Simple map entry.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public SimpleMapEntry(K key, V value) {
		super();
		if (null == key) {
			throw new NullPointerException();
		}
		this.key = key;
		this.keyHashCode = key.hashCode();
		this.value = value;
	}

	/**
	 * Copy of.
	 *
	 * @param <K>  the type parameter
	 * @param <V>  the type parameter
	 * @param baseEntry the base entry
	 * @return the simple map entry
	 */
	public static <K, V> SimpleMapEntry<K, V> copyOf(Map.Entry<? extends K, ? extends V> baseEntry) {
		if (null == baseEntry) {
			throw new NullPointerException();
		}
		final K key = baseEntry.getKey();
		if (null == key) {
			throw new IllegalArgumentException("copied map entry key is null");
		}
		final V value = baseEntry.getValue();
		return new SimpleMapEntry<K, V>(key, value);
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V newValue) {
		final V oldValue = value;
		value = newValue;
		return oldValue;
	}

	@Override
	public int hashCode() {
		return keyHashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if ((null == obj) || !(obj instanceof Map.Entry)) {
			return false;
		}
		final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
		if (!this.key.equals(other.getKey())) {
			return false;
		} else if (!safeEquals(this.value, other.getValue())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "{" + key + " => " + value + "}";
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		if (null == key) {
			throw new InvalidObjectException("attempt to deserialize map entry with null key");
		}
		keyHashCode = key.hashCode();
	}

	@SuppressWarnings("unused")
	private void readObjectNoData() throws ObjectStreamException {
		throw new UnsupportedOperationException();
	}

	private static boolean safeEquals(Object o1, Object o2) {
		if ((null == o1) != (null == o2)) {
			return false;
		} else if (null == o1) {
			return true;
		}
		return o1.equals(o2);
	}

}
