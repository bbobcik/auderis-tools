/*
 * Copyright 2015 Boleslav Bobcik
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class BucketedIntegerSet implements Set<Integer> {
	// Maximum 64 buckets to be able to store flags in a variable of type long
	public static final int MAX_BUCKET_ID_BITS = 6;
	private static final Integer[] EMPTY_INT_ARRAY = { };

	private final int bucketCount;
	private final int bucketIdMask;
	private final Set<Integer>[] bucketSets;
	private long bucketFlags;
	private int itemCount;


	public BucketedIntegerSet(int bits) {
		if ((bits < 1) || (bits > MAX_BUCKET_ID_BITS)) {
			throw new IllegalArgumentException("illegal number of bits for bucket");
		}
		this.bucketCount = 1 << bits;
		this.bucketIdMask = bucketCount - 1;
		this.bucketSets = new Set[bucketCount];
	}

	@Override
	public int size() {
		return itemCount;
	}

	@Override
	public boolean isEmpty() {
		return (0 == itemCount);
	}

	@Override
	public boolean contains(Object o) {
		if (0 == itemCount) {
			return false;
		} else if (!(o instanceof Integer)) {
			return false;
		}
		final int item = (Integer) o;
		final int bucketId = getBucketId(item);
		if (!hasBucket(bucketId)) {
			return false;
		}
		final Set<Integer> bucket = bucketSets[bucketId];
		return bucket.contains(item);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (c.isEmpty()) {
			return true;
		} else if (0 == itemCount) {
			return false;
		}
		for (final Object itemObj : c) {
			if (!(itemObj instanceof Integer)) {
				return false;
			}
			final int item = (Integer) itemObj;
			final int bucketId = getBucketId(item);
			if (!hasBucket(bucketId)) {
				return false;
			}
			final Set<Integer> bucket = bucketSets[bucketId];
			if (!bucket.contains(item)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean add(Integer item) {
		final int bucketId = getBucketId(item);
		final Set<Integer> bucket = getOrCreateBucket(bucketId);
		final boolean added = bucket.add(item);
		if (added) {
			setBucketPresence(bucketId, true);
			++itemCount;
		}
		return added;
	}

	@Override
	public boolean remove(Object itemObj) {
		if (!(itemObj instanceof Integer)) {
			return false;
		}
		final int item = (Integer) itemObj;
		final int bucketId = getBucketId(item);
		if (!hasBucket(bucketId)) {
			return false;
		}
		final Set<Integer> bucket = bucketSets[bucketId];
		final boolean removed = bucket.remove(item);
		if (removed) {
			--itemCount;
			if (bucket.isEmpty()) {
				setBucketPresence(bucketId, false);
			}
		}
		return removed;
	}

	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		boolean result = false;
		for (final Integer item : c) {
			final boolean added = add(item);
			if (added) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = false;
		for (final Object o : c) {
			final boolean removed = remove(o);
			if (removed) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (c.isEmpty()) {
			if (0 != itemCount) {
				clear();
				return true;
			} else {
				return false;
			}
		}
		boolean modified = false;
		int newSize = 0;
		for (int bucketId = 0; bucketId < bucketCount; ++bucketId) {
			final Set<Integer> bucket = bucketSets[bucketId];
			if ((null == bucket) || bucket.isEmpty()) {
				continue;
			}
			final boolean bucketChanged = bucket.retainAll(c);
			final int bucketSize = bucket.size();
			newSize += bucketSize;
			if (bucketChanged) {
				if (0 == bucketSize) {
					setBucketPresence(bucketId, false);
				}
				modified = true;
			}
		}
		if (modified) {
			itemCount = newSize;
		}
		return modified;
	}

	@Override
	public void clear() {
		itemCount = 0;
		bucketFlags = 0L;
		for (final Set<Integer> bucket : bucketSets) {
			if (null != bucket) {
				bucket.clear();
			}
		}
	}

	@Override
	public <T> T[] toArray(T[] a) {
		if (0 == itemCount) {
			if (null != a) {
				if (a.length > 0) {
					a[0] = null;
				}
				return a;
			}
			return (T[]) EMPTY_INT_ARRAY;
		}
		final Integer[] target;
		if ((null == a) || (a.length < itemCount)) {
			target = new Integer[itemCount];
		} else if (a[0] instanceof Integer) {
			target = (Integer[]) a;
		} else {
			throw new IllegalArgumentException("bad array item type");
		}
		int targetIdx = 0;
		for (final Set<Integer> bucket : bucketSets) {
			if (null != bucket) {
				for (final Integer item : bucket) {
					target[targetIdx] = item;
					++targetIdx;
				}
			}
		}
		if (target.length > targetIdx) {
			target[targetIdx] = null;
		}
		return (T[]) target;
	}

	@Override
	public Object[] toArray() {
		if (0 == itemCount) {
			return EMPTY_INT_ARRAY;
		}
		return toArray(null);
	}

	@Override
	public Iterator<Integer> iterator() {
		return new IteratorImpl();
	}

	@Override
	public int hashCode() {
		return itemCount ^ (int) ((bucketFlags >>> (Long.SIZE/2)) ^ bucketFlags);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof Set)) {
			return false;
		}
		final Set<?> other = (Set<?>) obj;
		if (itemCount != other.size()) {
			return false;
		}
		return this.containsAll(other);
	}

	private int getBucketId(int value) {
		return value & bucketIdMask;
	}

	private boolean hasBucket(int bucketId) {
		final int bit = 1 << bucketId;
		return bit == (bucketFlags & bit);
	}

	private void setBucketPresence(int bucketId, boolean present) {
		final int bit = 1 << bucketId;
		if (present) {
			bucketFlags |= bit;
		} else {
			bucketFlags &= ~bit;
		}
	}

	private Set<Integer> getOrCreateBucket(int bucketId) {
		Set<Integer> bucketSet = bucketSets[bucketId];
		if (null == bucketSet) {
			bucketSet = new HashSet<Integer>(4);
			bucketSets[bucketId] = bucketSet;
		}
		return bucketSet;
	}

	class IteratorImpl implements Iterator<Integer> {
		private int bucketId;
		private Iterator<Integer> bucketIterator;

		public IteratorImpl() {
			bucketId = -1;
			bucketIterator = null;
		}

		@Override
		public boolean hasNext() {
			if ((null != bucketIterator) && bucketIterator.hasNext()) {
				return true;
			}
			for (int nextBucketId = 1+bucketId; nextBucketId < bucketCount; ++nextBucketId) {
				final Set<Integer> nextBucket = bucketSets[nextBucketId];
				if ((null != nextBucket) && !nextBucket.isEmpty()) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Integer next() {
			if ((null == bucketIterator) || !bucketIterator.hasNext()) {
				bucketIterator = null;
				for (int id = 1+bucketId; id < bucketCount; ++id) {
					final Set<Integer> nextBucket = bucketSets[id];
					if ((null != nextBucket) && !nextBucket.isEmpty()) {
						bucketIterator = nextBucket.iterator();
						bucketId = id;
						break;
					}
				}
			}
			if (null == bucketIterator) {
				bucketId = bucketCount;
				throw new NoSuchElementException();
			}
			return bucketIterator.next();
		}

		@Override
		public void remove() {
			if (null == bucketIterator) {
				throw new IllegalStateException();
			}
			bucketIterator.remove();
			--itemCount;
			final Set<Integer> bucket = bucketSets[bucketId];
			if (bucket.isEmpty()) {
				setBucketPresence(bucketId, false);
			}
		}
	}

}
