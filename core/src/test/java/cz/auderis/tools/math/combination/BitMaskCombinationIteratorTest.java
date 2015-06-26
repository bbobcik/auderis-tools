package cz.auderis.tools.math.combination;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BitMaskCombinationIteratorTest {

	@Test
	public void shouldCreateSingleBitBytes() throws Exception {
		// Given
		final Set<Byte> combinations = new HashSet<Byte>();
		final BitMaskCombinationIterator combinator = new BitMaskCombinationIterator(0xFFL, 1);
		// When
		while (combinator.hasNext()) {
			final byte combination = (byte) (long) combinator.next();
			final boolean isNew = combinations.add(combination);
			assertTrue(String.format("redundant combination %02X", combination), isNew);
		}
		// Then
		for (int i=0; i<Byte.SIZE; ++i) {
			final byte sample = (byte) (1 << i);
			assertTrue(String.format("combination %02X missing", sample), combinations.contains(sample));
		}
	}

	@Test
	public void shouldCreateSingleBitNibbles() throws Exception {
		// Given
		final Set<Byte> combinations = new HashSet<Byte>();
		final BitMaskCombinationIterator combinator = new BitMaskCombinationIterator(0x0FL, 1);
		// When
		while (combinator.hasNext()) {
			final byte combination = (byte) (long) combinator.next();
			final boolean isNew = combinations.add(combination);
			assertTrue(String.format("redundant combination %02X", combination), isNew);
		}
		// Then
		for (int i=0; i<Byte.SIZE / 2; ++i) {
			final byte sample = (byte) (1 << i);
			assertTrue(String.format("combination %02X missing", sample), combinations.contains(sample));
		}
	}

	@Test
	public void shouldCreate7BitBytes() throws Exception {
		// Given
		final Set<Byte> combinations = new HashSet<Byte>();
		final BitMaskCombinationIterator combinator = new BitMaskCombinationIterator(0xFFL, 7);
		// When
		while (combinator.hasNext()) {
			final byte combination = (byte) (long) combinator.next();
			final boolean isNew = combinations.add(combination);
			assertTrue(String.format("redundant combination %02X", combination), isNew);
		}
		// Then
		for (int i=0; i<Byte.SIZE; ++i) {
			final byte sample = (byte) ~(1 << i);
			assertTrue(String.format("combination %02X missing", sample), combinations.contains(sample));
		}
	}

	@Test
	public void shouldCreate3BitNibbles() throws Exception {
		// Given
		final Set<Byte> combinations = new HashSet<Byte>();
		final BitMaskCombinationIterator combinator = new BitMaskCombinationIterator(0x0FL, 3);
		// When
		while (combinator.hasNext()) {
			final byte combination = (byte) (long) combinator.next();
			final boolean isNew = combinations.add(combination);
			assertTrue(String.format("redundant combination %02X", combination), isNew);
		}
		// Then
		for (int i=0; i<Byte.SIZE / 2; ++i) {
			final byte sample = (byte) (0x0F ^ (1 << i));
			assertTrue(String.format("combination %02X missing", sample), combinations.contains(sample));
		}
	}

	@Test
	public void shouldCreateSingleBitInterleavedNibbles() throws Exception {
		// bits from mask 0101'0101
		// Given
		final Set<Byte> combinations = new HashSet<Byte>();
		final BitMaskCombinationIterator combinator = new BitMaskCombinationIterator(0x55L, 1);
		// When
		while (combinator.hasNext()) {
			final byte combination = (byte) (long) combinator.next();
			final boolean isNew = combinations.add(combination);
			assertTrue(String.format("redundant combination %02X", combination), isNew);
		}
		// Then
		for (int i=0; i<Byte.SIZE / 2; ++i) {
			final byte sample = (byte) (1 << (i*2));
			assertTrue(String.format("combination %02X missing", sample), combinations.contains(sample));
		}
	}

	@Test
	public void shouldCreateArbitrarySubmasks() throws Exception {
		final long mask = 0xA35C7093B1452DFFL;
		final int submaskSize = 3;
		final BitMaskCombinationIterator combinator = new BitMaskCombinationIterator(mask, submaskSize);
		int count = 0;
		while (combinator.hasNext()) {
			final Long submask = combinator.next();
			assertThat("bits not from mask", submaskSize & ~mask, is(0L));
			assertThat("bad submask size", Long.bitCount(submask), is(submaskSize));
			++count;
		}
		assertThat("bad mask count", count, is(5984));
	}

}