package cz.auderis.tools.lang;

import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BooleanStyleTest {

	@Test
	public void shouldCorrectlyUseCzechLocale() throws Exception {
		// Given
		final Locale loc = new Locale("cs");
		// When
		final BooleanStyle style = BooleanStyle.localized(loc);
		final BooleanStyle shortStyle = style.copy().shortStyle();
		// Then
		assertThat("Czech long true", style.toString(true), is("ano"));
		assertThat("Czech long false", style.toString(false), is("ne"));
		assertThat("Czech short true", shortStyle.toString(true), is("A"));
		assertThat("Czech short false", shortStyle.toString(false), is("N"));
	}

	@Test
	public void shouldCorrectlyUseUnknownLocale() throws Exception {
		// Given
		final Locale loc = new Locale("und");
		// When
		final BooleanStyle style = BooleanStyle.localized(loc);
		final BooleanStyle shortStyle = style.copy().shortStyle();
		// Then
		assertThat(style.toString(true), is("yes"));
		assertThat(style.toString(false), is("no"));
		assertThat(shortStyle.toString(true), is("Y"));
		assertThat(shortStyle.toString(false), is("N"));
	}

}