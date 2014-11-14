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

package cz.auderis.tools.collection.iterator;

/**
 * Describes a generic encapsulation of iterated element that provides
 * more information about the iteration context.
 *
 * @param <T>  type of iterated elements
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public interface IterationItem<T> {

	/**
	 * Returns the element relevant to the current iteration cycle.
	 *
	 * @return current element
	 */
	T value();

	/**
	 * Index of the current iteration cycle. Before the start of iteration, its value is -1.
	 *
	 * @return index of the current iteration cycle
	 */
	int index();

	/**
	 * Determines whether the current element is the first member of the iterated sequence.
	 *
	 * @return {@code true} if the current element is the first member of the iterated sequence
	 */
	boolean isFirst();

	/**
	 * Determines whether the current element is the last member of the iterated sequence.
	 *
	 * @return {@code true} if the current element is the last member of the iterated sequence
	 */
	boolean isLast();

	/**
	 * Removes the current element.
	 */
	void remove();

}
