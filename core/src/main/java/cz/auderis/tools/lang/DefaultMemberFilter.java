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

package cz.auderis.tools.lang;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * Default predicate for class members that selects all members
 * except static, final or synthetic ones.
 */
public enum DefaultMemberFilter implements MemberFilter {

	ALL_MEMBERS {
		@Override
		public boolean accept(Member m, Class<?> declaringClass, Class<?> queriedClass) {
			return true;
		}
	},

	NORMAL_FIELDS {
		@Override
		public boolean accept(Member m, Class<?> declaringClass, Class<?> queriedClass) {
			final int memberType = m.getModifiers();
			if (Modifier.isStatic(memberType) || Modifier.isFinal(memberType)) {
				return false;
			} else if (m.isSynthetic()) {
				return false;
			}
			return true;
		}
	},

	NORMAL_METHODS {
		@Override
		public boolean accept(Member m, Class<?> declaringClass, Class<?> queriedClass) {
			final int memberType = m.getModifiers();
			if (Modifier.isStatic(memberType)) {
				return false;
			} else if (m.isSynthetic()) {
				return false;
			}
			return true;
		}
	},

	TOP_LEVEL_MEMBERS {
		@Override
		public boolean accept(Member m, Class<?> declaringClass, Class<?> queriedClass) {
			return declaringClass == queriedClass;
		}
	},

}
