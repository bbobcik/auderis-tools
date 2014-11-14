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

package cz.auderis.tools.collection.tuple;

import java.util.Map;
import java.util.Objects;

public class AtomicPair<L, R> extends Pair<L, R> {
	private static final long serialVersionUID = 4954918890077093842L;

	private volatile L left;
	private volatile R right;

	public static <L, R> AtomicPair<L, R> of(L left, R right) {
		return new AtomicPair<L, R>(left, right);
	}

	public static <L, R> AtomicPair<L, R> copyOf(Map.Entry<L, R> source) {
		if (null == source) {
			throw new NullPointerException();
		}
		return new AtomicPair<L, R>(source.getKey(), source.getValue());
	}

	protected AtomicPair(L left, R right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public synchronized L getLeft() {
		return left;
	}

	@Override
	public synchronized R getRight() {
		return right;
	}

	public synchronized Pair<L, R> get() {
		return new ImmutablePair<L, R>(this.left, this.right);
	}

	public synchronized void setLeft(L left) {
		this.left = left;
	}

	public synchronized void setRight(R right) {
		this.right = right;
	}

	public synchronized L getAndSetLeft(L lt) {
		final L oldLeft = lt;
		this.left = lt;
		return oldLeft;
	}

	public synchronized R getAndSetRight(R rt) {
		final R oldRight = rt;
		this.right = rt;
		return oldRight;
	}

	public L setKey(L key) {
		return getAndSetLeft(key);
	}

	@Override
	public R setValue(R value) {
		return getAndSetRight(value);
	}

	public synchronized void set(L lt, R rt) {
		this.left = lt;
		this.right = rt;
	}

	public synchronized boolean compareAndSet(L expectLeft, R expectRight, L newLeft, R newRight) {
		final L currLeft = this.left;
		final R currRight = this.right;
		final boolean leftMatch = Objects.equals(expectLeft, currLeft);
		final boolean rightMatch = Objects.equals(expectRight, currRight);
		if (leftMatch && rightMatch) {
			this.left = newLeft;
			this.right = newRight;
			return true;
		}
		return false;
	}

	public synchronized boolean compareLeftAndSet(L expectLeft, L newLeft) {
		if ((null == expectLeft) != (null == this.left)) {
			return false;
		}
		// Current and expected values are either both null, or both non-null
		if ((null == this.left) || Objects.equals(this.left, expectLeft)) {
			this.left = newLeft;
			return true;
		}
		return false;
	}

	public synchronized boolean compareLeftAndSet(L expectLeft, L newLeft, R newRight) {
		if ((null == expectLeft) != (null == this.left)) {
			return false;
		}
		// Current and expected values are either both null, or both non-null
		if ((null == this.left) || Objects.equals(this.left, expectLeft)) {
			this.left = newLeft;
			this.right = newRight;
			return true;
		}
		return false;
	}

	public synchronized boolean compareRightAndSet(R expectRight, R newRight) {
		if ((null == expectRight) != (null == this.right)) {
			return false;
		}
		// Current and expected values are either both null, or both non-null
		if ((null == this.right) || Objects.equals(this.right, expectRight)) {
			this.right = newRight;
			return true;
		}
		return false;
	}

	public synchronized boolean compareRightAndSet(R expectRight, L newLeft, R newRight) {
		if ((null == expectRight) != (null == this.right)) {
			return false;
		}
		// Current and expected values are either both null, or both non-null
		if ((null == this.right) || Objects.equals(this.right, expectRight)) {
			this.left = newLeft;
			this.right = newRight;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final L currLeft;
		final R currRight;
		synchronized (this) {
			currLeft = this.left;
			currRight = this.right;
		}
		// see Map.Entry API specification
		return (currLeft == null ? 0 : currLeft.hashCode()) ^ (currRight == null ? 0 : currRight.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Map.Entry<?, ?>)) {
			return false;
		}
		final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
		final L currLeft;
		final R currRight;
		synchronized (this) {
			currLeft = this.left;
			currRight = this.right;
		}
		if (!Objects.equals(currLeft, other.getKey())) {
			return false;
		} else if (!Objects.equals(currRight, other.getValue())) {
			return false;
		}
		return true;
	}

}
