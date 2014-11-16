/*
 * Copyright 2014 Boleslav Bobcik
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

package cz.auderis.tools.lang;

public final class BooleanStyle {

	public static Boolean toBoolean(Object obj) {
		if (null == obj) {
			return Boolean.FALSE;
		} else if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof Number) {
			final Number num = (Number) obj;
			return Boolean.valueOf(0 != num.intValue());
		} else if (obj instanceof String) {
			final String textVal = ((String) obj).trim();
			for (BasicStyleImpl style : BasicStyleImpl.values()) {
				if (style.isTrue(textVal)) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	public static Boolean toNullableBoolean(Object obj) {
		if (null == obj) {
			return null;
		} else if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof Number) {
			final Number num = (Number) obj;
			return Boolean.valueOf(0 != num.intValue());
		} else if (obj instanceof String) {
			final String textVal = ((String) obj).trim();
			if (textVal.matches("")) {
				return Boolean.FALSE;
			}
			for (BasicStyleImpl style : BasicStyleImpl.values()) {
				if (style.isTrue(textVal)) {
					return Boolean.TRUE;
				} else if (style.isFalse(textVal)) {
					return Boolean.FALSE;
				}
			}
		}
		return null;
	}

	public static BooleanStyle trueFalse() {
		return new BooleanStyle(BasicStyleImpl.TRUE_FALSE);
	}

	public static BooleanStyle yesNo() {
		return new BooleanStyle(BasicStyleImpl.YES_NO);
	}

	public static BooleanStyle onOff() {
		return new BooleanStyle(BasicStyleImpl.ON_OFF);
	}

	public static BooleanStyle numeric() {
		return new BooleanStyle(BasicStyleImpl.NUMERIC);
	}

	public static BooleanStyle custom(String trueMark, String falseMark) {
		return new BooleanStyle(new CustomStyleImpl(trueMark, falseMark));
	}

	public BooleanStyle shortStyle() {
		delegate = delegate.getShortVersion();
		return this;
	}

	public BooleanStyle longStyle() {
		delegate = delegate.getLongVersion();
		return this;
	}

	public BooleanStyle explicitFalse() {
		blankFalse = false;
		return this;
	}

	public BooleanStyle blankFalse() {
		blankFalse = true;
		return this;
	}

	public Boolean fromString(String value) {
		final String trimmed = (null != value) ? value.trim() : null;
		if ((null == trimmed) || trimmed.isEmpty()) {
			return blankFalse ? Boolean.FALSE : null;
		}
		return Boolean.valueOf(delegate.isTrue(value));
	}


	public String toString(boolean value) {
		if (blankFalse && !value) {
			return "";
		}
		return delegate.getMark(value);
	}

	private BooleanStyle(StyleImpl initialDelegate) {
		this.delegate = initialDelegate;
	}

	private StyleImpl delegate;
	private boolean blankFalse;

	interface StyleImpl {
		String getMark(boolean value);
		boolean isTrue(String textValue);
		boolean isFalse(String textValue);
		StyleImpl getShortVersion();
		StyleImpl getLongVersion();
	}

	static enum BasicStyleImpl implements StyleImpl {
		TRUE_FALSE {
			@Override public String getMark(boolean value) { return value ? "true" : "false"; }
			@Override public boolean isTrue(String textValue) { return "true".equalsIgnoreCase(textValue); }
			@Override public boolean isFalse(String textValue) { return "false".equalsIgnoreCase(textValue); }
			@Override public StyleImpl getShortVersion() { return SHORT_TRUE_FALSE; }
			@Override public StyleImpl getLongVersion() { return this; }
		},

		SHORT_TRUE_FALSE {
			@Override public String getMark(boolean value) { return value ? "t" : "f"; }
			@Override public boolean isTrue(String textValue) { return "t".equalsIgnoreCase(textValue); }
			@Override public boolean isFalse(String textValue) { return "f".equalsIgnoreCase(textValue); }
			@Override public StyleImpl getShortVersion() { return this; }
			@Override public StyleImpl getLongVersion() { return TRUE_FALSE; }
		},

		YES_NO {
			@Override public String getMark(boolean value) { return value ? "yes" : "no"; }
			@Override public boolean isTrue(String textValue) { return "yes".equalsIgnoreCase(textValue); }
			@Override public boolean isFalse(String textValue) { return "no".equalsIgnoreCase(textValue); }
			@Override public StyleImpl getShortVersion() { return SHORT_YES_NO; }
			@Override public StyleImpl getLongVersion() { return this; }
		},

		SHORT_YES_NO {
			@Override public String getMark(boolean value) { return value ? "y" : "n"; }
			@Override public boolean isTrue(String textValue) { return "y".equalsIgnoreCase(textValue); }
			@Override public boolean isFalse(String textValue) { return "n".equalsIgnoreCase(textValue); }
			@Override public StyleImpl getShortVersion() { return this; }
			@Override public StyleImpl getLongVersion() { return TRUE_FALSE; }
		},

		ON_OFF {
			@Override public String getMark(boolean value) { return value ? "on" : "off"; }
			@Override public boolean isTrue(String textValue) { return "on".equalsIgnoreCase(textValue); }
			@Override public boolean isFalse(String textValue) { return "off".equalsIgnoreCase(textValue); }
			@Override public StyleImpl getShortVersion() { return this; }
			@Override public StyleImpl getLongVersion() { return this; }
		},

		NUMERIC {
			@Override public String getMark(boolean value) { return value ? "1" : "0"; }
			@Override public boolean isTrue(String textValue) {
				return (null != textValue) && textValue.matches("^[+-]?\\d*[1-9]\\d*$");
			}
			@Override public boolean isFalse(String textValue) {
				return (null != textValue) && textValue.matches("^-?0+$");
			}
			@Override public StyleImpl getShortVersion() { return this; }
			@Override public StyleImpl getLongVersion() { return this; }
		},
		;
	}

	static class CustomStyleImpl implements StyleImpl {
		private final String trueValue;
		private final String falseValue;

		protected CustomStyleImpl(String tVal, String fVal) {
			this.trueValue = tVal.intern();
			this.falseValue = fVal.intern();
		}

		@Override
		public String getMark(boolean value) {
			return value ? trueValue : falseValue;
		}

		@Override
		public boolean isTrue(String textValue) {
			return trueValue.equalsIgnoreCase(textValue);
		}

		@Override
		public boolean isFalse(String textValue) {
			return falseValue.equalsIgnoreCase(textValue);
		}

		@Override
		public StyleImpl getShortVersion() {
			return this;
		}

		@Override
		public StyleImpl getLongVersion() {
			return this;
		}
	}

}
