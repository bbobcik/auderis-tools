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
 * The type Single item iterator.
 * @param <T>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class SingleItemIterator<T> implements Iterator<T>, Iterable<T> {

	private T itemReference;
	private boolean exhausted;

	/**
	 * Instantiates a new Single item iterator.
	 */
	public SingleItemIterator() {
		this(null);
	}

	/**
	 * Instantiates a new Single item iterator.
	 *
	 * @param item the item
	 */
	public SingleItemIterator(T item) {
		itemReference = item;
		exhausted = false;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return !exhausted;
	}

	@Override
	public T next() {
		if (exhausted) {
			throw new NoSuchElementException();
		}
		exhausted = true;
		return itemReference;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Reset void.
	 */
	public void reset() {
		exhausted = false;
	}

	/**
	 * Reset void.
	 *
	 * @param newItem the new item
	 */
	public void reset(T newItem) {
		itemReference = newItem;
		exhausted = false;
	}

}
