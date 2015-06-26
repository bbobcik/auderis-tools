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

public abstract class AbstractLexicographicCombinationIterator<T> extends AbstractCombinationIterator<T> {

	protected AbstractLexicographicCombinationIterator(int itemCount, int combinationSize) {
		super(itemCount, combinationSize);
		reset();
	}

	@Override
	public String getCombinationType() {
		return "Lexi";
	}

	@Override
	protected BitSet createCombinationBitSet() {
		return new BitSet(itemCount);
	}

	@Override
	protected boolean initialize() {
		combinationBits.set(0, combinationSize);
		combinationBits.clear(combinationSize, itemCount);
		return true;
	}

	@Override
	protected boolean prepareNextCombination() {
		// L2
		int j = itemCount - 1;
		if (0 == j) {
			return false;
		}
		boolean a_j = combinationBits.get(j - 1);
		boolean a_j1 = combinationBits.get(j);
		while (compareBits(a_j, a_j1) >= 0) {
			--j;
			if (0 == j) {
				return false;
			}
			a_j1 = a_j;
			a_j = combinationBits.get(j - 1);
		}
		// L3
		int l = itemCount;
		boolean a_l = combinationBits.get(l - 1);
		while (compareBits(a_j, a_l) >= 0) {
			--l;
			a_l = combinationBits.get(l - 1);
		}
		combinationBits.set(j - 1, a_l);
		combinationBits.set(l - 1, a_j);
		// L4
		int k = j + 1;
		l = itemCount;
		while (k < l) {
			boolean a_k = combinationBits.get(k - 1);
			a_l = combinationBits.get(l - 1);
			combinationBits.set(k - 1, a_l);
			combinationBits.set(l - 1, a_k);
			++k;
			--l;
		}
		return true;
	}

	private int compareBits(boolean b1, boolean b2) {
		if (b1 == b2) {
			return 0;
		}
		return b1 ? -1 : 1;
	}

}
