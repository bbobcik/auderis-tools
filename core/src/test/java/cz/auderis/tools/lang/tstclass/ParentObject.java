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

package cz.auderis.tools.lang.tstclass;

public class ParentObject {

	private int parentNormalPrivateField = 0;
	protected int parentNormalProtectedField = 0;
	int parentNormalPackageField = 0;
	public int parentNormalPublicField = 0;

	private final int parentFinalPrivateField = 0;
	protected final int parentFinalProtectedField = 0;
	final int parentFinalPackageField = 0;
	public final int parentFinalPublicField = 0;

	static private int parentStaticPrivateField = 0;
	static protected int parentStaticProtectedField = 0;
	static int parentStaticPackageField = 0;
	static public int parentStaticPublicField = 0;

	static final private int parentStaticFinalPrivateField = 0;
	static final protected int parentStaticFinalProtectedField = 0;
	static final int parentStaticFinalPackageField = 0;
	static final public int parentStaticFinalPublicField = 0;

}
