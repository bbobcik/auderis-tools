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

import cz.auderis.tools.time.timeout.Timeout;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class SimpleCache<K, V> {

	private final Map<K, CacheEntry<V>> cache;

	protected SimpleCache() {
		super();
		cache = new HashMap<K, CacheEntry<V>>();
	}

	public static <K, V> SimpleCache<K, V> newInstance() {
		return new SimpleCache<K, V>();
	}

	public void put(K key, V value) {
		if (null == key) {
			throw new NullPointerException();
		}
		CacheEntry<V> entry = cache.get(key);
		if (null == entry) {
			entry = new CacheEntry<V>(value);
			cache.put(key, entry);
		} else {
			entry.setValue(value);
			Timeout t = entry.getTimeout();
			if (null != t) {
				t.restart();
			}
		}
	}

	public void putWithTimeout(K key, V value, Timeout t) {
		if (null == key) {
			throw new NullPointerException();
		}
		CacheEntry<V> entry = cache.get(key);
		if (null == entry) {
			entry = new CacheEntry<V>(value);
			entry.setTimeout(t);
			cache.put(key, entry);
		} else {
			entry.setValue(value);
			entry.setTimeout(t);
		}
		if (null != t) {
			t.restart();
		}
	}

	public V get(K key) {
		if (null == key) {
			throw new NullPointerException();
		}
		CacheEntry<V> entry = cache.get(key);
		if (null == entry) {
			return null;
		} else if (!entry.isAvailable()) {
			cache.remove(key);
			return null;
		}
		return entry.getValue();
	}

	public boolean hasKey(K key) {
		if (null == key) {
			throw new NullPointerException();
		}
		return cache.containsKey(key);
	}

	public void remove(K key) {
		if (null == key) {
			throw new NullPointerException();
		}
		cache.remove(key);
	}

	public void clear() {
		cache.clear();
	}

	public boolean isAvailable(K key) {
		if (null == key) {
			throw new NullPointerException();
		}
		CacheEntry<V> entry = cache.get(key);
		if (null == entry) {
			return false;
		}
		return entry.isAvailable();
	}

	public boolean isExpired(K key) {
		if (null == key) {
			throw new NullPointerException();
		}
		CacheEntry<V> entry = cache.get(key);
		if (null == entry) {
			return false;
		}
		return entry.isTimeoutExpired();
	}

	public void refresh(K key) {
		if (null == key) {
			throw new NullPointerException();
		}
		CacheEntry<V> entry = cache.get(key);
		if (null != entry) {
			entry.refresh();
		}
	}

	protected static final class CacheEntry<W> {

		// Invariant: either nullStored is true or valueRef != null
		private boolean nullStored;
		private SoftReference<W> valueRef;
		private Timeout expiration;

		public CacheEntry(W value) {
			this.expiration = null;
			if (null == value) {
				this.nullStored = true;
			} else {
				this.valueRef = new SoftReference<W>(value);
			}
		}

		public boolean isAvailable() {
			if ((null != expiration) && expiration.isExpired()) {
				return false;
			}
			return nullStored || (null != valueRef.get());
		}

		public boolean isTimeoutExpired() {
			return (null != expiration) && expiration.isExpired();
		}

		public W getValue() {
			if ((null != expiration) && expiration.isExpired()) {
				return null;
			}
			if (nullStored) {
				return null;
			}
			return valueRef.get();
		}

		public void setValue(W newValue) {
			if (null == newValue) {
				nullStored = true;
				valueRef = null;
			} else {
				valueRef = new SoftReference<W>(newValue);
				nullStored = false;
			}
		}

		public Timeout getTimeout() {
			return expiration;
		}

		public void setTimeout(Timeout t) {
			this.expiration = t;
		}

		public void refresh() {
			if (null != expiration) {
				expiration.restart();
			}
		}
	}

}
