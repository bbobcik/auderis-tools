package cz.auderis.tools.collection.fixed;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleItemCollection<E> implements Collection<E>, Serializable {
	private static final long serialVersionUID = 234L;

	protected boolean hasItem;
	protected E item;

	public SingleItemCollection() {
	}

	public SingleItemCollection(E obj) {
		this.hasItem = true;
		this.item = obj;
	}

	@Override
	public int size() {
		return hasItem ? 1 : 0;
	}

	@Override
	public boolean isEmpty() {
		return !hasItem;
	}

	@Override
	public boolean contains(Object o) {
		if (!hasItem) {
			return false;
		}
		return (null == item) ? (null == o) : item.equals(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (c.isEmpty()) {
			return true;
		} else if (!hasItem) {
			return false;
		} else if (1 == c.size()) {
			final Object other = c.iterator().next();
			return (null == item) ? (null == other) : item.equals(other);
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
	public void clear() {
		this.hasItem = false;
		this.item = null;
	}

	@Override
	public Iterator<E> iterator() {
		return new IteratorImpl();
	}

	@Override
	public Object[] toArray() {
		if (!hasItem) {
			return BlackHoleCollection.EMPTY_ARRAY;
		}
		return new Object[] { item };
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if (!hasItem) {
			if (0 != a.length) {
				a[0] = null;
			}
			return a;
		} else if (a.length < 1) {
			return (T[]) new Object[] { item };
		}
		a[0] = (T) item;
		if (a.length > 1) {
			a[1] = null;
		}
		return a;
	}

	@Override
	public boolean add(E e) {
		if (contains(e)) {
			return false;
		}
		item = e;
		hasItem = true;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c.isEmpty()) {
			return false;
		} else if (!hasItem && (1 == c.size())) {
			return add(c.iterator().next());
		}
		throw new IllegalStateException("cannot add collection contents");
	}

	@Override
	public boolean remove(Object o) {
		if (!contains(o)) {
			return false;
		}
		hasItem = false;
		item = null;
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (!hasItem || !c.contains(item)) {
			return false;
		}
		hasItem = false;
		item = null;
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (!hasItem || c.contains(item)) {
			return false;
		}
		hasItem = false;
		item = null;
		return true;
	}

	@Override
	public int hashCode() {
		return (hasItem && (null != item)) ? item.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if ((null == obj) || !getEqualClass().isAssignableFrom(obj.getClass())) {
			return false;
		}
		return isContentsEqual((Collection) obj);
	}

	protected Class<?> getEqualClass() {
		return Collection.class;
	}

	protected boolean isContentsEqual(Collection<?> other) {
		if (!hasItem) {
			return other.isEmpty();
		} else if (1 != other.size()) {
			return false;
		}
		final Object otherItem = other.iterator().next();
		return (null == item) ? (null == otherItem) : item.equals(otherItem);
	}

	public String toString() {
		if (!hasItem) {
			return "[]";
		} else if (null == item) {
			return "[null]";
		}
		return "[" + item + ']';
	}

	protected class IteratorImpl implements Iterator<E> {
		protected boolean exhausted;

		protected IteratorImpl() {
			// No special construction, exhausted received default value (false) automatically
		}

		@Override
		public boolean hasNext() {
			return hasItem && !exhausted;
		}

		@Override
		public E next() {
			if (!hasItem || exhausted) {
				throw new NoSuchElementException();
			}
			exhausted = true;
			return item;
		}

		@Override
		public void remove() {
			if (!hasItem || !exhausted) {
				throw new IllegalStateException("remove via iterator failed");
			}
			hasItem = false;
			item = null;
		}
	}

}
