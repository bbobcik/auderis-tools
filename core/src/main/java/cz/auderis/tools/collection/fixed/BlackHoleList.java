package cz.auderis.tools.collection.fixed;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public class BlackHoleList<E> extends BlackHoleCollection<E> implements List<E>, RandomAccess {
	private static final long serialVersionUID = 123L;

	@Override
	public E get(int index) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public E set(int index, E element) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public void add(int index, E element) {
		// Ignored
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return false;
	}

	@Override
	public E remove(int index) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int indexOf(Object o) {
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		return -1;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if ((fromIndex < 0) || (toIndex > size()) || (fromIndex > toIndex)) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}

	@Override
	public ListIterator<E> listIterator() {
		return (ListIterator<E>) Collections.emptyList().listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return (ListIterator<E>) Collections.emptyList().listIterator(index);
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	protected Class<? extends Collection> getRequiredClass() {
		return List.class;
	}

}
