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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Decorator that mixes elements from a decorated iterator with another items. The mixed
 * items may come either from another iterator or be a constant.
 * <p>
 * Example: Given that the base iterator {@code I} would
 * yield items {@code a,b,c,d} and iterator {@code J} would yield items {@code 1,2,3},
 * the interleaved iterator of {@code I,J} will yield sequence {@code a,1,b,2,c,3,d}.
 *
 * @param <T>  common type of iterated elements
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class InterleavedIteratorDecorator<T> implements Iterator<T>, Iterable<T> {

	private final Iterator<? extends T> baseIterator;
	private final Iterator<? extends T> interIterator;
	private final T interValue;
	private boolean lastInterValue;
	private boolean interIterDone;

	/**
	 * Instantiates a new Interleaved iterator decorator.
	 *
	 * @param baseIter the base iter
	 * @param interIter the inter iter
	 * @param remainingValue the remaining value
	 */
	public InterleavedIteratorDecorator(Iterator<? extends T> baseIter, Iterator<? extends T> interIter, T remainingValue) {
		if (null != baseIter) {
			baseIterator = baseIter;
		} else {
			baseIterator = Iterators.emptyIterator();
		}
		if (null != interIter) {
			interIterator = interIter;
			interIterDone = !interIterator.hasNext();
		} else {
			interIterator = null;
			interIterDone = true;
		}
		interValue = remainingValue;
		lastInterValue = true;
	}

	/**
	 * Instantiates a new Interleaved iterator decorator.
	 *
	 * @param baseIter the base iter
	 * @param interIter the inter iter
	 */
	public InterleavedIteratorDecorator(Iterator<? extends T> baseIter, Iterator<? extends T> interIter) {
		this(baseIter, interIter, null);
	}

	/**
	 * Instantiates a new Interleaved iterator decorator.
	 *
	 * @param baseIter the base iter
	 * @param interVal the inter val
	 */
	public InterleavedIteratorDecorator(Iterator<? extends T> baseIter, T interVal) {
		this(baseIter, null, interVal);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return baseIterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next() {
		if (!baseIterator.hasNext()) {
			throw new NoSuchElementException();
		}
		T result;
		if (lastInterValue) {
			result = baseIterator.next();
		} else if (interIterDone) {
			result = interValue;
		} else if (!interIterator.hasNext()) {
			result = interValue;
			interIterDone = true;
		} else {
			result = interIterator.next();
		}
		lastInterValue = !lastInterValue;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		if (!lastInterValue) {
			baseIterator.remove();
		} else if (!interIterDone) {
			interIterator.remove();
		}
	}

	/**
	 * Is between.
	 *
	 * @return the boolean
	 */
	public boolean isBetween() {
		return lastInterValue;
	}

}
