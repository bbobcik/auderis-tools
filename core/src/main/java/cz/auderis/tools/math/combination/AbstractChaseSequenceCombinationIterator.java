package cz.auderis.tools.math.combination;

import java.util.BitSet;

/**
 * Created by Customer on 26.6.2015.
 */
public abstract class AbstractChaseSequenceCombinationIterator<T> implements CombinationIterator<T> {

	protected final int itemCount;
	protected final int combinationSize;
	protected final BitSet combinationBits;
	private final transient BitSet w;
	private final transient int unselectedSize;
	private transient boolean moreCombinations;
	private transient int r;

	protected abstract T createCombination();

	public AbstractChaseSequenceCombinationIterator(int itemCount, int combinationSize) {
		this.itemCount = itemCount;
		this.combinationSize = combinationSize;
		this.unselectedSize = itemCount - combinationSize;
		this.combinationBits = new BitSet(itemCount);
		this.w = new BitSet(itemCount + 1);
		reset();
	}

	@Override
	public int getItemCount() {
		return itemCount;
	}

	@Override
	public int getCombinationSize() {
		return combinationSize;
	}

	@Override
	public boolean hasNext() {
		return moreCombinations;
	}

	@Override
	public void reset() {
		moreCombinations = (combinationSize <= itemCount);
		if (!moreCombinations) {
			return;
		}
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
	}

	@Override
	public T next() {
		final T result = createCombination();
		int j = r;
		while (false == w.get(j)) {
			w.set(j);
			++j;
			if (j == itemCount) {
				moreCombinations = false;
				return result;
			}
		}
		// Turn of bit j
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
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
