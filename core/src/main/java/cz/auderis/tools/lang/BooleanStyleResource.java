package cz.auderis.tools.lang;

import java.util.ListResourceBundle;

public final class BooleanStyleResource extends ListResourceBundle {

	private static final String[][] LOCALIZATION = {
			{ BooleanStyle.RESOURCE_KEY_TRUE, "yes" },
			{ BooleanStyle.RESOURCE_KEY_FALSE, "no" },
			{ BooleanStyle.RESOURCE_KEY_TRUE_SHORT, "Y" },
			{ BooleanStyle.RESOURCE_KEY_FALSE_SHORT, "N" }
	};

	@Override
	protected Object[][] getContents() {
		return LOCALIZATION;
	}

}
