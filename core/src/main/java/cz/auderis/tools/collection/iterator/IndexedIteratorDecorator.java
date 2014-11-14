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

/**
 * Decorator of another iterator that provides numeric index of the current item
 * within iterated collection. Additionally, a "true index" that denotes current
 * iteration cycle, is available as well.
 * <p>
 * The difference between "normal" and "true" index becomes clear when {@link #remove()}
 * is invoked - in the next iteration cycle, the true index is normally incremented,
 * but the normal index keeps the previous value. Assume the following sample:
 * <pre>
 *     IndexedIteratorDecorator iter = ...
 *     ...
 *     iter.next();
 *     int normalIndex1 = iter.getIndex();
 *     int trueIndex1 = iter.getTrueIndex();
 *     iter.remove();
 *     iter.next();
 *     int normalIndex2 = iter.getIndex();
 *     int trueIndex2 = iter.getTrueIndex();
 * </pre>
 * Due to {@code remove()}, the following relations are valid:
 * <ul>
 *     <li>{@code normalIndex1 == normalIndex2}</li>
 *     <li>{@code trueIndex1 + 1 == trueIndex2}</li>
 * </ul>
 *
 * @param <T>  type of iterated elements
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class IndexedIteratorDecorator<T> implements Iterator<T>, Iterable<T> {

	private final Iterator<? extends T> baseIterator;
	private int index;
	private int trueIndex;
	private boolean afterRemove;

	/**
	 * Creates a new instance of the decorator. If the argument is
	 * {@code null}, it is considered as an empty iterator.
	 *
	 * @param baseIter iterator to be decorated
	 */
	public IndexedIteratorDecorator(Iterator<? extends T> baseIter) {
		if (null != baseIter) {
			baseIterator = baseIter;
		} else {
			baseIterator = Iterators.emptyIterator();
		}
		index = -1;
		trueIndex = -1;
		afterRemove = false;
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
		T result = baseIterator.next();
		if (!afterRemove) {
			++index;
		} else {
			afterRemove = false;
		}
		++trueIndex;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		baseIterator.remove();
		afterRemove = true;
	}

	/**
	 * Returns an index of the current element in the iterated collection. Before iteration
	 * start the value is -1. With each {@code next()} that is not preceded by {@code remove()}
	 * the index value is incremented by 1.
	 *
	 * @return index of the current iterated element
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Returns a "true" index of the current iteration cycle. Before iteration
	 * start the value is -1 and with each {@code next()} its value is incremented
	 * by 1.
	 *
	 * @return index of the current iteration cycle
	 */
	public int getTrueIndex() {
		return trueIndex;
	}

}
