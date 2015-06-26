package cz.auderis.tools.lang;

import java.util.ListResourceBundle;

public final class BooleanStyleResource_cs extends ListResourceBundle {

	private static final String[][] LOCALIZATION = {
			{ BooleanStyle.RESOURCE_KEY_TRUE, "ano" },
			{ BooleanStyle.RESOURCE_KEY_FALSE, "ne" },
			{ BooleanStyle.RESOURCE_KEY_TRUE_SHORT, "A" },
			{ BooleanStyle.RESOURCE_KEY_FALSE_SHORT, "N" }
	};

	@Override
	protected Object[][] getContents() {
		return LOCALIZATION;
	}

}
