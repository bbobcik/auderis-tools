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
 * Simple iterator of arithmetic integer sequence. Removal of iterated elements
 * is not supported.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class IntegerIterator implements Iterator<Integer>, Iterable<Integer> {

	private final int step;
	private final int count;

	/**
	 * Current index within the iterated sequence. Before the iteration
	 * starts its value is -1.
	 */
	protected int idx;

	/**
	 * Current value of iterated sequence. Before the start of iteration
	 * its value is {@code start - step}.
	 */
	protected int value;

	/**
	 * Instantiates a new arithmetic iterator with the given start, number of
	 * iteration steps and value increment.
	 *
	 * @param start initial sequence value
	 * @param count number of steps
	 * @param step increment of the sequence value during one step
	 */
	protected IntegerIterator(int start, int count, int step) {
		if (count < 0) {
			throw new IllegalArgumentException("count must be non-negative");
		}
		this.step = step;
		this.count = count;
		this.idx = -1;
		this.value = start - this.step;
	}

	/**
	 * Constructs a simple {@code count}-step arithmetic sequence that
	 * starts at 0 and increments by 1 in each iteration.
	 *
	 * @param count number of steps
	 * @return new instance of {@code IntegerIterator}
	 */
	public static IntegerIterator withCount(int count) {
		return new IntegerIterator(0, count, 1);
	}

	/**
	 * Constructs a {@code count}-step arithmetic sequence that
	 * starts at 0 and increments by {@code step} in each iteration.
	 *
	 * @param count number of steps
	 * @param step increment of the value in each step
	 * @return new instance of {@code IntegerIterator}
	 */
	public static IntegerIterator withCountAndStep(int count, int step) {
		return new IntegerIterator(0, count, step);
	}

	/**
	 * Constructs a {@code count}-step arithmetic sequence that
	 * starts at {@code start} and increments by {@code step} in each iteration.
	 *
	 * @param start initial sequence value
	 * @param count number of steps
	 * @param step increment of the value in each step
	 * @return new instance of {@code IntegerIterator}
	 */
	public static IntegerIterator withStartCountStep(int start, int count, int step) {
		return new IntegerIterator(start, count, step);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Integer> iterator() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return (idx + 1) < count;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer next() {
		++idx;
		if (idx >= count) {
			throw new NoSuchElementException();
		}
		value += step;
		return value;
	}

	/**
	 * Throws {@link java.lang.UnsupportedOperationException}.
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
