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

import cz.auderis.tools.collection.AlwaysEmptySet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BasicChangeTracker implements ChangeTracker {

	private static final Set<String> PROPERTY_IGNORING_SET = new AlwaysEmptySet<String>();

	private int changeCounter;
	private Set<String> changedProperties;


	public static BasicChangeTracker simple() {
		return new BasicChangeTracker(false);
	}

	public static BasicChangeTracker gatheringPropertyNames() {
		return new BasicChangeTracker(true);
	}

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

	//

	public int updateInt(int origValue, int newValue) {
		return updateInt(origValue, newValue, null);
	}

	public int updateInt(int origValue, int newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	public short updateShort(short origValue, short newValue) {
		return updateShort(origValue, newValue, null);
	}

	public short updateShort(short origValue, short newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	public long updateLong(long origValue, long newValue) {
		return updateLong(origValue, newValue, null);
	}

	public long updateLong(long origValue, long newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	public byte updateByte(byte origValue, byte newValue) {
		return updateByte(origValue, newValue, null);
	}

	public byte updateByte(byte origValue, byte newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	public boolean updateBoolean(boolean origValue, boolean newValue) {
		return updateBoolean(origValue, newValue, null);
	}

	public boolean updateBoolean(boolean origValue, boolean newValue, String propertyName) {
		if (origValue != newValue) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	public String updateString(String origValue, String newValue) {
		return updateString(origValue, newValue, null);
	}

	public String updateString(String origValue, String newValue, String propertyName) {
		if (!Objects.equals(origValue, newValue)) {
			addChangedProperty(propertyName);
		}
		return newValue;
	}

	public <T> T updateObject(T origValue, T newValue) {
		return updateObject(origValue, newValue, null);
	}

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
