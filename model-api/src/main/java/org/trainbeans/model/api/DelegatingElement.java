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

import java.beans.PropertyChangeListener;

/**
 * An {@link Element} that can have a {@link Delegate} assigned to it.Some
 * properties that must not be null in Elements can be null in a
 * DelegatingElement since the Delegate may provide the values for those
 * properties.
 *
 * Implementations of this interface must listen to changes in their delegate,
 * and propagate those changes as if they were their own.
 *
 * @author rhwood
 * @param <E> the type of element supported by the delegate
 * @param <D> the type of delegate
 */
public interface DelegatingElement<E extends DelegatingElement,
        D extends Delegate<E>> extends Element, PropertyChangeListener {

    /**
     * Get the delegate for this element.
     *
     * @return the delegate or null if there is no delegate
     */
    D getDelegate();

    /**
     * Set the delegate for this element.
     *
     * @param delegate the delegate or null if removing a delegate
     * @return this object
     */
    DelegatingElement<E, D> setDelegate(D delegate);
}
