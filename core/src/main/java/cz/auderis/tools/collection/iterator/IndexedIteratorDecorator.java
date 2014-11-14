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

public class IndexedIteratorDecorator<T> implements Iterator<T>, Iterable<T> {

	private final Iterator<? extends T> baseIterator;
	private int index;
	private int trueIndex;
	private boolean afterRemove;

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
		T result = baseIterator.next();
		if (!afterRemove) {
			++index;
		} else {
			afterRemove = false;
		}
		++trueIndex;
		return result;
	}

	@Override
	public void remove() {
		baseIterator.remove();
		afterRemove = true;
	}

	public int getIndex() {
		return index;
	}

	public int getTrueIndex() {
		return trueIndex;
	}

}
