package cz.auderis.tools.collection.fixed;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class BlackHoleCollection<E> implements Collection<E>, Serializable, Cloneable {
	private static final long serialVersionUID = 123L;
	static final Object[] EMPTY_ARRAY = new Object[0];

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return c.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return (Iterator<E>) Collections.emptyList().iterator();
	}

	@Override
	public Object[] toArray() {
		return EMPTY_ARRAY;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if (0 != a.length) {
			a[0] = null;
		}
		return a;
	}

	@Override
	public void clear() {
		// Nothing to do
	}

	@Override
	public boolean add(E e) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if ((null == obj) || !getRequiredClass().isAssignableFrom(obj.getClass())) {
			return false;
		}
		final Collection<?> other = (Collection<?>) obj;
		return other.isEmpty();
	}

	@Override
	public String toString() {
		return "[]";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new BlackHoleCollection<E>();
	}

	protected Class<? extends Collection> getRequiredClass() {
		return Collection.class;
	}

}
