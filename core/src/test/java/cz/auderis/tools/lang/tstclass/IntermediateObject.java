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

public class IntermediateObject extends ParentObject {

	private int interNormalPrivateField = 0;
	protected int interNormalProtectedField = 0;
	int interNormalPackageField = 0;
	public int interNormalPublicField = 0;

	private final int interFinalPrivateField = 0;
	protected final int interFinalProtectedField = 0;
	final int interFinalPackageField = 0;
	public final int interFinalPublicField = 0;

	static private int interStaticPrivateField = 0;
	static protected int interStaticProtectedField = 0;
	static int interStaticPackageField = 0;
	static public int interStaticPublicField = 0;

	static final private int interStaticFinalPrivateField = 0;
	static final protected int interStaticFinalProtectedField = 0;
	static final int interStaticFinalPackageField = 0;
	static final public int interStaticFinalPublicField = 0;

	private int interNormalPrivateMethod() { return 1; }
	protected int interNormalProtectedMethod() { return 1; }
	int interNormalPackageMethod() { return 1; }
	public int interNormalPublicMethod() { return 1; }

	private final int interFinalPrivateMethod() { return 1; }
	protected final int interFinalProtectedMethod() { return 1; }
	final int interFinalPackageMethod() { return 1; }
	public final int interFinalPublicMethod() { return 1; }

	private static int interStaticPrivateMethod() { return 1; }
	protected static int interStaticProtectedMethod() { return 1; }
	static int interStaticPackageMethod() { return 1; }
	public static int interStaticPublicMethod() { return 1; }

	private static final int interStaticFinalPrivateMethod() { return 1; }
	protected static final int interStaticFinalProtectedMethod() { return 1; }
	static final int interStaticFinalPackageMethod() { return 1; }
	public static final int interStaticFinalPublicMethod() { return 1; }

}
