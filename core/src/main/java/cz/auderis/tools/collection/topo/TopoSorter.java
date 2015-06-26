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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Implements topological sorting algorithm that ensures dependencies
 * are listed before their dependants in the resulting list. Formally,
 * for every pair {@code X &lt;-- Y} (i.e. {@code Y} depends on {@code X})
 * the algorithm generates a sorted list {@code L} where
 * {@code L.indexOf(X) &lt; L.indexOf(Y)}.
 * <p>
 * In case the input structure contain one or more cycles, the algorithm
 * does its best to sort the non-cyclic part but throws {@code TopoCycleException}
 * at the end. The sorted items as well as nodes found in cycles can be obtained from
 * the exception.
 * <p>
 * To allow a wide spectrum of applications, the algorithm assumes that every
 * item is identified by a unique key. Dependencies of an item are subsequently
 * defined as a set of other items' keys.
 *
 * @param <K>   type of key (usually identifier) of the sorted item
 * @param <V>   type of sorted item
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public final class TopoSorter<K, V> {

	private static final String ERR_CYCLE = "input dependency structure contains a cycle";

	private Queue<TopoNode<? extends K, ? extends V>> leadNodes;
	private List<TopoNode<? extends K, ? extends V>> dependentNodes;
	private List<? super V> result;
	private int startResultIndex;

	/**
	 * Processes the given collection of {@code TopoNode} nodes and returns the topologically
	 * sorted list of items represented by input nodes or, if a cycle in dependency graph is detected,
	 * throws {@code TopoCycleException}.
	 *
	 * @param <K>   type of key (usually identifier) of the sorted item
	 * @param <V>   type of sorted item
	 * @param sourceNodes collection of source nodes encapsulating sorted items and their dependencies
	 * @return list of topologically sorted items
	 * @throws TopoCycleException if a cycle is detected in dependency relations
	 */
	public static <K, V> List<V> sort(Collection<? extends TopoNode<? extends K, ? extends V>> sourceNodes) throws TopoCycleException {
		if ((null == sourceNodes) || sourceNodes.isEmpty()) {
			return new ArrayList<V>();
		}
		final int nodeCount = sourceNodes.size();
		final List<V> result = new ArrayList<V>(nodeCount);
		final TopoSorter<K, V> sorter = new TopoSorter<K, V>(sourceNodes, result);
		sorter.sort();
		sorter.checkUnprocessedNodes();
		return result;
	}

	/**
	 * Processes the given collection of {@code TopoNode} nodes and appends the topologically
	 * sorted items represented by input nodes to the end of target list. If a cycle in dependency
	 * graph is detected, throws {@code TopoCycleException}; the target list may or may not have
	 * been modified.
	 *
	 * @param <K>   type of key (usually identifier) of the sorted item
	 * @param <V>   type of sorted item
	 * @param sourceNodes collection of source nodes encapsulating sorted items and their dependencies
	 * @param target target list to which the sorted items are appended
	 * @throws TopoCycleException the topo cycle exception
	 * @throws java.lang.NullPointerException if target list is {@code null}
	 */
	public static <K, V> void sortTo(Collection<? extends TopoNode<? extends K, ? extends V>> sourceNodes,
			List<? super V> target) throws TopoCycleException {
		if ((null == sourceNodes) || sourceNodes.isEmpty()) {
			return;
		} else if (null == target) {
			throw new NullPointerException();
		}
		final TopoSorter<K, V> sorter = new TopoSorter<K, V>(sourceNodes, target);
		sorter.sort();
		sorter.checkUnprocessedNodes();
	}

	private TopoSorter(Collection<? extends TopoNode<? extends K, ? extends V>> sourceNodes, List<? super V> result) {
		this.leadNodes = new LinkedList<TopoNode<? extends K, ? extends V>>();
		this.dependentNodes = new LinkedList<TopoNode<? extends K, ? extends V>>();
		this.result = result;
		this.startResultIndex = result.size();
		for (TopoNode<? extends K, ? extends V> node : sourceNodes) {
			if (null == node) {
				// Ignore node
			} else if (node.getRemainingTopoDependencies().isEmpty()) {
				leadNodes.add(node);
			} else {
				dependentNodes.add(node);
			}
		}
	}

	private void sort() {
		while (!leadNodes.isEmpty()) {
			final TopoNode<? extends K, ? extends V> leadNode = leadNodes.remove();
			result.add(leadNode.getValue());
			final K currentKey = leadNode.getTopoKey();
			for (Iterator<TopoNode<? extends K, ? extends V>> nodeIter = dependentNodes.iterator(); nodeIter.hasNext(); ) {
				final TopoNode<? extends K, ? extends V> node = nodeIter.next();
				final Set<? extends K> dependencies = node.getRemainingTopoDependencies();
				dependencies.remove(currentKey);
				if (dependencies.isEmpty()) {
					leadNodes.add(node);
					nodeIter.remove();
				}
			}
		}
	}

	private void checkUnprocessedNodes() throws TopoCycleException {
		if (dependentNodes.isEmpty()) {
			return;
		}
		final StringBuilder msg = new StringBuilder(ERR_CYCLE);
		boolean first = true;
		for (TopoNode<? extends K, ? extends V> node : dependentNodes) {
			if (first) {
				msg.append(" in ");
				first = false;
			} else {
				msg.append(", ");
			}
			msg.append(node.getTopoKey());
			char dependencySeparator = '[';
			for (K remainingDependency : node.getRemainingTopoDependencies()) {
				msg.append(dependencySeparator);
				if ('[' == dependencySeparator) {
					dependencySeparator = ',';
				}
				msg.append(remainingDependency);
			}
			msg.append(']');
		}
		final List<Object> okValues;
		if (result.size() > startResultIndex) {
			final List<? super V> addedItems = result.subList(startResultIndex, result.size());
			okValues = new ArrayList<Object>(addedItems);
		} else {
			okValues = Collections.emptyList();
		}
		final List<TopoNode<? extends K, ? extends V>> failNodes = new ArrayList<TopoNode<? extends K, ? extends V>>(dependentNodes);
		throw new TopoCycleException(msg.toString(), okValues, failNodes);
	}

}
