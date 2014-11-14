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

package cz.auderis.tools.change;

import java.util.Set;

/**
 * General interface for individual change tracking.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public interface ChangeTracker {

	/**
	 * Determines whether any change has happened so far.
	 *
	 * @return {@code true} if a change was recorded
	 */
	boolean isChanged();

	/**
	 * Determines number of detected changes
	 *
	 * @return number of detected changes
	 */
	int getChangeCount();

	/**
	 * Returns a set of changed property names.
	 *
	 * @return set of changed property names
	 */
	Set<String> getChangedProperties();

	/**
	 * Marks a change without specific binding to a named property.
	 */
	void markChange();

	/**
	 * Marks a change in a named property.
	 *
	 * @param propertyName name of changed property
	 */
	void markChange(String propertyName);

	/**
	 * Reset the tracker to initial state, where there are no changes
	 * recorded.
	 */
	void resetChanges();

}