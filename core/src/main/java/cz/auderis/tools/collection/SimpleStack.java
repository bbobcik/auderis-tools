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

import java.util.NoSuchElementException;

/**
 * The type Simple stack.
 * @param <T>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class SimpleStack<T> {

	private T value;
	private Item<T> head;
	private final Object[] pushArray;
	private int pushIdx;

	/**
	 * Instantiates a new Simple stack.
	 */
	public SimpleStack() {
		this(DEFAULT_PUSH_ARRAY_SIZE);
	}

	/**
	 * Instantiates a new Simple stack.
	 *
	 * @param arrayCapacity the array capacity
	 */
	public SimpleStack(int arrayCapacity) {
		if (arrayCapacity < 0) {
			throw new IllegalArgumentException(ERR_NEG_CAPACITY);
		}
		pushArray = new Object[arrayCapacity];
	}

	/**
	 * Is empty.
	 *
	 * @return the boolean
	 */
	public boolean isEmpty() {
		return (EMPTY_IDX == pushIdx);
	}

	/**
	 * Get t.
	 *
	 * @return the t
	 */
	public T get() {
		if (EMPTY_IDX == pushIdx) {
			throw new NoSuchElementException(ERR_EMPTY);
		}
		return value;
	}

	/**
	 * Pop t.
	 *
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public T pop() {
		if (EMPTY_IDX == pushIdx) {
			throw new NoSuchElementException(ERR_EMPTY);
		}
		final T result = value;
		if (null != head) {
			value = head.value;
			head = head.next;
		} else {
			if (SINGLE_IDX != pushIdx) {
				value = (T) pushArray[pushIdx];
			}
			--pushIdx;
		}
		return result;
	}

	/**
	 * Push void.
	 *
	 * @param newValue the new value
	 */
	public void push(T newValue) {
		if ((null != head) || (pushArray.length - 1 == pushIdx)) {
			head = new Item<T>(value, head);
			value = newValue;
		} else {
			++pushIdx;
			if (pushIdx > SINGLE_IDX) {
				pushArray[pushIdx] = value;
			}
			value = newValue;
		}
	}

	/**
	 * Replace t.
	 *
	 * @param newValue the new value
	 * @return the t
	 */
	public T replace(T newValue) {
		if (EMPTY_IDX == pushIdx) {
			throw new NoSuchElementException(ERR_EMPTY);
		}
		final T oldValue = value;
		value = newValue;
		return oldValue;
	}

	/**
	 * Clear void.
	 */
	public void clear() {
		value = null;
		head = null;
		pushIdx = EMPTY_IDX;
	}

	/**
	 * The type Item.
	 * @param <T>  the type parameter
	 */
	static class Item<T> {
		/**
		 * The Value.
		 */
		protected final T value;
		/**
		 * The Next.
		 */
		protected final Item<T> next;

		/**
		 * Instantiates a new Item.
		 *
		 * @param value the value
		 * @param next the next
		 */
		protected Item(T value, Item<T> next) {
			this.value = value;
			this.next = next;
		}
	}

	private static final int SINGLE_IDX = -1;
	private static final int EMPTY_IDX = SINGLE_IDX - 1;
	private static final int DEFAULT_PUSH_ARRAY_SIZE = 2;
	private static final String ERR_EMPTY = "stack is empty";
	private static final String ERR_NEG_CAPACITY = "stack array capacity must be non-negative";

}
