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

public class IntegerIterator implements Iterator<Integer>, Iterable<Integer> {

	private final int start;
	private final int step;
	private final int count;
	protected int idx;
	protected int value;

	protected IntegerIterator(int start, int count, int step) {
		if (count < 0) {
			throw new IllegalArgumentException("count must be non-negative");
		}
		this.start = start;
		this.step = step;
		this.count = count;
		this.idx = -1;
		this.value = this.start - this.step;
	}

	public static IntegerIterator withCount(int count) {
		return new IntegerIterator(0, count, 1);
	}

	public static IntegerIterator withCountAndStep(int count, int step) {
		return new IntegerIterator(0, count, step);
	}

	public static IntegerIterator withStartCountStep(int start, int count, int step) {
		return new IntegerIterator(start, count, step);
	}

	@Override
	public Iterator<Integer> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return (idx + 1) < count;
	}

	@Override
	public Integer next() {
		++idx;
		if (idx >= count) {
			throw new NoSuchElementException();
		}
		value += step;
		return value;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
