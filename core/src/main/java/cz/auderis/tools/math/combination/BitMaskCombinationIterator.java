package cz.auderis.tools.math.combination;

public class BitMaskCombinationIterator extends AbstractChaseSequenceCombinationIterator<Long> {

	private final long[] bitArray;

	public BitMaskCombinationIterator(long mask, int combinationBits) {
		super(Long.bitCount(mask), combinationBits);
		final int maskBits = getItemCount();
		this.bitArray = new long[maskBits];
		long value = mask;
		for (int i=0; i<maskBits; ++i) {
			final long bit = Long.highestOneBit(value);
			bitArray[i] = bit;
			value ^= bit;
		}
	}

	@Override
	protected Long createCombination() {
		long result = 0L;
		for (int i = combinationBits.nextSetBit(0); i >= 0; i = combinationBits.nextSetBit(i+1)) {
			result |= bitArray[i];
		}
		return result;
	}

}
