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

public abstract class AbstractChaseSequenceCombinationIterator<T> extends AbstractCombinationIterator<T> {

	private final transient BitSet w;
	private final transient int unselectedSize;
	private transient int r;

	protected abstract T createCombination();

	protected AbstractChaseSequenceCombinationIterator(int itemCount, int combinationSize) {
		super(itemCount, combinationSize);
		this.unselectedSize = itemCount - combinationSize;
		this.w = new BitSet(itemCount + 1);
		reset();
	}

	@Override
	public String getCombinationType() {
		return "Chase";
	}

	@Override
	protected BitSet createCombinationBitSet() {
		return new BitSet(itemCount);
	}

	@Override
	protected boolean initialize() {
		w.set(0, itemCount + 1);
		// T=selected bits, S=unselected bits, N=S+T
		if (unselectedSize > 0) {
			// Set bits [0 .. S) to false, bits [S..N) to true
			combinationBits.clear(0, unselectedSize);
			combinationBits.set(unselectedSize, itemCount);
			r = unselectedSize;
		} else {
			combinationBits.set(0, itemCount);
			r = combinationSize;
		}
		return true;
	}

	@Override
	protected boolean prepareNextCombination() {
		if (itemCount <= 1) {
			return false;
		}
		int j = r;
		while (!w.get(j)) {
			w.set(j);
			++j;
			if (j == itemCount) {
				return false;
			}
		}
		// Turn of combinationBits j
		w.clear(j);
		//
		final boolean jOdd = (1 == (j & 1));
		final boolean aBitSet = combinationBits.get(j);
		if (aBitSet) {
			if (jOdd || combinationBits.get(j - 2)) {
				// Case C4
				combinationBits.clear(j);
				combinationBits.set(j - 1);
				if ((r == j) && (j > 1)) {
					r = j - 1;
				} else if (r == j - 1) {
					r = j;
				}
			} else {
				// Case C5 when a[j-2] = 0
				combinationBits.clear(j);
				combinationBits.set(j - 2);
				if (r == j) {
					r = Math.max(1, j - 2);
				} else if (r == j - 2) {
					r = j - 1;
				}
			}
		} else {
			if (!jOdd || combinationBits.get(j - 1)) {
				// Case C6
				combinationBits.set(j);
				combinationBits.clear(j - 1);
				if ((r == j) && (j > 1)) {
					r = j - 1;
				} else if (r == j - 1) {
					r = j;
				}
			} else {
				// Case C7 when a[j-1] = 0
				combinationBits.set(j);
				combinationBits.clear(j - 2);
				if (r == j - 2) {
					r = j;
				} else if (r == j - 1) {
					r = j - 2;
				}
			}
		}
		return true;
	}

}
