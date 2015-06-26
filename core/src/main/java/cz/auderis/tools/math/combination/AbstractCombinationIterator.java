/*
 * Copyright 2015 Boleslav Bobcik
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
package cz.auderis.tools.math.combination;

import java.util.BitSet;
import java.util.NoSuchElementException;

public abstract class AbstractCombinationIterator<T> implements CombinationIterator<T> {

	protected final int itemCount;
	protected final int combinationSize;
	protected final BitSet combinationBits;
	private transient boolean moreCombinations;

	protected abstract T createCombination();
	protected abstract BitSet createCombinationBitSet();
	protected abstract boolean initialize();
	protected abstract boolean prepareNextCombination();

	protected AbstractCombinationIterator(int itemCount, int combinationSize) {
		this.itemCount = itemCount;
		this.combinationSize = combinationSize;
		this.combinationBits = createCombinationBitSet();
	}

	@Override
	public int getItemCount() {
		return itemCount;
	}

	@Override
	public int getCombinationSize() {
		return combinationSize;
	}

	public String getCombinationType() {
		return "";
	}

	@Override
	public boolean hasNext() {
		return moreCombinations;
	}

	@Override
	public void reset() {
		if ((combinationSize <= 0) || (combinationSize > itemCount)) {
			moreCombinations = false;
			return;
		}
		moreCombinations = initialize();
	}

	@Override
	public T next() {
		if (!moreCombinations) {
			throw new NoSuchElementException();
		}
		final T result = createCombination();
		if (combinationSize == itemCount) {
			moreCombinations = false;
		} else {
			moreCombinations = prepareNextCombination();
		}
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder("Combination(N=");
		str.append(itemCount);
		str.append(", K=");
		str.append(combinationSize);
		final String type = getCombinationType();
		if ((null != type) && !type.isEmpty()) {
			str.append(", type=");
			str.append(getCombinationType());
		}
		str.append(')');
		return str.toString();
	}

}
