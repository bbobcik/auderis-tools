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

/**
 * Facilitates detection of property changes.
 * <p>
 * There may be two modes of operation, explicit and implicit
 * change recording.
 * <p>
 * <b>Explicit change recording</b> depends on the user of change tracker to
 * invoke appropriate methods, such as {@link cz.auderis.tools.change.ChangeTracker#markChange()}.
 * The programming idiom would be:
 * <pre>
 *     ChangeTracker tracker = ...
 *     ...
 *     if (propertyNameWasChanged) {
 *         tracker.markChange("name");
 *     }
 * </pre>
 * <p>
 * <b>Implicit change recording</b> allows to insert change tracking operation as a part
 * of property setting pipeline. The usual idiom is:
 * <pre>
 *     BasicChangeTracker tracker = ...
 *     ...
 *     object.setName(tracker.updateString( object.getName(), newName, "name" ));
 *     object.setAge(tracker.updateInt( object.getAge(), newAge, "age" ));
 * </pre>
 *
 * @author Boleslav Bobcik &lt;bbobcik@gmail.com&gt;
 * @version 1.0
 */
package cz.auderis.tools.change;