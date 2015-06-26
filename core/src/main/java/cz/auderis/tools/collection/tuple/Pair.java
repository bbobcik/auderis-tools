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

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * The type Pair.
 * @param <L>  the type parameter
 * @param <R>  the type parameter
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public abstract class Pair<L, R> implements Map.Entry<L, R>, Serializable {
	private static final long serialVersionUID = 4954918890077093842L;

	/**
	 * Immutable pair.
	 *
	 * @param <L>  the type parameter
	 * @param <R>  the type parameter
	 * @param left the left
	 * @param right the right
	 * @return the pair
	 */
	public static <L, R> Pair<L, R> immutable(L left, R right) {
		return new ImmutablePair<L, R>(left, right);
	}

	/**
	 * Gets left.
	 *
	 * @return the left
	 */
	public abstract L getLeft();

	/**
	 * Gets right.
	 *
	 * @return the right
	 */
	public abstract R getRight();

	@Override
	public final L getKey() {
		return getLeft();
	}

	@Override
	public final R getValue() {
		return getRight();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if ((null == obj) || !(obj instanceof Map.Entry<?, ?>)) {
			return false;
		}
		final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
		final Object k1 = this.getKey();
		final Object k2 = other.getKey();
		if (!Objects.equals(k1, k2)) {
			return false;
		}
		final Object v1 = this.getValue();
		final Object v2 = other.getValue();
		if (!Objects.equals(v1, v2)) {
			return false;
		}
		return true;
	}

	/**
	 * Generates hash code conforming with {@link java.util.Map.Entry#hashCode()} specification.
	 */
	@Override
	public int hashCode() {
		// see Map.Entry API specification
		return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
	}

	@Override
	public String toString() {
		return "(" + getLeft() + ',' + getRight() + ')';
	}

}