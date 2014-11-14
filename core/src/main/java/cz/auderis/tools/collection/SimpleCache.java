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

/**
 * The type Simple cache.
 * @param <K>  the type parameter
 * @param <V>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class SimpleCache<K, V> {

	private final Map<K, CacheEntry<V>> cache;

	/**
	 * Instantiates a new Simple cache.
	 */
	protected SimpleCache() {
		super();
		cache = new HashMap<K, CacheEntry<V>>();
	}

	/**
	 * New instance.
	 * @param <K>  the type parameter
	 * @param <V>  the type parameter
	 * @return the simple cache
	 */
	public static <K, V> SimpleCache<K, V> newInstance() {
		return new SimpleCache<K, V>();
	}

	/**
	 * Put void.
	 *
	 * @param key the key
	 * @param value the value
	 */
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

	/**
	 * Put with timeout.
	 *
	 * @param key the key
	 * @param value the value
	 * @param t the t
	 */
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

	/**
	 * Get v.
	 *
	 * @param key the key
	 * @return the v
	 */
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

	/**
	 * Has key.
	 *
	 * @param key the key
	 * @return the boolean
	 */
	public boolean hasKey(K key) {
		if (null == key) {
			throw new NullPointerException();
		}
		return cache.containsKey(key);
	}

	/**
	 * Remove void.
	 *
	 * @param key the key
	 */
	public void remove(K key) {
		if (null == key) {
			throw new NullPointerException();
		}
		cache.remove(key);
	}

	/**
	 * Clear void.
	 */
	public void clear() {
		cache.clear();
	}

	/**
	 * Is available.
	 *
	 * @param key the key
	 * @return the boolean
	 */
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

	/**
	 * Is expired.
	 *
	 * @param key the key
	 * @return the boolean
	 */
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

	/**
	 * Refresh void.
	 *
	 * @param key the key
	 */
	public void refresh(K key) {
		if (null == key) {
			throw new NullPointerException();
		}
		CacheEntry<V> entry = cache.get(key);
		if (null != entry) {
			entry.refresh();
		}
	}

	/**
	 * The type Cache entry.
	 * @param <W>  the type parameter
	 */
	protected static final class CacheEntry<W> {

		// Invariant: either nullStored is true or valueRef != null
		private boolean nullStored;
		private SoftReference<W> valueRef;
		private Timeout expiration;

		/**
		 * Instantiates a new Cache entry.
		 *
		 * @param value the value
		 */
		public CacheEntry(W value) {
			this.expiration = null;
			if (null == value) {
				this.nullStored = true;
			} else {
				this.valueRef = new SoftReference<W>(value);
			}
		}

		/**
		 * Is available.
		 *
		 * @return the boolean
		 */
		public boolean isAvailable() {
			if ((null != expiration) && expiration.isExpired()) {
				return false;
			}
			return nullStored || (null != valueRef.get());
		}

		/**
		 * Is timeout expired.
		 *
		 * @return the boolean
		 */
		public boolean isTimeoutExpired() {
			return (null != expiration) && expiration.isExpired();
		}

		/**
		 * Gets value.
		 *
		 * @return the value
		 */
		public W getValue() {
			if ((null != expiration) && expiration.isExpired()) {
				return null;
			}
			if (nullStored) {
				return null;
			}
			return valueRef.get();
		}

		/**
		 * Sets value.
		 *
		 * @param newValue the new value
		 */
		public void setValue(W newValue) {
			if (null == newValue) {
				nullStored = true;
				valueRef = null;
			} else {
				valueRef = new SoftReference<W>(newValue);
				nullStored = false;
			}
		}

		/**
		 * Gets timeout.
		 *
		 * @return the timeout
		 */
		public Timeout getTimeout() {
			return expiration;
		}

		/**
		 * Sets timeout.
		 *
		 * @param t the t
		 */
		public void setTimeout(Timeout t) {
			this.expiration = t;
		}

		/**
		 * Refresh void.
		 */
		public void refresh() {
			if (null != expiration) {
				expiration.restart();
			}
		}
	}

}
