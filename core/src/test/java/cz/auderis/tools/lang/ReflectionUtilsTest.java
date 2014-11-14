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

import cz.auderis.tools.lang.tstclass.ChildObject;
import cz.auderis.tools.lang.tstclass.NormalObject;
import cz.auderis.tools.lang.tstclass.otherpkg.OtherPkgChildObject;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ReflectionUtilsTest {

	@Test
	public void shouldReturnNormalFieldsFromNormalClass() throws Exception {
		final List<Field> normalFields = ReflectionUtils.getNormalFields(NormalObject.class);
		assertThat(normalFields, notNullValue());
		final Set<String> fieldNames = ReflectionUtils.getMemberNames(normalFields);
		assertThat(fieldNames, hasItems(nameCombinations("normal", "Field")));
		assertThat(fieldNames, not(hasItems(nameCombinations("final", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("static", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("staticFinal", "Field"))));
	}

	@Test
	public void shouldReturnNormalFieldsFromChildClass() throws Exception {
		final List<Field> normalFields = ReflectionUtils.getNormalFields(ChildObject.class);
		assertThat(normalFields, notNullValue());
		final Set<String> fieldNames = ReflectionUtils.getMemberNames(normalFields);
		//
		assertThat(fieldNames, hasItems(nameCombinations("childNormal", "Field")));
		assertThat(fieldNames, hasItems(nameCombinations("interNormal", "Field")));
		assertThat(fieldNames, hasItems(nameCombinations("parentNormal", "Field")));
		//
		assertThat(fieldNames, not(hasItems(nameCombinations("childFinal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("childStatic", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("childStaticFinal", "Field"))));
		//
		assertThat(fieldNames, not(hasItems(nameCombinations("interFinal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("interStatic", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("interStaticFinal", "Field"))));
		//
		assertThat(fieldNames, not(hasItems(nameCombinations("parentFinal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("parentStatic", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("parentStaticFinal", "Field"))));
	}

	@Test
	public void shouldReturnTopLevelNormalFieldsFromChildClass() throws Exception {
		final List<Field> normalFields = ReflectionUtils.getFields(ChildObject.class,
				DefaultMemberFilter.NORMAL_FIELDS, DefaultMemberFilter.TOP_LEVEL_MEMBERS
		);
		assertThat(normalFields, notNullValue());
		final Set<String> fieldNames = ReflectionUtils.getMemberNames(normalFields);
		//
		assertThat(fieldNames, hasItems(nameCombinations("childNormal", "Field")));
		assertThat(fieldNames, not(hasItems(nameCombinations("interNormal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("parentNormal", "Field"))));
		//
		assertThat(fieldNames, not(hasItems(nameCombinations("childFinal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("childStatic", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("childStaticFinal", "Field"))));
		//
		assertThat(fieldNames, not(hasItems(nameCombinations("interFinal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("interStatic", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("interStaticFinal", "Field"))));
		//
		assertThat(fieldNames, not(hasItems(nameCombinations("parentFinal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("parentStatic", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("parentStaticFinal", "Field"))));
	}

	@Test
	public void shouldReturnNormalFieldsFromChildClassFromOtherPackage() throws Exception {
		final List<Field> normalFields = ReflectionUtils.getNormalFields(OtherPkgChildObject.class);
		assertThat(normalFields, notNullValue());
		final Set<String> fieldNames = ReflectionUtils.getMemberNames(normalFields);
		//
		assertThat(fieldNames, hasItems(nameCombinations("childNormal", "Field")));
		assertThat(fieldNames, hasItems(nameCombinations("interNormal", "Field")));
		assertThat(fieldNames, hasItems(nameCombinations("parentNormal", "Field")));
		//
		assertThat(fieldNames, not(hasItems(nameCombinations("childFinal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("childStatic", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("childStaticFinal", "Field"))));
		//
		assertThat(fieldNames, not(hasItems(nameCombinations("interFinal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("interStatic", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("interStaticFinal", "Field"))));
		//
		assertThat(fieldNames, not(hasItems(nameCombinations("parentFinal", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("parentStatic", "Field"))));
		assertThat(fieldNames, not(hasItems(nameCombinations("parentStaticFinal", "Field"))));
	}

	@Test
	public void shouldReturnNormalMethodsFromNormalClass() throws Exception {
		final List<Method> normalMethods = ReflectionUtils.getNormalMethods(NormalObject.class);
		assertThat(normalMethods, notNullValue());
		final Set<String> methodNames = ReflectionUtils.getMemberNames(normalMethods);
		assertThat(methodNames, hasItems(nameCombinations("normal", "Method")));
		assertThat(methodNames, hasItems(nameCombinations("final", "Method")));
		assertThat(methodNames, not(hasItems(nameCombinations("static", "Field"))));
		assertThat(methodNames, not(hasItems(nameCombinations("staticFinal", "Field"))));
	}

	@Test
	public void shouldReturnNormalMethodsFromChildClass() throws Exception {
		final List<Method> normalMethods = ReflectionUtils.getNormalMethods(ChildObject.class);
		assertThat(normalMethods, notNullValue());
		final Set<String> methodNames = ReflectionUtils.getMemberNames(normalMethods);
		//
		assertThat(methodNames, hasItems(nameCombinations("childNormal", "Method")));
		assertThat(methodNames, hasItems(nameCombinations("interNormal", "Method")));
		assertThat(methodNames, hasItems(nameCombinations("parentNormal", "Method")));
		//
		assertThat(methodNames, hasItems(nameCombinations("childFinal", "Method")));
		assertThat(methodNames, hasItems(nameCombinations("interFinal", "Method")));
		assertThat(methodNames, hasItems(nameCombinations("parentFinal", "Method")));
		//
		assertThat(methodNames, not(hasItems(nameCombinations("childStatic", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("interStatic", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("parentStatic", "Method"))));
		//
		assertThat(methodNames, not(hasItems(nameCombinations("childStaticFinal", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("interStaticFinal", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("parentStaticFinal", "Method"))));
	}

	@Test
	public void shouldReturnTopLevelNormalMethodsFromChildClass() throws Exception {
		final List<Method> normalMethods = ReflectionUtils.getMethods(ChildObject.class,
				DefaultMemberFilter.NORMAL_METHODS, DefaultMemberFilter.TOP_LEVEL_MEMBERS);
		assertThat(normalMethods, notNullValue());
		final Set<String> methodNames = ReflectionUtils.getMemberNames(normalMethods);
		//
		assertThat(methodNames, hasItems(nameCombinations("childNormal", "Method")));
		assertThat(methodNames, not(hasItems(nameCombinations("interNormal", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("parentNormal", "Method"))));
		//
		assertThat(methodNames, hasItems(nameCombinations("childFinal", "Method")));
		assertThat(methodNames, not(hasItems(nameCombinations("interFinal", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("parentFinal", "Method"))));
		//
		assertThat(methodNames, not(hasItems(nameCombinations("childStatic", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("interStatic", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("parentStatic", "Method"))));
		//
		assertThat(methodNames, not(hasItems(nameCombinations("childStaticFinal", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("interStaticFinal", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("parentStaticFinal", "Method"))));
	}

	@Test
	public void shouldReturnNormalMethodsFromChildClassFromOtherPackage() throws Exception {
		final List<Method> normalMethods = ReflectionUtils.getNormalMethods(OtherPkgChildObject.class);
		assertThat(normalMethods, notNullValue());
		final Set<String> methodNames = ReflectionUtils.getMemberNames(normalMethods);
		//
		assertThat(methodNames, hasItems(nameCombinations("childNormal", "Method")));
		assertThat(methodNames, hasItems(nameCombinations("interNormal", "Method")));
		assertThat(methodNames, hasItems(nameCombinations("parentNormal", "Method")));
		//
		assertThat(methodNames, hasItems(nameCombinations("childFinal", "Method")));
		assertThat(methodNames, hasItems(nameCombinations("interFinal", "Method")));
		assertThat(methodNames, hasItems(nameCombinations("parentFinal", "Method")));
		//
		assertThat(methodNames, not(hasItems(nameCombinations("childStatic", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("interStatic", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("parentStatic", "Method"))));
		//
		assertThat(methodNames, not(hasItems(nameCombinations("childStaticFinal", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("interStaticFinal", "Method"))));
		assertThat(methodNames, not(hasItems(nameCombinations("parentStaticFinal", "Method"))));
	}

	private String[] nameCombinations(String prefix, String suffix) {
		if (null == prefix) {
			prefix = "";
		}
		final String[] accessModifiers = { "private", "protected", "package", "public" };
		final StringBuilder str = new StringBuilder(prefix);
		final int resetLength = str.length();
		final String[] result = new String[accessModifiers.length];
		for (int i=0; i<accessModifiers.length; ++i) {
			final String access = accessModifiers[i];
			str.setLength(resetLength);
			if (0 != resetLength) {
				str.append(Character.toUpperCase(access.charAt(0)));
				str.append(access.substring(1));
			} else {
				str.append(access);
			}
			str.append(suffix);
			result[i] = str.toString();
		}
		return result;
	}

}