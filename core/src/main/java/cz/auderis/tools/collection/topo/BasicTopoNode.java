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
import java.util.Set;

/**
 * Basic implementation of {@link TopoNode}
 * that exposes convenient static methods for easier instantiation.
 *
 * @param <K>   type of key (usually identifier) of the sorted item
 * @param <V>   type of sorted item
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class BasicTopoNode<K, V> implements TopoNode<K, V> {

	private static final Set<?> EMPTY_MUTABLE_SET = new AlwaysEmptySet<Object>();
	private final K key;
	private final V value;
	private Set<K> dependencies;

	/**
	 * Creates an instance of topological sorting node for sorting item
	 * {@code value} identified by key {@code key} without any dependencies.
	 *
	 * @param <K1>  type of key (usually identifier) of the sorted item
	 * @param <V1>  type of sorted item
	 * @param key key of the sorted item
	 * @param value sorted item
	 * @return new node for topological sorting
	 * @throws java.lang.NullPointerException if key is {@code null}
	 */
	public static <K1, V1> BasicTopoNode<K1, V1> create(K1 key, V1 value) {
		if (null == key) {
			throw new NullPointerException();
		}
		return new BasicTopoNode<K1, V1>(key, value);
	}

	/**
	 * Creates an instance of topological sorting node for sorting item
	 * {@code value} identified by key {@code key} with an initial set of dependencies
	 * defined by argument {@code dependencies}.
	 *
	 * @param <K1>  type of key (usually identifier) of the sorted item
	 * @param <V1>  type of sorted item
	 * @param key key of the sorted item
	 * @param value sorted item
	 * @param dependencies collection of keys of items that are prerequisities for the item {@code value}
	 * @return new node for topological sorting
	 * @throws java.lang.NullPointerException if key is {@code null}
	 */
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

	/**
	 * Creates an instance of topological sorting node for sorting item
	 * {@code value} identified by key {@code key} with an initial set of dependencies
	 * defined by argument {@code dependencies}.
	 *
	 * @param <K1>  type of key (usually identifier) of the sorted item
	 * @param <V1>  type of sorted item
	 * @param key key of the sorted item
	 * @param value sorted item
	 * @param dependencies array of keys of items that are prerequisities for the item {@code value}
	 * @return new node for topological sorting
	 * @throws java.lang.NullPointerException if key is {@code null}
	 */
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

	/**
	 * Returns hash code of the item's key.
	 *
	 * @return {@code hashCode()} of the item's key
	 */
	@Override
	public int hashCode() {
		return key.hashCode();
	}

	/**
	 * Determines whether the given object is an instance of {@code TopoNode}
	 * that has a key equal to the key of this node.
	 *
	 * @param obj object for comparison
	 * @return {@code true} if {@code obj} is an instance of {@code TopoNode} and
	 * its key is equal to this node's key; in all other cases {@code false}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof TopoNode)) {
			return false;
		}
		final TopoNode<?, ?> other = (TopoNode<?, ?>) obj;
		final Object otherKey = other.getTopoKey();
		return this.key.equals(otherKey);
	}

	/**
	 * Returns text representation of the item's key.
	 *
	 * @return {@code toString()} of the item's key
	 */
	@Override
	public String toString() {
		return key.toString();
	}

	/**
	 * Instantiates a new node with empty set of dependencies.
	 *
	 * @param key the key
	 * @param value the value
	 */
	protected BasicTopoNode(K key, V value) {
		this.key = key;
		this.value = value;
		@SuppressWarnings("unchecked")
		final Set<K> emptySet = (Set<K>) EMPTY_MUTABLE_SET;
		this.dependencies = emptySet;
	}

	/**
	 * Sets the dependencies of this node.
	 *
	 * @param deps collection of keys of item that are prerequisities
	 * for the item represented by this node
	 */
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
