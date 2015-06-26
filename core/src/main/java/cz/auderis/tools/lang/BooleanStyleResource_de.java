package cz.auderis.tools.lang;

import java.util.ListResourceBundle;

public final class BooleanStyleResource_de extends ListResourceBundle {

	private static final String[][] LOCALIZATION = {
			{ BooleanStyle.RESOURCE_KEY_TRUE, "ja" },
			{ BooleanStyle.RESOURCE_KEY_FALSE, "nein" },
			{ BooleanStyle.RESOURCE_KEY_TRUE_SHORT, "J" },
			{ BooleanStyle.RESOURCE_KEY_FALSE_SHORT, "N" }
	};

	@Override
	protected Object[][] getContents() {
		return LOCALIZATION;
	}

}
