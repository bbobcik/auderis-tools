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

public class NormalObject {

	private int normalPrivateField = 0;
	protected int normalProtectedField = 0;
	int normalPackageField = 0;
	public int normalPublicField = 0;

	private final int finalPrivateField = 0;
	protected final int finalProtectedField = 0;
	final int finalPackageField = 0;
	public final int finalPublicField = 0;

	static private int staticPrivateField = 0;
	static protected int staticProtectedField = 0;
	static int staticPackageField = 0;
	static public int staticPublicField = 0;

	static final private int staticFinalPrivateField = 0;
	static final protected int staticFinalProtectedField = 0;
	static final int staticFinalPackageField = 0;
	static final public int staticFinalPublicField = 0;

}
