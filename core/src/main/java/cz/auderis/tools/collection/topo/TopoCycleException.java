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

import java.util.Collection;
import java.util.List;

/**
 * Indication that a cycle has been detected during topological sorting.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class TopoCycleException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final List<?> sortedValues;
	private final Collection<? extends TopoNode<?, ?>> cycleNodes;

	/**
	 * Instantiates a new Topo cycle exception.
	 *
	 * @param msg the msg
	 * @param okValues the ok values
	 * @param failNodes the fail nodes
	 */
	TopoCycleException(String msg, List<?> okValues, Collection<? extends TopoNode<?, ?>> failNodes) {
		super(msg);
		this.sortedValues = okValues;
		this.cycleNodes = failNodes;
	}

	/**
	 * Returns a subset of items that were successfully sorted before the cycle was detected.
	 *
	 * @return list of successfully sorted items
	 */
	public List<?> getSortedValues() {
		return sortedValues;
	}

	/**
	 * Returns a collection of {@code TopoNode} instances that are a part
	 * of detected cyclic structures.
	 *
	 * @return nodes that make up one of detected cycles
	 */
	public Collection<? extends TopoNode<?, ?>> getCycleNodes() {
		return cycleNodes;
	}

}
