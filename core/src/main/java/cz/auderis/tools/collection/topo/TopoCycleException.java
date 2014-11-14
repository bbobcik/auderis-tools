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

public class TopoCycleException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final List<?> sortedValues;
	private final Collection<? extends TopoNode<?, ?>> cycleNodes;

	TopoCycleException(String msg, List<?> okValues, Collection<? extends TopoNode<?, ?>> failNodes) {
		super(msg);
		this.sortedValues = okValues;
		this.cycleNodes = failNodes;
	}

	public List<?> getSortedValues() {
		return sortedValues;
	}

	public Collection<? extends TopoNode<?, ?>> getCycleNodes() {
		return cycleNodes;
	}

}
