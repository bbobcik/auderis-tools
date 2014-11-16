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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The type Circular queue.
 * @param <E>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class CircularQueue<E> implements Collection<E> {

	/**
	 * The Capacity.
	 */
	protected final int capacity;

	/**
	 * The Entries.
	 */
	protected final QueueEntry<E>[] entries;

	/**
	 * The Size.
	 */
	protected int size;

	/**
	 * Where the last entry was written; when the queue is empty, its value is -1
	 */
	protected int tailIndex;

	/**
	 * Fast-fail modification detection in iterators
	 */
	protected volatile long version;

	private final ReadWriteLock lock;

	/**
	 * Instantiates a new Circular queue.
	 *
	 * @param maxSize the max size
	 */
	public CircularQueue(int maxSize) {
		super();
		if (maxSize <= 0) {
			throw new IllegalArgumentException("circular queue capacity must be positive");
		}
		this.capacity = maxSize;
		@SuppressWarnings("unchecked")
		final QueueEntry<E>[] newEntries = new QueueEntry[this.capacity];
		this.entries = newEntries;
		this.size = 0;
		this.tailIndex = -1;
		this.version = 0;
		this.lock = new ReentrantReadWriteLock();
	}

	/**
	 * Gets lock.
	 *
	 * @return the lock
	 */
	public ReadWriteLock getLock() {
		return lock;
	}

	@Override
	public int size() {
		lock.readLock().lock();
		try {
			return size;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		lock.readLock().lock();
		try {
			return 0 == size;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void clear() {
		lock.writeLock().lock();
		try {
			++version;
			size = 0;
			tailIndex = -1;
			Arrays.fill(entries, null);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public boolean add(E e) {
		lock.writeLock().lock();
		try {
			++version;
			tailIndex = (1 + tailIndex) % capacity;
			final QueueEntry<E> entry = entries[tailIndex];
			if (null == entry) {
				final QueueEntry<E> newEntry = new QueueEntry<E>(e);
				entries[tailIndex] = newEntry;
				++size;
			} else {
				entry.data = e;
			}
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		lock.writeLock().lock();
		try {
			++version;
			for (E e : c) {
				tailIndex = (1 + tailIndex) % capacity;
				final QueueEntry<E> entry = entries[tailIndex];
				if (null == entry) {
					final QueueEntry<E> newEntry = new QueueEntry<E>(e);
					entries[tailIndex] = newEntry;
					++size;
				} else {
					entry.data = e;
				}
			}
			return true;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Iterator<E> iterator() {
		lock.readLock().lock();
		try {
			if (0 == size) {
				return Collections.emptyIterator();
			}
			return new WrappingIterator();
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(Object o) {
		lock.readLock().lock();
		try {
			if (null == o) {
				for (int i = 0; i < size; ++i) {
					final E entryData = entries[i].data;
					if (null == entries[i].data) {
						return true;
					}
				}
			} else {
				for (int i = 0; i < size; ++i) {
					if (o.equals(entries[i].data)) {
						return true;
					}
				}
			}
			return false;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (null == c) {
			throw new NullPointerException();
		} else if (c.isEmpty()) {
			return true;
		}
		lock.readLock().lock();
		try {
			if (0 == size) {
				return false;
			}
			FIND_TESTED_ITEM:
			for (Object o : c) {
				if (null == o) {
					for (int i = 0; i < size; ++i) {
						final E entryData = entries[i].data;
						if (null == entryData) {
							continue FIND_TESTED_ITEM;
						}
					}
				} else {
					for (int i = 0; i < size; ++i) {
						final E entryData = entries[i].data;
						if (o.equals(entryData)) {
							continue FIND_TESTED_ITEM;
						}
					}
				}
				return false;
			}
			return true;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public Object[] toArray() {
		lock.readLock().lock();
		try {
			final Object[] a = new Object[size];
			for (int i = 0; i < size; ++i) {
				final int srcIdx = (tailIndex + 1 + i) % capacity;
				a[i] = entries[srcIdx].data;
			}
			return a;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public <T> T[] toArray(T[] a) {
		lock.readLock().lock();
		try {
			if (null == a) {
				throw new NullPointerException();
			} else if (a.length < size) {
				final Class<?> type = a.getClass().getComponentType();
				@SuppressWarnings("unchecked")
				final T[] newArray = (T[]) Array.newInstance(type, size);
				a = newArray;
			}
			if (a.length > size) {
				a[size] = null;
			}
			for (int i = 0; i < size; ++i) {
				final int srcIdx = (tailIndex + 1 + i) % capacity;
				@SuppressWarnings("unchecked")
				final T item = (T) entries[srcIdx].data;
				a[i] = item;
			}
			return a;
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * The type Queue entry.
	 * @param <E>  the type parameter
	 */
	static final class QueueEntry<E> {
		/**
		 * The Data.
		 */
		protected E data;

		/**
		 * Instantiates a new Queue entry.
		 *
		 * @param value the value
		 */
		public QueueEntry(E value) {
			this.data = value;
		}
	}

	/**
	 * The type Wrapping iterator.
	 */
	final class WrappingIterator implements Iterator<E> {
		private final long dataVersion;

		// Not constrained into interval [0, capacity-1]!
		private final int lastIdx;

		private int nextIdx;

		/**
		 * Instantiates a new Wrapping iterator.
		 */
		WrappingIterator() {
			dataVersion = CircularQueue.this.version;
			if (size < capacity) {
				nextIdx = 0;
			} else {
				nextIdx = (1 + tailIndex) % capacity;
			}
			lastIdx = size - 1 + nextIdx;
		}

		@Override
		public boolean hasNext() {
			checkConsistency();
			return nextIdx <= lastIdx;
		}

		@Override
		public E next() {
			checkConsistency();
			if (nextIdx > lastIdx) {
				throw new NoSuchElementException();
			}
			final QueueEntry<E> nextEntry = entries[nextIdx % capacity];
			++nextIdx;
			return nextEntry.data;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		private void checkConsistency() {
			if (dataVersion != CircularQueue.this.version) {
				throw new ConcurrentModificationException();
			}
		}
	}

}
