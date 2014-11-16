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

package cz.auderis.tools.collection;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The type Loop.
 * @param <T>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public abstract class Loop<T> {

	/**
	 * Value t.
	 *
	 * @return the t
	 */
	public abstract T value();

	/**
	 * Index int.
	 *
	 * @return the int
	 */
	public abstract int index();

	/**
	 * Is first.
	 *
	 * @return the boolean
	 */
	public abstract boolean isFirst();

	/**
	 * Is last.
	 *
	 * @return the boolean
	 */
	public abstract boolean isLast();

	/**
	 * Is removed.
	 *
	 * @return the boolean
	 */
	public abstract boolean isRemoved();

	/**
	 * Remove void.
	 */
	public abstract void remove();

	/**
	 * Replace void.
	 *
	 * @param newValue the new value
	 */
	public abstract void replace(T newValue);

	/**
	 * Stop iteration.
	 */
	public abstract void stopIteration();

	/**
	 * Over iterable.
	 *
	 * @param <E>  the type parameter
	 * @param source the source
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static <E> Iterable<Loop<E>> over(Iterable<? extends E> source) {
		if (null == source) {
			return EMPTY_LOOP;
		} else if (source instanceof List) {
			final List<E> sourceList = (List<E>) source;
			if (sourceList.isEmpty()) {
				return EMPTY_LOOP;
			}
			return new ListLoop<E>(sourceList);
		}
		final Iterator<? extends E> sourceIterator = source.iterator();
		if (!sourceIterator.hasNext()) {
			return EMPTY_LOOP;
		}
		return new BasicIterableLoop<E>(sourceIterator);
	}

	/**
	 * Over iterable.
	 *
	 * @param <E>  the type parameter
	 * @param sourceIterator the source iterator
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static <E> Iterable<Loop<E>> over(Iterator<? extends E> sourceIterator) {
		if ((null == sourceIterator) || !sourceIterator.hasNext()) {
			return EMPTY_LOOP;
		}
		return new BasicIterableLoop<E>(sourceIterator);
	}

	/**
	 * Over array.
	 *
	 * @param <E>  the type parameter
	 * @param sourceArray the source array
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static <E> Iterable<Loop<E>> overArray(E... sourceArray) {
		if ((null == sourceArray) || (0 == sourceArray.length)) {
			return EMPTY_LOOP;
		}
		return new NormalArrayLoop<E>(sourceArray);
	}

	/**
	 * Over array.
	 *
	 * @param sourceArray the source array
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static Iterable<Loop<Integer>> overArray(int... sourceArray) {
		if ((null == sourceArray) || (0 == sourceArray.length)) {
			return EMPTY_LOOP;
		}
		return new IntArrayLoop(sourceArray);
	}

	/**
	 * Over array.
	 *
	 * @param sourceArray the source array
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static Iterable<Loop<Long>> overArray(long... sourceArray) {
		if ((null == sourceArray) || (0 == sourceArray.length)) {
			return EMPTY_LOOP;
		}
		return new LongArrayLoop(sourceArray);
	}

	/**
	 * Over array.
	 *
	 * @param sourceArray the source array
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static Iterable<Loop<Short>> overArray(short... sourceArray) {
		if ((null == sourceArray) || (0 == sourceArray.length)) {
			return EMPTY_LOOP;
		}
		return new ShortArrayLoop(sourceArray);
	}

	/**
	 * Over array.
	 *
	 * @param sourceArray the source array
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static Iterable<Loop<Byte>> overArray(byte... sourceArray) {
		if ((null == sourceArray) || (0 == sourceArray.length)) {
			return EMPTY_LOOP;
		}
		return new ByteArrayLoop(sourceArray);
	}

	/**
	 * Over array.
	 *
	 * @param sourceArray the source array
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static Iterable<Loop<Character>> overArray(char... sourceArray) {
		if ((null == sourceArray) || (0 == sourceArray.length)) {
			return EMPTY_LOOP;
		}
		return new CharArrayLoop(sourceArray);
	}


	/**
	 * Over array.
	 *
	 * @param sourceArray the source array
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static Iterable<Loop<Boolean>> overArray(boolean... sourceArray) {
		if ((null == sourceArray) || (0 == sourceArray.length)) {
			return EMPTY_LOOP;
		}
		return new BooleanArrayLoop(sourceArray);
	}

	/**
	 * Over iterable.
	 *
	 * @param text the text
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static Iterable<Loop<Character>> over(CharSequence text) {
		if ((null == text) || (0 == text.length())) {
			return EMPTY_LOOP;
		}
		return new CharSequenceLoop(text);
	}

	/**
	 * Over range.
	 *
	 * @param minIncl the min incl
	 * @param maxExcl the max excl
	 * @return the iterable
	 */
	@SuppressWarnings("unchecked")
	public static Iterable<Loop<Integer>> overRange(int minIncl, int maxExcl) {
		if (minIncl >= maxExcl) {
			return EMPTY_LOOP;
		}
		return new RangeLoop(minIncl, maxExcl);
	}

	/**
	 * The type Basic iterable loop.
	 * @param <E>  the type parameter
	 */
	static class BasicIterableLoop<E> extends Loop<E> implements Iterator<Loop<E>>, Iterable<Loop<E>> {

		private final transient Iterator<? extends E> baseIterator;
		private transient int index;
		private transient int increment;
		private transient E current;
		private transient boolean last;

		/**
		 * Instantiates a new Basic iterable loop.
		 *
		 * @param baseIterator the base iterator
		 */
		protected BasicIterableLoop(Iterator<? extends E> baseIterator) {
			this.baseIterator = baseIterator;
			this.index = -1;
			this.increment = 1;
			this.current = null;
			this.last = false;
		}

		@Override
		public Iterator<Loop<E>> iterator() {
			return this;
		}

		@Override
		public final boolean hasNext() {
			return (!last) && baseIterator.hasNext();
		}

		@Override
		public final Loop<E> next() {
			if (last) {
				throw new NoSuchElementException(ERR_STOPPED);
			}
			index += increment;
			increment = 1;
			current = baseIterator.next();
			return this;
		}

		@Override
		public final E value() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			return current;
		}

		@Override
		public final int index() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			return index;
		}

		@Override
		public final boolean isFirst() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			return (0 == index);
		}

		@Override
		public final boolean isLast() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			return !hasNext();
		}

		@Override
		public final boolean isRemoved() {
			return 0 == increment;
		}

		@Override
		public final void remove() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			} else if (isRemoved()) {
				throw new IllegalStateException(ERR_REMOVED);
			}
			baseIterator.remove();
			increment = 0;
		}

		@Override
		public void replace(E newValue) {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			throw new UnsupportedOperationException();
		}

		@Override
		public final void stopIteration() {
			last = true;
		}

		@Override
		public final String toString() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			final StringBuilder str = new StringBuilder("[");
			str.append(index);
			str.append("] ");
			str.append(current);
			return str.toString();
		}

		/**
		 * Sets current.
		 *
		 * @param newCurrent the new current
		 */
		protected final void setCurrent(E newCurrent) {
			this.current = newCurrent;
		}
	}

	/**
	 * The type List loop.
	 * @param <E>  the type parameter
	 */
	static final class ListLoop<E> extends BasicIterableLoop<E> {

		@SuppressWarnings("rawtypes")
		private final transient List baseList;

		/**
		 * Instantiates a new List loop.
		 *
		 * @param baseList the base list
		 */
		protected ListLoop(List<E> baseList) {
			super(baseList.iterator());
			this.baseList = baseList;
		}

		@Override
		@SuppressWarnings("unchecked")
		public void replace(E newValue) {
			final int idx = index();
			if (-1 == idx) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			} else if (isRemoved()) {
				throw new IllegalStateException(ERR_REMOVED);
			}
			baseList.set(idx, newValue);
			setCurrent(newValue);
		}
	}

	/**
	 * The type Empty loop.
	 */
	static final class EmptyLoop extends Loop<Object> implements Iterable<Loop<Object>>, Iterator<Loop<Object>> {

		/**
		 * Instantiates a new Empty loop.
		 */
		protected EmptyLoop() {
			// Empty constructor
		}

		@Override
		public Iterator<Loop<Object>> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public Loop<Object> next() {
			throw new NoSuchElementException();
		}

		@Override
		public Object value() {
			throw new IllegalStateException(ERR_NOT_ACTIVE);
		}

		@Override
		public int index() {
			throw new IllegalStateException(ERR_NOT_ACTIVE);
		}

		@Override
		public boolean isFirst() {
			throw new IllegalStateException(ERR_NOT_ACTIVE);
		}

		@Override
		public boolean isLast() {
			throw new IllegalStateException(ERR_NOT_ACTIVE);
		}

		@Override
		public boolean isRemoved() {
			throw new IllegalStateException(ERR_NOT_ACTIVE);
		}

		@Override
		public void remove() {
			throw new IllegalStateException(ERR_NOT_ACTIVE);
		}

		@Override
		public void replace(Object newValue) {
			throw new IllegalStateException(ERR_NOT_ACTIVE);
		}

		@Override
		public void stopIteration() {
			// Do nothing
		}
	}

	/**
	 * The type Abstract array loop.
	 * @param <E>  the type parameter
	 */
	abstract static class AbstractArrayLoop<E> extends Loop<E> implements Iterator<Loop<E>>, Iterable<Loop<E>> {

		private final transient int length;
		/**
		 * The Index.
		 */
		protected transient int index;
		private transient boolean last;

		/**
		 * Instantiates a new Abstract array loop.
		 *
		 * @param length the length
		 */
		protected AbstractArrayLoop(int length) {
			this.length = length;
			this.index = -1;
			this.last = false;
		}

		/**
		 * Gets value.
		 *
		 * @return the value
		 */
		protected abstract E getValue();

		/**
		 * Sets value.
		 *
		 * @param newValue the new value
		 */
		protected abstract void setValue(E newValue);

		@Override
		public final Iterator<Loop<E>> iterator() {
			return this;
		}

		@Override
		public final boolean hasNext() {
			return (!last) && (index < length - 1);
		}

		@Override
		public final Loop<E> next() {
			if (last) {
				throw new NoSuchElementException(ERR_STOPPED);
			}
			++index;
			if (length == index) {
				throw new NoSuchElementException();
			}
			return this;
		}

		@Override
		public E value() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			return getValue();
		}

		@Override
		public final int index() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			return index;
		}

		@Override
		public final boolean isFirst() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			return (0 == index);
		}

		@Override
		public final boolean isLast() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			return (1 + index) == length;
		}

		@Override
		public final boolean isRemoved() {
			return false;
		}

		@Override
		public final void remove() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			throw new UnsupportedOperationException();
		}

		@Override
		public void replace(E newValue) {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			setValue(newValue);
		}

		@Override
		public final void stopIteration() {
			last = true;
		}

		@Override
		public String toString() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			final StringBuilder str = new StringBuilder("[");
			str.append(index);
			str.append("] ");
			str.append(getValue());
			return str.toString();
		}
	}

	/**
	 * The type Normal array loop.
	 * @param <E>  the type parameter
	 */
	static final class NormalArrayLoop<E> extends AbstractArrayLoop<E> {

		private final transient E[] baseArray;

		/**
		 * Instantiates a new Normal array loop.
		 *
		 * @param baseArray the base array
		 */
		protected NormalArrayLoop(E[] baseArray) {
			super(baseArray.length);
			this.baseArray = baseArray;
		}

		@Override
		public E value() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			return baseArray[index];
		}

		@Override
		public void replace(E newValue) {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			baseArray[index] = newValue;
		}

		@Override
		public String toString() {
			if (-1 == index) {
				throw new IllegalStateException(ERR_NOT_ACTIVE);
			}
			final StringBuilder str = new StringBuilder("[");
			str.append(index);
			str.append("] ");
			str.append(baseArray[index]);
			return str.toString();
		}

		@Override
		protected E getValue() {
			// Not used
			return null;
		}

		@Override
		protected void setValue(E newValue) {
			// Not used
		}
	}

	/**
	 * The type Int array loop.
	 */
	static final class IntArrayLoop extends AbstractArrayLoop<Integer> {
		private final transient int[] baseArray;

		/**
		 * Instantiates a new Int array loop.
		 *
		 * @param baseArray the base array
		 */
		protected IntArrayLoop(int[] baseArray) {
			super(baseArray.length);
			this.baseArray = baseArray;
		}

		@Override
		protected Integer getValue() {
			return baseArray[index];
		}

		@Override
		protected void setValue(Integer newValue) {
			baseArray[index] = newValue;
		}
	}

	/**
	 * The type Long array loop.
	 */
	static final class LongArrayLoop extends AbstractArrayLoop<Long> {
		private final transient long[] baseArray;

		/**
		 * Instantiates a new Long array loop.
		 *
		 * @param baseArray the base array
		 */
		protected LongArrayLoop(long[] baseArray) {
			super(baseArray.length);
			this.baseArray = baseArray;
		}

		@Override
		protected Long getValue() {
			return baseArray[index];
		}

		@Override
		protected void setValue(Long newValue) {
			baseArray[index] = newValue;
		}
	}

	/**
	 * The type Short array loop.
	 */
	static final class ShortArrayLoop extends AbstractArrayLoop<Short> {
		private final transient short[] baseArray;

		/**
		 * Instantiates a new Short array loop.
		 *
		 * @param baseArray the base array
		 */
		protected ShortArrayLoop(short[] baseArray) {
			super(baseArray.length);
			this.baseArray = baseArray;
		}

		@Override
		protected Short getValue() {
			return baseArray[index];
		}

		@Override
		protected void setValue(Short newValue) {
			baseArray[index] = newValue;
		}
	}

	/**
	 * The type Byte array loop.
	 */
	static final class ByteArrayLoop extends AbstractArrayLoop<Byte> {
		private final transient byte[] baseArray;

		/**
		 * Instantiates a new Byte array loop.
		 *
		 * @param baseArray the base array
		 */
		protected ByteArrayLoop(byte[] baseArray) {
			super(baseArray.length);
			this.baseArray = baseArray;
		}

		@Override
		protected Byte getValue() {
			return baseArray[index];
		}

		@Override
		protected void setValue(Byte newValue) {
			baseArray[index] = newValue;
		}
	}

	/**
	 * The type Char array loop.
	 */
	static final class CharArrayLoop extends AbstractArrayLoop<Character> {
		private final transient char[] baseArray;

		/**
		 * Instantiates a new Char array loop.
		 *
		 * @param baseArray the base array
		 */
		protected CharArrayLoop(char[] baseArray) {
			super(baseArray.length);
			this.baseArray = baseArray;
		}

		@Override
		protected Character getValue() {
			return baseArray[index];
		}

		@Override
		protected void setValue(Character newValue) {
			baseArray[index] = newValue;
		}
	}

	/**
	 * The type Boolean array loop.
	 */
	static final class BooleanArrayLoop extends AbstractArrayLoop<Boolean> {
		private final transient boolean[] baseArray;

		/**
		 * Instantiates a new Boolean array loop.
		 *
		 * @param baseArray the base array
		 */
		protected BooleanArrayLoop(boolean[] baseArray) {
			super(baseArray.length);
			this.baseArray = baseArray;
		}

		@Override
		protected Boolean getValue() {
			return baseArray[index];
		}

		@Override
		protected void setValue(Boolean newValue) {
			baseArray[index] = newValue;
		}
	}

	/**
	 * The type Char sequence loop.
	 */
	static final class CharSequenceLoop extends AbstractArrayLoop<Character> {
		private final transient CharSequence baseText;

		/**
		 * Instantiates a new Char sequence loop.
		 *
		 * @param baseText the base text
		 */
		protected CharSequenceLoop(CharSequence baseText) {
			super(baseText.length());
			this.baseText = baseText;
		}

		@Override
		protected Character getValue() {
			return baseText.charAt(index);
		}

		@Override
		protected void setValue(Character newValue) {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * The type Range loop.
	 */
	static final class RangeLoop extends AbstractArrayLoop<Integer> {
		private final transient int start;

		/**
		 * Instantiates a new Range loop.
		 *
		 * @param min the min
		 * @param max the max
		 */
		protected RangeLoop(int min, int max) {
			super(max - min);
			this.start = min;
		}

		@Override
		protected Integer getValue() {
			return start + index;
		}

		@Override
		protected void setValue(Integer newValue) {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException(ERR_TRANSIENT);
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException(ERR_TRANSIENT);
	}

	/**
	 * The constant ERR_TRANSIENT.
	 */
	static final String ERR_TRANSIENT = "loop item is transient object, use its value instead";
	/**
	 * The constant ERR_NOT_ACTIVE.
	 */
	static final String ERR_NOT_ACTIVE = "loop not active";
	/**
	 * The constant ERR_STOPPED.
	 */
	static final String ERR_STOPPED = "loop was stopped";
	/**
	 * The constant ERR_REMOVED.
	 */
	static final String ERR_REMOVED = "loop item was already removed";

	@SuppressWarnings("rawtypes")
	private static final Iterable EMPTY_LOOP = new EmptyLoop();

}
