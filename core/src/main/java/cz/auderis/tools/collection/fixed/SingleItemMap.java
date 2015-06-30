package cz.auderis.tools.collection.fixed;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class SingleItemMap<K,V> implements Map<K,V>, Serializable {
	private static final long serialVersionUID = 234L;

	protected boolean hasItem;
	protected K key;
	protected V value;
	protected final transient EntryImpl entryInstance;

	public SingleItemMap() {
		entryInstance = new EntryImpl();
	}

	public SingleItemMap(K key, V value) {
		this.key = key;
		this.value = value;
		this.hasItem = true;
		entryInstance = new EntryImpl();
	}

	@Override
	public boolean isEmpty() {
		return !hasItem;
	}

	@Override
	public int size() {
		return hasItem ? 1 : 0;
	}

	@Override
	public boolean containsKey(Object k) {
		return hasItem && ((null == key) ? (null == k) : key.equals(k));
	}

	@Override
	public boolean containsValue(Object v) {
		return hasItem && ((null == value) ? (null == v) : value.equals(v));
	}

	@Override
	public V get(Object k) {
		return containsKey(k) ? value : null;
	}

	@Override
	public void clear() {
		hasItem = false;
		key = null;
		value = null;
	}

	@Override
	public V put(K k, V v) {
		final V prevValue = get(k);
		key = k;
		value = v;
		hasItem = true;
		return prevValue;
	}

	@Override
	public V remove(Object k) {
		if (!containsKey(k)) {
			return null;
		}
		hasItem = false;
		final V prevValue = value;
		key = null;
		value = null;
		return prevValue;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		if (m.isEmpty()) {
			return;
		} else if (1 == m.size()) {
			final Entry<? extends K, ? extends V> otherEntry = m.entrySet().iterator().next();
			key = otherEntry.getKey();
			value = otherEntry.getValue();
			hasItem = true;
		}
		throw new IllegalArgumentException("cannot add more than 1 mapping");
	}

	@Override
	public Set<K> keySet() {
		return new SetImpl<K>(key, Set.class);
	}

	@Override
	public Collection<V> values() {
		return new SetImpl<V>(value, Collection.class);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return new SetImpl<Entry<K, V>>(entryInstance, Set.class);
	}

	protected class SetImpl<T> implements Set<T> {
		private final T item;
		private final transient Class<? extends Collection> equalClass;

		protected SetImpl(T item, Class<? extends Collection> equalClass) {
			this.item = item;
			this.equalClass = equalClass;
		}

		@Override
		public boolean isEmpty() {
			return !hasItem;
		}

		@Override
		public int size() {
			return hasItem ? 1 : 0;
		}

		@Override
		public boolean contains(Object o) {
			return hasItem && ((null == item) ? (null == o) : item.equals(o));
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			if (c.isEmpty()) {
				return true;
			} else if (!hasItem) {
				return false;
			} else if (1 == c.size()) {
				final Object o = c.iterator().next();
				return (null == item) ? (null == o) : item.equals(o);
			}
			if (null == item) {
				for (final Object o : c) {
					if (null != o) {
						return false;
					}
				}
			} else {
				for (final Object o : c) {
					if (!item.equals(o)) {
						return false;
					}
				}
			}
			return true;
		}

		@Override
		public Iterator<T> iterator() {
			return new IteratorImpl<T>(item);
		}

		@Override
		public Object[] toArray() {
			if (!hasItem) {
				return BlackHoleCollection.EMPTY_ARRAY;
			}
			return new Object[] { item };
		}

		@Override
		public <T1> T1[] toArray(T1[] a) {
			if (!hasItem) {
				if (a.length > 0) {
					a[0] = null;
				}
				return a;
			} else if (a.length < 1) {
				return (T1[]) new Object[] { item };
			}
			a[0] = (T1) item;
			if (a.length > 1) {
				a[1] = null;
			}
			return a;
		}

		@Override
		public boolean add(T t) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean addAll(Collection<? extends T> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(Object o) {
			if (!contains(o)) {
				return false;
			}
			SingleItemMap.this.clear();
			return true;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			if (!hasItem || !c.contains(item)) {
				return false;
			}
			SingleItemMap.this.clear();
			return true;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			if (!hasItem || c.contains(item)) {
				return false;
			}
			SingleItemMap.this.clear();
			return true;
		}

		@Override
		public void clear() {
			SingleItemMap.this.clear();
		}

		@Override
		public int hashCode() {
			return (hasItem && (null != item)) ? item.hashCode() : 0;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else if ((null == obj) || !equalClass.isAssignableFrom(obj.getClass())) {
				return false;
			}
			final Collection<?> other = (Collection<?>) obj;
			if (!hasItem) {
				return other.isEmpty();
			} else if (1 != other.size()) {
				return false;
			}
			final Object o = other.iterator().next();
			return (null == item) ? (null == o) : item.equals(o);
		}

		@Override
		public String toString() {
			if (!hasItem) {
				return "[]";
			} else if (null == item) {
				return "[null]";
			}
			return "[" + item + ']';
		}
	}

	protected class IteratorImpl<T> implements Iterator<T> {
		private final T item;
		private boolean exhausted;

		protected IteratorImpl(T item) {
			this.item = item;
		}

		@Override
		public boolean hasNext() {
			return hasItem && !exhausted;
		}

		@Override
		public T next() {
			if (!hasItem || exhausted) {
				throw new NoSuchElementException();
			}
			exhausted = true;
			return item;
		}

		@Override
		public void remove() {
			if (!hasItem || !exhausted) {
				throw new IllegalStateException();
			}
			clear();
		}
	}

	protected class EntryImpl implements Map.Entry<K, V> {

		@Override
		public K getKey() {
			if (!hasItem) {
				throw new IllegalStateException();
			}
			return key;
		}

		@Override
		public V getValue() {
			if (!hasItem) {
				throw new IllegalStateException();
			}
			return value;
		}

		@Override
		public V setValue(V newValue) {
			if (!hasItem) {
				throw new IllegalStateException();
			}
			final V prevValue = value;
			value = newValue;
			return prevValue;
		}

		@Override
		public int hashCode() {
			if (!hasItem) {
				throw new IllegalStateException();
			}
			final int keyHash = (null != key) ? key.hashCode() : 0;
			final int valueHash = (null != value) ? value.hashCode() : 0;
			return keyHash ^ valueHash;
		}

		@Override
		public boolean equals(Object obj) {
			if (!hasItem) {
				throw new IllegalStateException();
			} else if (this == obj) {
				return true;
			} else if (!(obj instanceof Map.Entry)) {
				return false;
			}
			final Map.Entry<?, ?> otherEntry = (Entry<?, ?>) obj;
			final Object otherKey = otherEntry.getKey();
			if ((null == key) ? (null != otherKey) : !key.equals(otherKey)) {
				return false;
			}
			final Object otherValue = otherEntry.getValue();
			if ((null == value) ? (null != otherValue) : !value.equals(otherValue)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			if (!hasItem) {
				throw new IllegalStateException();
			} else if (null == key) {
				return "null=" + value;
			}
			return key + "=" + value;
		}
	}

}
