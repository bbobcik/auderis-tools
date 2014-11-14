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

import java.util.Collections;
import java.util.Iterator;

public final class Iterators {

	@SuppressWarnings("unchecked")
	public static <T> Iterable<T> asIterable(Iterator<? extends T> baseIter) {
		if (null == baseIter) {
			return Collections.emptyList();
		} else if (baseIter instanceof Iterable<?>) {
			Iterable<T> result = (Iterable<T>) baseIter;
			return result;
		}
		final Iterator<T> iter = (Iterator<T>) baseIter;
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return iter;
			}
		};
	}

	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> emptyIterator() {
		return EMPTY_ITERATOR;
	}

	public static <T> SingleItemIterator<T> singleItemIterator(T item) {
		return new SingleItemIterator<T>(item);
	}

	public static <K, V> ParallelIteratorDecorator<K, V> parallelIterator(Iterator<? extends K> keyIter,
			Iterator<? extends V> valIter) {
		return new ParallelIteratorDecorator<K, V>(keyIter, valIter);
	}

	public static <K, V> ParallelIteratorDecorator<K, V> parallelIterable(Iterable<? extends K> keySource,
			Iterable<? extends V> valSource) {
		return new ParallelIteratorDecorator<K, V>((null != keySource) ? keySource.iterator() : null,
				(null != valSource) ? valSource.iterator() : null);
	}

	public static <T> IndexedIteratorDecorator<T> indexedIterator(Iterator<? extends T> baseIter) {
		return new IndexedIteratorDecorator<T>(baseIter);
	}

	public static <T> IndexedIteratorDecorator<T> indexedIterable(Iterable<? extends T> baseSource) {
		return new IndexedIteratorDecorator<T>((null != baseSource) ? baseSource.iterator() : null);
	}

	public static <T> PushbackIteratorDecorator<T> pushbackIterator(Iterator<? extends T> baseIter) {
		return new PushbackIteratorDecorator<T>(baseIter);
	}

	public static <T> PushbackIteratorDecorator<T> pushbackIterable(Iterable<? extends T> baseSource) {
		return new PushbackIteratorDecorator<T>((null != baseSource) ? baseSource.iterator() : null);
	}

	public static <T> InterleavedIteratorDecorator<T> interleavedIterator(Iterator<? extends T> baseIter,
			Iterator<? extends T> interIter) {
		return new InterleavedIteratorDecorator<T>(baseIter, interIter);
	}

	public static <T> InterleavedIteratorDecorator<T> interleavedIterator(Iterator<? extends T> base, T interValue) {
		return new InterleavedIteratorDecorator<T>(base, interValue);
	}

	public static <T> InterleavedIteratorDecorator<T> interleavedIterable(Iterable<? extends T> baseSrc,
			Iterable<? extends T> interSrc) {
		return new InterleavedIteratorDecorator<T>((null != baseSrc) ? baseSrc.iterator() : null,
				(null != interSrc) ? interSrc.iterator() : null);
	}

	public static <T> InterleavedIteratorDecorator<T> interleavedIterable(Iterable<? extends T> baseSrc, T interValue) {
		return new InterleavedIteratorDecorator<T>((null != baseSrc) ? baseSrc.iterator() : null, interValue);
	}

	private Iterators() {
		// Not supposed to be instantiated
	}

	@SuppressWarnings("rawtypes")
	private static final Iterator EMPTY_ITERATOR = new EmptyIterator();

	protected static final class EmptyIterator<T> implements Iterator<T> {
		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public T next() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
