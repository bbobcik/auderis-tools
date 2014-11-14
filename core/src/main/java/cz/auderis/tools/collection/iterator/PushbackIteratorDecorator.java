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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PushbackIteratorDecorator<T> implements Iterator<T>, Iterable<T> {

	private final Iterator<? extends T> baseIterator;
	private List<T> pushbackBuffer;
	private boolean lastFromBuffer;
	private T lastItem;

	public PushbackIteratorDecorator(Iterator<? extends T> baseIter) {
		if (null != baseIter) {
			baseIterator = baseIter;
		} else {
			baseIterator = Iterators.emptyIterator();
		}
		pushbackBuffer = null;
		lastFromBuffer = false;
		lastItem = null;
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		if ((null != pushbackBuffer) && !pushbackBuffer.isEmpty()) {
			return true;
		}
		return baseIterator.hasNext();
	}

	@Override
	public T next() {
		lastFromBuffer = ((null != pushbackBuffer) && !pushbackBuffer.isEmpty());
		if (lastFromBuffer) {
			lastItem = pushbackBuffer.remove(0);
		} else {
			lastItem = baseIterator.next();
		}
		return lastItem;
	}

	@Override
	public void remove() {
		if (!lastFromBuffer) {
			baseIterator.remove();
		}
	}

	public void pushback() {
		pushback(lastItem);
	}

	public void pushback(T item) {
		if (null == pushbackBuffer) {
			pushbackBuffer = new LinkedList<T>();
		}
		pushbackBuffer.add(item);
	}

	public void pushback(Collection<? extends T> items) {
		if (null == items) {
			return;
		} else if (null == pushbackBuffer) {
			pushbackBuffer = new LinkedList<T>(items);
		} else {
			pushbackBuffer.addAll(items);
		}
	}

	public void clearBuffer() {
		if (null != pushbackBuffer) {
			pushbackBuffer.clear();
		}
	}

}