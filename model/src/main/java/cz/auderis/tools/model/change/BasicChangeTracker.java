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

package cz.auderis.tools.model.change;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Basic implementation of change tracking mechanism that provides implicit change recording.
 * <p>
 * Explicit change recording (i.e. using {@link #markChange()}) works as intended. A new mode,
 * <b>implicit</b> change recording, works as follows:
 * <ul>
 *     <li>{@code TR} is an instance of {@code BasicChangeTracker}.</li>
 *     <li>There is a property {@code Prop} in object {@code A} and {@code XXX} is its type.</li>
 *     <li>{@code NV} is a new value for that property.</li>
 *     <li>The idiom is {@code A.setProp(TR.updateXXX(A.getProp(), NV)}</li>
 * </ul>
 * The {@code updateXXX} group of methods determine whether the new value is different from
 * the old value. If there is a difference, the change is recorded. In any case, the new value
 * is returned and may be fed into an appropriate property setter.
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
public class BasicChangeTracker implements ChangeTracker {

	private static final Set<String> PROPERTY_IGNORING_SET = new BlackHoleSet<String>();

	private int changeCounter;
	private Set<String> changedProperties;

	/**
	 * Creates a new instance of change tracked that does <b>not</b> record names
	 * of changed properties.
	 *
	 * @return new change tracker instance
	 */
	public static BasicChangeTracker simple() {
		return new BasicChangeTracker(false);
	}

	/**
	 * Creates a new instance of change tracker that records names of changed
	 * properties.
	 *
	 * @return new change tracker instance
	 */
	public static BasicChangeTracker gatheringPropertyNames() {
		return new BasicChangeTracker(true);
	}

	/**
	 * Instantiates a new change tracker with optional recording of
	 * changed properties.
	 *
	 * @param enablePropertyGathering enables recording of changed property names
	 */
	protected BasicChangeTracker(boolean enablePropertyGathering) {
		super();
		if (!enablePropertyGathering) {
			changedProperties = PROPERTY_IGNORING_SET;
		}
	}

	@Override
	public void resetChanges() {
		changeCounter = 0;
		if (null != changedProperties) {
			changedProperties.clear();
		}
	}

	@Override
	public boolean isChanged() {
		return (changeCounter > 0);
	}

	@Override
	public int getChangeCount() {
		return changeCounter;
	}

	@Override
	public Set<String> getChangedProperties() {
		if (null == changedProperties) {
			return Collections.emptySet();
		}
		return changedProperties;
	}

	@Override
	public void markChange() {
		++changeCounter;
	}

	@Override
	public void markChange(String propertyName) {
		addChangedProperty(propertyName);
	}

	/**
	 * Equivalent of {@code updateInt(origValue, newValue, null)}.
	 *
	 * @param origValue original value of some property
	 * @param newValue new value for the property
	 * @return new value for the property
	 */
	public int updateInt(int origValue, int newValue) {
		return updateInt(origValue, newValue, null);
	}

	/**
	 * Implicitly records a change of the given property if the provided original
	 * and new values are different.
	 *
	 * @param origValue original value of property with name specified by {@code propertyName}
	 * @param newValue new value for the property
	 * @param propertyName name of the manipulated property
	 * @return new value for the property
	 */
	public int updateInt(int origValue, int newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	/**
	 * Equivalent of {@code updateShort(origValue, newValue, null)}.
	 *
	 * @param origValue original value of some property
	 * @param newValue new value for the property
	 * @return new value for the property
	 */
	public short updateShort(short origValue, short newValue) {
		return updateShort(origValue, newValue, null);
	}

	/**
	 * Implicitly records a change of the given property if the provided original
	 * and new values are different.
	 *
	 * @param origValue original value of property with name specified by {@code propertyName}
	 * @param newValue new value for the property
	 * @param propertyName name of the manipulated property
	 * @return new value for the property
	 */
	public short updateShort(short origValue, short newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	/**
	 * Equivalent of {@code updateLong(origValue, newValue, null)}.
	 *
	 * @param origValue original value of some property
	 * @param newValue new value for the property
	 * @return new value for the property
	 */
	public long updateLong(long origValue, long newValue) {
		return updateLong(origValue, newValue, null);
	}

	/**
	 * Implicitly records a change of the given property if the provided original
	 * and new values are different.
	 *
	 * @param origValue original value of property with name specified by {@code propertyName}
	 * @param newValue new value for the property
	 * @param propertyName name of the manipulated property
	 * @return new value for the property
	 */
	public long updateLong(long origValue, long newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	/**
	 * Equivalent of {@code updateByte(origValue, newValue, null)}.
	 *
	 * @param origValue original value of some property
	 * @param newValue new value for the property
	 * @return new value for the property
	 */
	public byte updateByte(byte origValue, byte newValue) {
		return updateByte(origValue, newValue, null);
	}

	/**
	 * Implicitly records a change of the given property if the provided original
	 * and new values are different.
	 *
	 * @param origValue original value of property with name specified by {@code propertyName}
	 * @param newValue new value for the property
	 * @param propertyName name of the manipulated property
	 * @return new value for the property
	 */
	public byte updateByte(byte origValue, byte newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	/**
	 * Equivalent of {@code updateBoolean(origValue, newValue, null)}.
	 *
	 * @param origValue original value of some property
	 * @param newValue new value for the property
	 * @return new value for the property
	 */
	public boolean updateBoolean(boolean origValue, boolean newValue) {
		return updateBoolean(origValue, newValue, null);
	}

	/**
	 * Implicitly records a change of the given property if the provided original
	 * and new values are different.
	 *
	 * @param origValue original value of property with name specified by {@code propertyName}
	 * @param newValue new value for the property
	 * @param propertyName name of the manipulated property
	 * @return new value for the property
	 */
	public boolean updateBoolean(boolean origValue, boolean newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	/**
	 * Equivalent of {@code updateString(origValue, newValue, null)}.
	 *
	 * @param origValue original value of some property
	 * @param newValue new value for the property
	 * @return new value for the property
	 */
	public String updateString(String origValue, String newValue) {
		return updateString(origValue, newValue, null);
	}

	/**
	 * Implicitly records a change of the given property if the provided original
	 * and new values are different.
	 *
	 * @param origValue original value of property with name specified by {@code propertyName}
	 * @param newValue new value for the property
	 * @param propertyName name of the manipulated property
	 * @return new value for the property
	 */
	public String updateString(String origValue, String newValue, String propertyName) {
		final boolean origDefined = (null != origValue);
		if ((!origDefined && (null != newValue)) || (origDefined && !origValue.equals(newValue))) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	/**
	 * Equivalent of {@code updateObject(origValue, newValue, null)}.
	 *
	 * @param origValue original value of some property
	 * @param newValue new value for the property
	 * @return new value for the property
	 */
	public <T> T updateObject(T origValue, T newValue) {
		return updateObject(origValue, newValue, null);
	}

	/**
	 * Implicitly records a change of the given property if the provided original
	 * and new values are different.
	 *
	 * @param <T> type of the manipulated property
	 * @param origValue original value of property with name specified by {@code propertyName}
	 * @param newValue new value for the property
	 * @param propertyName name of the manipulated property
	 * @return new value for the property
	 */
	public <T> T updateObject(T origValue, T newValue, String propertyName) {
		if (!Objects.equals(origValue, newValue)) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	private void addChangedProperty(String propertyName) {
		++changeCounter;
		if (null == propertyName) {
			return;
		} else if (null == changedProperties) {
			changedProperties = new HashSet<String>();
		}
		changedProperties.add(propertyName);
	}

}
