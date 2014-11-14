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

/**
 * The type Immutable pair.
 * @param <L>  the type parameter
 * @param <R>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public final class ImmutablePair<L, R> extends Pair<L, R> {
	private static final long serialVersionUID = 4954918890077093842L;

	private final L left;
	private final R right;

	/**
	 * Of immutable pair.
	 *
	 * @param <L>  the type parameter
	 * @param <R>  the type parameter
	 * @param left the left
	 * @param right the right
	 * @return the immutable pair
	 */
	public static <L, R> ImmutablePair<L, R> of(L left, R right) {
		return new ImmutablePair<L, R>(left, right);
	}

	/**
	 * Copy of.
	 *
	 * @param <L>  the type parameter
	 * @param <R>  the type parameter
	 * @param source the source
	 * @return the immutable pair
	 */
	public static <L, R> ImmutablePair<L, R> copyOf(Map.Entry<L, R> source) {
		if (null == source) {
			throw new NullPointerException();
		}
		return new ImmutablePair<L, R>(source.getKey(), source.getValue());
	}

	/**
	 * Instantiates a new Immutable pair.
	 *
	 * @param left the left
	 * @param right the right
	 */
	ImmutablePair(L left, R right) {
		super();
		this.left = left;
		this.right = right;
	}

	@Override
	public L getLeft() {
		return left;
	}

	@Override
	public R getRight() {
		return right;
	}

	@Override
	public R setValue(R value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		// see Map.Entry API specification
		return (left == null ? 0 : left.hashCode()) ^ (right == null ? 0 : right.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Map.Entry<?, ?>)) {
			return false;
		}
		final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
		if (!Objects.equals(this.left, other.getKey())) {
			return false;
		} else if (!Objects.equals(this.right, other.getValue())) {
			return false;
		}
		return true;
	}

}