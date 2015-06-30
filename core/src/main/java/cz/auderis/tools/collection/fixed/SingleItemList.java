package cz.auderis.tools.collection.fixed;

import cz.auderis.tools.collection.fixed.SingleItemCollection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class SingleItemList<E> extends SingleItemCollection<E> implements List<E>, RandomAccess, Serializable {
	private static final long serialVersionUID = 234L;

	public SingleItemList() {
		super();
	}

	public SingleItemList(E item) {
		super(item);
	}

	@Override
	public E get(int index) {
		if (!hasItem || (index < 0) || (index >= 1)) {
			throw new IndexOutOfBoundsException();
		}
		return item;
	}

	@Override
	public int indexOf(Object o) {
		return contains(o) ? 0 : -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		return contains(o) ? 0 : -1;
	}

	@Override
	public ListIterator<E> listIterator() {
		return new ListIteratorImpl(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		if ((index < 0) || (index > size())) {
			throw new IndexOutOfBoundsException();
		}
		return new ListIteratorImpl(index);
	}

	@Override
	public E set(int index, E element) {
		if (!hasItem || (index < 0) || (index >= 1)) {
			throw new IndexOutOfBoundsException();
		}
		final E prevValue = item;
		item = element;
		return prevValue;
	}

	@Override
	public E remove(int index) {
		if (!hasItem || (index < 0) || (index >= 1)) {
			throw new IndexOutOfBoundsException();
		}
		final E prevValue = item;
		hasItem = false;
		item = null;
		return prevValue;
	}

	@Override
	public void add(int index, E element) {
		if (0 != index) {
			throw new IndexOutOfBoundsException();
		}
		super.add(element);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (0 != index) {
			throw new IndexOutOfBoundsException();
		}
		return super.addAll(c);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if ((fromIndex < 0) || (toIndex > size()) || (fromIndex > toIndex)) {
			throw new IndexOutOfBoundsException();
		} else if (hasItem && (fromIndex == toIndex)) {
			return Collections.emptyList();
		}
		return this;
	}

	@Override
	public int hashCode() {
		if (!hasItem) {
			return 1;
		}
		return 31 + ((null != item) ? item.hashCode() : 0);
	}

	@Override
	protected Class<?> getEqualClass() {
		return List.class;
	}

	protected class ListIteratorImpl implements ListIterator<E> {
		private int cursor;
		private boolean stepped;

		protected ListIteratorImpl(int startIndex) {
			cursor = startIndex - 1;
		}

		@Override
		public boolean hasNext() {
			return hasItem && (cursor < 0);
		}

		@Override
		public boolean hasPrevious() {
			return hasItem && (cursor == 0);
		}

		@Override
		public int nextIndex() {
			return (cursor < 0) ? 0 : 1;
		}

		@Override
		public int previousIndex() {
			return (cursor < 0) ? -1 : 0;
		}

		@Override
		public E next() {
			if (!hasItem || (cursor >= 0)) {
				throw new NoSuchElementException();
			}
			cursor = 0;
			stepped = true;
			return item;
		}

		@Override
		public E previous() {
			if (!hasItem || (cursor != 0)) {
				throw new NoSuchElementException();
			}
			cursor = -1;
			stepped = true;
			return item;
		}

		@Override
		public void remove() {
			if (!hasItem || !stepped) {
				throw new IllegalStateException();
			}
			hasItem = false;
			item = null;
			cursor = -1;
		}

		@Override
		public void set(E e) {
			if (!hasItem || !stepped) {
				throw new IllegalStateException();
			}
			item = e;
		}

		@Override
		public void add(E e) {
			throw new UnsupportedOperationException();
		}
	}

}
