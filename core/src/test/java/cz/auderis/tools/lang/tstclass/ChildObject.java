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

	private int childNormalPrivateMethod() { return 1; }
	protected int childNormalProtectedMethod() { return 1; }
	int childNormalPackageMethod() { return 1; }
	public int childNormalPublicMethod() { return 1; }

	private final int childFinalPrivateMethod() { return 1; }
	protected final int childFinalProtectedMethod() { return 1; }
	final int childFinalPackageMethod() { return 1; }
	public final int childFinalPublicMethod() { return 1; }

	private static int childStaticPrivateMethod() { return 1; }
	protected static int childStaticProtectedMethod() { return 1; }
	static int childStaticPackageMethod() { return 1; }
	public static int childStaticPublicMethod() { return 1; }

	private static final int childStaticFinalPrivateMethod() { return 1; }
	protected static final int childStaticFinalProtectedMethod() { return 1; }
	static final int childStaticFinalPackageMethod() { return 1; }
	public static final int childStaticFinalPublicMethod() { return 1; }

}
