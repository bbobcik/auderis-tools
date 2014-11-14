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

public class InterleavedIteratorDecorator<T> implements Iterator<T>, Iterable<T> {

	private final Iterator<? extends T> baseIterator;
	private final Iterator<? extends T> interIterator;
	private final T interValue;
	private boolean lastInterValue;
	private boolean interIterDone;

	public InterleavedIteratorDecorator(Iterator<? extends T> baseIter, Iterator<? extends T> interIter,
			T remainingValue) {
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

	public InterleavedIteratorDecorator(Iterator<? extends T> baseIter, Iterator<? extends T> interIter) {
		this(baseIter, interIter, null);
	}

	public InterleavedIteratorDecorator(Iterator<? extends T> baseIter, T interVal) {
		this(baseIter, null, interVal);
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return baseIterator.hasNext();
	}

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

	@Override
	public void remove() {
		if (!lastInterValue) {
			baseIterator.remove();
		} else if (!interIterDone) {
			interIterator.remove();
		}
	}

	public boolean isBetween() {
		return lastInterValue;
	}

}
