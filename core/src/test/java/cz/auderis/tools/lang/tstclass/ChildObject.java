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

public class ChildObject extends IntermediateObject {

	private int childNormalPrivateField = 0;
	protected int childNormalProtectedField = 0;
	int childNormalPackageField = 0;
	public int childNormalPublicField = 0;

	private final int childFinalPrivateField = 0;
	protected final int childFinalProtectedField = 0;
	final int childFinalPackageField = 0;
	public final int childFinalPublicField = 0;

	static private int childStaticPrivateField = 0;
	static protected int childStaticProtectedField = 0;
	static int childStaticPackageField = 0;
	static public int childStaticPublicField = 0;

	static final private int childStaticFinalPrivateField = 0;
	static final protected int childStaticFinalProtectedField = 0;
	static final int childStaticFinalPackageField = 0;
	static final public int childStaticFinalPublicField = 0;

}
