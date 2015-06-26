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

package cz.auderis.tools.collection.iterator;

import java.util.Arrays;
import java.util.Iterator;

/**
 * The type Iteration items.
 * @param <T>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public final class IterationItems<T> implements Iterable<IterationItem<T>> {

	/**
	 * Of iteration items.
	 *
	 * @param <T>  the type parameter
	 * @param source the source
	 * @return the iteration items
	 */
	public static <T> IterationItems<T> of(Iterable<? extends T> source) {
		final Iterator<? extends T> baseIterator;
		if (null == source) {
			baseIterator = Iterators.emptyIterator();
		} else {
			baseIterator = source.iterator();
		}
		final IterableItemWrapper<T> itemIterator = new IterableItemWrapper<T>(baseIterator);
		return new IterationItems<T>(itemIterator);
	}

	/**
	 * Of iteration items.
	 *
	 * @param <T>  the type parameter
	 * @param source the source
	 * @return the iteration items
	 */
	public static <T> IterationItems<T> of(Iterator<? extends T> source) {
		if (null == source) {
			throw new NullPointerException();
		}
		final IterableItemWrapper<T> itemIterator = new IterableItemWrapper<T>(source);
		return new IterationItems<T>(itemIterator);
	}

	/**
	 * Of array.
	 *
	 * @param <T>  the type parameter
	 * @param source the source
	 * @return the iteration items
	 */
	@SafeVarargs
	public static <T> IterationItems<T> ofArray(T... source) {
		final Iterator<? extends T> baseIterator;
		if (null == source) {
			baseIterator = Iterators.emptyIterator();
		} else {
			baseIterator = Arrays.asList(source).iterator();
		}
		final IterableItemWrapper<T> itemIterator = new IterableItemWrapper<T>(baseIterator);
		return new IterationItems<T>(itemIterator);
	}

	private final Iterator<IterationItem<T>> itemIterator;

	private IterationItems(Iterator<IterationItem<T>> itemIterator) {
		this.itemIterator = itemIterator;
	}

	@Override
	public Iterator<IterationItem<T>> iterator() {
		return itemIterator;
	}

	/**
	 * The type Iterable item wrapper.
	 * @param <E>  the type parameter
	 */
	static final class IterableItemWrapper<E> implements IterationItem<E>, Iterator<IterationItem<E>> {

		private final Iterator<? extends E> baseIterator;
		private int index;
		private E current;

		/**
		 * Instantiates a new Iterable item wrapper.
		 *
		 * @param baseIterator the base iterator
		 */
		public IterableItemWrapper(Iterator<? extends E> baseIterator) {
			this.baseIterator = baseIterator;
			this.index = -1;
			this.current = null;
		}

		@Override
		public E value() {
			return current;
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public boolean isFirst() {
			return (0 == index);
		}

		@Override
		public boolean isLast() {
			return !baseIterator.hasNext();
		}

		@Override
		public void remove() {
			baseIterator.remove();
		}

		@Override
		public boolean hasNext() {
			return baseIterator.hasNext();
		}

		@Override
		public IterationItem<E> next() {
			++index;
			current = baseIterator.next();
			return this;
		}

		@Override
		public int hashCode() {
			throw new UnsupportedOperationException("iteration item is transient object, use its value instead");
		}

		@Override
		public boolean equals(Object obj) {
			throw new UnsupportedOperationException("iteration item is transient object, use its value instead");
		}

		@Override
		public String toString() {
			return "[" + index + "] " + current;
		}
	}

}
