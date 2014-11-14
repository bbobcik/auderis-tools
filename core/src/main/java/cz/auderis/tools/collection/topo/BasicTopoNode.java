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

package cz.auderis.tools.collection.topo;

import cz.auderis.tools.collection.AlwaysEmptySet;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BasicTopoNode<K, V> implements TopoNode<K, V> {

	private static final Set<?> EMPTY_MUTABLE_SET = new AlwaysEmptySet<Object>();
	private final K key;
	private final V value;
	private Set<K> dependencies;

	public static <K1, V1> BasicTopoNode<K1, V1> create(K1 key, V1 value) {
		if (null == key) {
			throw new NullPointerException();
		}
		return new BasicTopoNode<K1, V1>(key, value);
	}

	public static <K1, V1> BasicTopoNode<K1, V1> createWithDependencies(K1 key, V1 value, Collection<? extends K1> dependencies) {
		if (null == key) {
			throw new NullPointerException();
		}
		final BasicTopoNode<K1, V1> newNode = new BasicTopoNode<K1, V1>(key, value);
		if ((null != dependencies) && !dependencies.isEmpty()) {
			newNode.setDependencies(dependencies);
		}
		return newNode;
	}

	public static <K1, V1> BasicTopoNode<K1, V1> createWithDependencies(K1 key, V1 value, K1... dependencies) {
		if (null == key) {
			throw new NullPointerException();
		}
		final BasicTopoNode<K1, V1> newNode = new BasicTopoNode<K1, V1>(key, value);
		if ((null != dependencies) && 0 != dependencies.length) {
			newNode.setDependencies(Arrays.asList(dependencies));
		}
		return newNode;
	}

	@Override
	public K getTopoKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public final Set<K> getRemainingTopoDependencies() {
		return dependencies;
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof TopoNode)) {
			return false;
		}
		final TopoNode<?, ?> other = (TopoNode<?, ?>) obj;
		if (!Objects.equals(this.key, other.getTopoKey())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return key.toString();
	}

	protected BasicTopoNode(K key, V value) {
		this.key = key;
		this.value = value;
		@SuppressWarnings("unchecked")
		final Set<K> emptySet = (Set<K>) EMPTY_MUTABLE_SET;
		this.dependencies = emptySet;
	}

	protected void setDependencies(Collection<? extends K> deps) {
		// assumes deps != null
		final int newCount = deps.size();
		if (0 == newCount) {
			@SuppressWarnings("unchecked")
			final Set<K> emptySet = (Set<K>) EMPTY_MUTABLE_SET;
			this.dependencies = emptySet;
			return;
		} else if (dependencies == EMPTY_MUTABLE_SET) {
			dependencies = new HashSet<K>(newCount);
		}
		dependencies.clear();
		for (K dep : deps) {
			if (null != dep) {
				dependencies.add(dep);
			}
		}
	}

}
