package cz.auderis.tools.math.combination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Customer on 26.6.2015.
 */
@RunWith(Parameterized.class)
public class BasicCombinationIteratorTest {

	@Parameterized.Parameters(name = "items={0}, comb={1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{ 10, 3, 120L },
				{ 10, 7, 120L },
				{ 13, 5, 1287L },
				{ 13, 8, 1287L },
				{ 1, 1, 1L },
		});
	}

	@Parameterized.Parameter(0)
	public int totalItems;

	@Parameterized.Parameter(1)
	public int combinationSize;

	@Parameterized.Parameter(2)
	public long expectedCombinationCount;


	@Test
	public void shouldGenerateCorrectLexicographicCombinationCount() throws Exception {
		// Given
		final AtomicLong counter = new AtomicLong(0L);
		final CombinationIterator<Void> iterator = new AbstractLexicographicCombinationIterator<Void>(totalItems,
				combinationSize) {
			@Override
			protected Void createCombination() {
				counter.incrementAndGet();
				return null;
			}
		};
		// When
		while (iterator.hasNext()) {
			iterator.next();
		}
		// Then
		assertThat("lexicographic combination count", counter.get(), is(expectedCombinationCount));
	}

	@Test
	public void shouldGenerateCorrectChaseCombinationCount() throws Exception {
		// Given
		final AtomicLong counter = new AtomicLong(0L);
		final CombinationIterator<Void> iterator = new AbstractChaseSequenceCombinationIterator<Void>(totalItems,
				combinationSize) {
			@Override
			protected Void createCombination() {
				counter.incrementAndGet();
				return null;
			}
		};
		// When
		while (iterator.hasNext()) {
			iterator.next();
		}
		// Then
		assertThat("chase combination count", counter.get(), is(expectedCombinationCount));
	}

}