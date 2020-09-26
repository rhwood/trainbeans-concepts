/*
 * Copyright 2020 rhwood.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trainbeans.beans;

/**
 * Helper interface to define methods required to implement a fluent and
 * extensible API.
 *
 * @author rhwood
 */
public interface Fluent {

    /**
     * Get this object. This is an exposed internal implementation detail that
     * enables a fluent interface and method chaining. It should <em>not</em>
     * be implemented by abstract classes.
     *
     * @param <F> the return type
     * @return this object
     */
    <F extends Fluent> F getSelf();
}
