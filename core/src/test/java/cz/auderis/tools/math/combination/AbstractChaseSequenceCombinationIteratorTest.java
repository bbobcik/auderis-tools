package cz.auderis.tools.math.combination;

import cz.auderis.tools.math.combination.AbstractChaseSequenceCombinationIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by Customer on 26.6.2015.
 */
@RunWith(Parameterized.class)
public class AbstractChaseSequenceCombinationIteratorTest {

	@Parameterized.Parameters(name = "items={0}, comb={1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ 10, 3, 120L },
				{ 10, 7, 120L },
				{ 13, 5, 1287L },
				{ 13, 8, 1287L },
		});
	}

	@Parameterized.Parameter(0)
	public int totalItems;

	@Parameterized.Parameter(1)
	public int combinationSize;

	@Parameterized.Parameter(2)
	public long expectedCombinationCount;


	@Test
	public void shouldGenerateCorrectCombinationCount() throws Exception {
		// Given
		final CombinationCounter counter = new CombinationCounter(totalItems, combinationSize);

		// When
		while (counter.hasNext()) {
			counter.next();
		}

		// Then
		assertThat("combination count", counter.getCounter(), is(expectedCombinationCount));
	}





	static class CombinationCounter extends AbstractChaseSequenceCombinationIterator<Void> {

		private long counter;

		public CombinationCounter(int itemCount, int combinationSize) {
			super(itemCount, combinationSize);
		}

		public long getCounter() {
			return counter;
		}

		@Override
		public void reset() {
			counter = 0L;
			super.reset();
		}

		@Override
		protected Void createCombination() {
			++counter;
			return null;
		}
	}

}