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
package org.trainbeans.model.api;

/**
 * A delegate for connecting an element to a model.
 *
 * @author rhwood
 * @param <T> the type of Element this Delegate supports
 */
public interface Delegate<T extends DelegatingElement> extends Element {

    /**
     * Set the Element this is a delegate for.
     *
     * @param delagator the element
     */
    // TODO is any need for this a sign of failed design?
    void setDelagator(T delagator);

    /**
     * Get the Element this is a delegate for.
     *
     * @return the element or null if this Delegate is not being delegated to
     */
    // TODO is any need for this a sign of failed design?
    T getDelagator();
}
