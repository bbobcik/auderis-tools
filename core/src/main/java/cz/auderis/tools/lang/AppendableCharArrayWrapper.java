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


/**
 * The type Appendable char array wrapper.
 */
public class AppendableCharArrayWrapper implements Appendable, CharSequence {

	private final char[] target;
	private final int capacity;
	private final int minIndex;
	private final int maxIndex;
	private final int length;
	private final char zeroReplacement;
	private int offset;

	/**
	 * Instantiates a new Appendable char array wrapper.
	 *
	 * @param wrappedArray the wrapped array
	 * @param offset the offset
	 * @param length the length
	 */
	public AppendableCharArrayWrapper(char[] wrappedArray, int offset, int length, char zeroReplacement) {
		if (null == wrappedArray) {
			throw new NullPointerException();
		}
		this.target = wrappedArray;
		this.capacity = target.length;
		if ((offset < 0) || (offset >= capacity)) {
			throw new IndexOutOfBoundsException("invalid offset");
		}
		this.minIndex = offset;
		if ((length < 0) || (minIndex + length > capacity)) {
			throw new IllegalArgumentException("invalid length");
		}
		this.length = length;
		this.maxIndex = minIndex + length - 1;
		this.offset = minIndex;
		this.zeroReplacement = zeroReplacement;
	}


	/**
	 * Instantiates a new Appendable char array wrapper.
	 *
	 * @param wrappedArray the wrapped array
	 * @param offset the offset
	 * @param length the length
	 */
	public AppendableCharArrayWrapper(char[] wrappedArray, int offset, int length) {
		this(wrappedArray, offset, length, '\0');
	}

	/**
	 * Instantiates a new Appendable char array wrapper.
	 *
	 * @param wrappedArray the wrapped array
	 */
	public AppendableCharArrayWrapper(char[] wrappedArray) {
		this(wrappedArray, 0, (null != wrappedArray) ? wrappedArray.length : 0, '\0');
	}

	@Override
	public Appendable append(char c) {
		if (offset <= maxIndex) {
			target[offset++] = c;
		}
		return this;
	}

	@Override
	public Appendable append(CharSequence csq) {
		if (null == csq) {
			// Silently ignore
			return this;
		}
		return append(csq, 0, csq.length());
	}

	@Override
	public Appendable append(CharSequence csq, int srcStart, int srcEnd) {
		if ((null == csq) || (srcEnd == srcStart)) {
			// Silently ignore
			return this;
		} else if ((srcStart < 0) || (srcStart >= csq.length())) {
			throw new IndexOutOfBoundsException("bad start index");
		} else if ((srcEnd < srcStart) || (srcEnd > csq.length())) {
			throw new IndexOutOfBoundsException("bad end index");
		}
		int srcLen = srcEnd - srcStart;
		if (offset + srcLen > maxIndex + 1) {
			srcLen = maxIndex + 1 - offset;
		}
		for (int i=0; i < srcLen; ++i) {
			final char c = csq.charAt(i);
			target[offset++] = ('\0' != c) ? c : zeroReplacement;
		}
		return this;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public char charAt(int index) {
		if ((index < 0) || (index >= length)) {
			throw new IndexOutOfBoundsException();
		}
		return target[minIndex + index];
	}

	/**
	 * Sets char at.
	 *
	 * @param index the index
	 * @param c the c
	 */
	public void setCharAt(int index, char c) {
		if ((index < 0) || (index >= length)) {
			throw new IndexOutOfBoundsException();
		}
		target[minIndex + index] = ('\0' != c) ? c : zeroReplacement;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		if ((start < 0) || (start >= length)) {
			throw new IndexOutOfBoundsException("bad start index");
		} else if ((end < start) || (end > length)) {
			throw new IndexOutOfBoundsException("bad end index");
		}
		final int subseqLen = end - start;
		return new AppendableCharArrayWrapper(target, minIndex+start, subseqLen);
	}

	@Override
	public String toString() {
		String result = new String(target, minIndex, length);
		result = result.intern();
		return result;
	}

	/**
	 * Gets current offset.
	 *
	 * @return the current offset
	 */
	public int getCurrentOffset() {
		return offset - minIndex;
	}

	/**
	 * Sets current offset.
	 *
	 * @param newOffset the new offset
	 */
	public void setCurrentOffset(int newOffset) {
		if ((newOffset < 0) || (newOffset >= length)) {
			throw new IndexOutOfBoundsException();
		}
		offset = minIndex + newOffset;
	}

	/**
	 * Clear void.
	 *
	 * @param clearChar the clear char
	 */
	public void clear(char clearChar) {
		if ('\0' == clearChar) {
			clearChar = zeroReplacement;
		}
		for (int i=minIndex; i<=maxIndex; ++i) {
			target[i] = clearChar;
		}
		offset = minIndex;
	}

	/**
	 * Clear void.
	 */
	public void clear() {
		clear(zeroReplacement);
	}

}
