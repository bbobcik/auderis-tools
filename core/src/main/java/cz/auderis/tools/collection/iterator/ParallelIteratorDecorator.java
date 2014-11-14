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


import cz.auderis.tools.collection.tuple.ImmutablePair;
import cz.auderis.tools.collection.tuple.Pair;

import java.util.Iterator;

public class ParallelIteratorDecorator<I, J> implements Iterator<Pair<I, J>>, Iterable<Pair<I, J>> {

	private final Iterator<? extends I> keyIterator;
	private final Iterator<? extends J> valueIterator;

	public ParallelIteratorDecorator(Iterator<? extends I> keyIter, Iterator<? extends J> valueIter) {
		if (null != keyIter) {
			keyIterator = keyIter;
		} else {
			keyIterator = Iterators.emptyIterator();
		}
		if (null != valueIter) {
			valueIterator = valueIter;
		} else {
			valueIterator = Iterators.emptyIterator();
		}
	}

	@Override
	public Iterator<Pair<I, J>> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return keyIterator.hasNext() || valueIterator.hasNext();
	}

	public boolean hasNextKey() {
		return keyIterator.hasNext();
	}

	public boolean hasNextValue() {
		return valueIterator.hasNext();
	}

	@Override
	public Pair<I, J> next() {
		final I key = keyIterator.hasNext() ? keyIterator.next() : null;
		final J val = valueIterator.hasNext() ? valueIterator.next() : null;
		return ImmutablePair.of(key, val);
	}

	@Override
	public void remove() {
		keyIterator.remove();
		valueIterator.remove();
	}

	public void removeKey() {
		keyIterator.remove();
	}

	public void removeValue() {
		valueIterator.remove();
	}

}
