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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

/**
 * The type Always empty set.
 * @param <E>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class AlwaysEmptySet<E> implements Set<E> {

	/**
	 * The constant EMPTY_ARRAY.
	 */
	protected static final Object[] EMPTY_ARRAY = new Object[0];

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return Collections.emptyIterator();
	}

	@Override
	public Object[] toArray() {
		return EMPTY_ARRAY;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		return (T[]) EMPTY_ARRAY;
	}

	@Override
	public boolean add(E e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {
		// No action needed
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if ((null == obj) || !(obj instanceof Set)) {
			return false;
		}
		final Set<?> other = (Set<?>) obj;
		return other.isEmpty();
	}

	@Override
	public String toString() {
		return "EmptySet{}";
	}

}
