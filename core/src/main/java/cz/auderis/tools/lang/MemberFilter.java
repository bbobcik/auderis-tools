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

/**
 * Simple predicate for selecting class members.
 */
public interface MemberFilter {

	/**
	 * Determines whether a member can be accepted by caller
	 * for further processing.
	 *
	 * @param m queried member
	 * @param declaringClass class, that contains the member
	 * @return {@code true} if the member can be accepted
	 * @throws java.lang.NullPointerException if any of the arguments is {@code null}
	 */
	boolean accept(Member m, Class<?> declaringClass);

}
