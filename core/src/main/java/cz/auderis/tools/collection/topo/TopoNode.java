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

import java.util.Set;

/**
 * Encapsulates all information about one sorted item needed
 * by topological sorting algorithm.
 *
 * @param <K>  type of key (usually identifier) of the sorted item
 * @param <V>  type of sorted item
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public interface TopoNode<K, V> {

	/**
	 * Obtains the key of sorted item represented by this node.
	 *
	 * @return key of sorted item
	 */
	K getTopoKey();

	/**
	 * Obtains the sorted item that is represented by this node.
	 *
	 * @return sorted item
	 */
	V getValue();

	/**
	 * Obtains a set of keys that represent prerequisite items that this
	 * item depends on.
	 * <p>
	 * Provided that the sorted item represented by this
	 * node depends on nodes {@code A}, {@code B}, {@code C}, that have
	 * their respective keys {@code Ak}, {@code Bk}, {@code Ck}, this
	 * method initially returs a set of {@code (Ak, Bk, Ck)}.
	 * <p>
	 * During processing of topological sorting algorithm, the returned set will
	 * be modified; unless a cycle is detected, at the end of the algorithm
	 * this set will be empty.
	 * <p>
	 * <i>Notice that the returned set must be mutable.</i> This implies
	 * that an instance of {@code TopoNode} may not be used in two invocations
	 * of topological sorting.
	 *
	 * @return mutable set of dependencies of the sorted item that were not
	 * yet processed
	 */
	Set<K> getRemainingTopoDependencies();

}
