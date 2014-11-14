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

/**
 * The type Iterators.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public final class Iterators {

	/**
	 * As iterable.
	 *
	 * @param <T>  the type parameter
	 * @param baseIter the base iter
	 * @return the iterable
	 */
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

	/**
	 * Empty iterator.
	 * @param <T>  the type parameter
	 * @return the iterator
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<T> emptyIterator() {
		return EMPTY_ITERATOR;
	}

	/**
	 * Single item iterator.
	 *
	 * @param <T>  the type parameter
	 * @param item the item
	 * @return the single item iterator
	 */
	public static <T> SingleItemIterator<T> singleItemIterator(T item) {
		return new SingleItemIterator<T>(item);
	}

	/**
	 * Parallel iterator.
	 *
	 * @param <K>  the type parameter
	 * @param <V>  the type parameter
	 * @param keyIter the key iter
	 * @param valIter the val iter
	 * @return the parallel iterator decorator
	 */
	public static <K, V> ParallelIteratorDecorator<K, V> parallelIterator(Iterator<? extends K> keyIter,
			Iterator<? extends V> valIter) {
		return new ParallelIteratorDecorator<K, V>(keyIter, valIter);
	}

	/**
	 * Parallel iterable.
	 *
	 * @param <K>  the type parameter
	 * @param <V>  the type parameter
	 * @param keySource the key source
	 * @param valSource the val source
	 * @return the parallel iterator decorator
	 */
	public static <K, V> ParallelIteratorDecorator<K, V> parallelIterable(Iterable<? extends K> keySource,
			Iterable<? extends V> valSource) {
		return new ParallelIteratorDecorator<K, V>((null != keySource) ? keySource.iterator() : null,
				(null != valSource) ? valSource.iterator() : null);
	}

	/**
	 * Indexed iterator.
	 *
	 * @param <T>  the type parameter
	 * @param baseIter the base iter
	 * @return the indexed iterator decorator
	 */
	public static <T> IndexedIteratorDecorator<T> indexedIterator(Iterator<? extends T> baseIter) {
		return new IndexedIteratorDecorator<T>(baseIter);
	}

	/**
	 * Indexed iterable.
	 *
	 * @param <T>  the type parameter
	 * @param baseSource the base source
	 * @return the indexed iterator decorator
	 */
	public static <T> IndexedIteratorDecorator<T> indexedIterable(Iterable<? extends T> baseSource) {
		return new IndexedIteratorDecorator<T>((null != baseSource) ? baseSource.iterator() : null);
	}

	/**
	 * Pushback iterator.
	 *
	 * @param <T>  the type parameter
	 * @param baseIter the base iter
	 * @return the pushback iterator decorator
	 */
	public static <T> PushbackIteratorDecorator<T> pushbackIterator(Iterator<? extends T> baseIter) {
		return new PushbackIteratorDecorator<T>(baseIter);
	}

	/**
	 * Pushback iterable.
	 *
	 * @param <T>  the type parameter
	 * @param baseSource the base source
	 * @return the pushback iterator decorator
	 */
	public static <T> PushbackIteratorDecorator<T> pushbackIterable(Iterable<? extends T> baseSource) {
		return new PushbackIteratorDecorator<T>((null != baseSource) ? baseSource.iterator() : null);
	}

	/**
	 * Interleaved iterator.
	 *
	 * @param <T>  the type parameter
	 * @param baseIter the base iter
	 * @param interIter the inter iter
	 * @return the interleaved iterator decorator
	 */
	public static <T> InterleavedIteratorDecorator<T> interleavedIterator(Iterator<? extends T> baseIter,
			Iterator<? extends T> interIter) {
		return new InterleavedIteratorDecorator<T>(baseIter, interIter);
	}

	/**
	 * Interleaved iterator.
	 *
	 * @param <T>  the type parameter
	 * @param base the base
	 * @param interValue the inter value
	 * @return the interleaved iterator decorator
	 */
	public static <T> InterleavedIteratorDecorator<T> interleavedIterator(Iterator<? extends T> base, T interValue) {
		return new InterleavedIteratorDecorator<T>(base, interValue);
	}

	/**
	 * Interleaved iterable.
	 *
	 * @param <T>  the type parameter
	 * @param baseSrc the base src
	 * @param interSrc the inter src
	 * @return the interleaved iterator decorator
	 */
	public static <T> InterleavedIteratorDecorator<T> interleavedIterable(Iterable<? extends T> baseSrc,
			Iterable<? extends T> interSrc) {
		return new InterleavedIteratorDecorator<T>((null != baseSrc) ? baseSrc.iterator() : null,
				(null != interSrc) ? interSrc.iterator() : null);
	}

	/**
	 * Interleaved iterable.
	 *
	 * @param <T>  the type parameter
	 * @param baseSrc the base src
	 * @param interValue the inter value
	 * @return the interleaved iterator decorator
	 */
	public static <T> InterleavedIteratorDecorator<T> interleavedIterable(Iterable<? extends T> baseSrc, T interValue) {
		return new InterleavedIteratorDecorator<T>((null != baseSrc) ? baseSrc.iterator() : null, interValue);
	}

	private Iterators() {
		// Not supposed to be instantiated
	}

	@SuppressWarnings("rawtypes")
	private static final Iterator EMPTY_ITERATOR = new EmptyIterator();

	/**
	 * The type Empty iterator.
	 * @param <T>  the type parameter
	 */
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
