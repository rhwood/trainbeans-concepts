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

import org.trainbeans.beans.PropertyChangeProvider;
import org.trainbeans.beans.VetoableChangeProvider;

/**
 * An element in the internal model of a model railroad.
 *
 * @author rhwood
 */
public interface Element extends PropertyChangeProvider,
        VetoableChangeProvider {

    /**
     * Get the name of the element.
     *
     * @return the name
     */
    String getName();

    /**
     * Set the name of the element. Implementations of this must notify
     * {@link java.beans.VetoableChangeListener}s and
     * {@link java.beans.PropertyChangeListener}s.
     *
     * @param name the new name
     * @throws IllegalArgumentException if another element in the same model has
     * the same name, the name is blank, or the name is null and the Element
     * does not have a {@link Delegate}
     */
    // TODO: should this throw an IllegalStateException if vetoed instead of an
    // IllegalArgumentException?
    void setName(String name);
}
