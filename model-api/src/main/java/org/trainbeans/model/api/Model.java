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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Set;
import org.openide.util.Lookup;
import org.trainbeans.beans.PropertyChangeProvider;

/**
 * A Model is the internal model of an external Model Railroad.
 *
 * A Model has one constraint against the Elements it contains: no two Elements
 * may have the same name.
 *
 * @author Randall Wood
 */
public interface Model extends PropertyChangeProvider, VetoableChangeListener {

    /**
     * Create an element in the model.
     *
     * @param <T> the type of element
     * @param type the type of element
     * @param name the name of the element
     * @return the newly created element
     * @throws IllegalArgumentException if an element of type with name already
     * exists
     * @throws IllegalArgumentException if no
     * {@link org.trainbeans.model.spi.ElementFactory} for type exists in model
     */
    public default <T extends Element> T create(Class<T> type, String name) {
        return create(type, name, null);
    }

    /**
     * Create an element in the model. If the type is a
     * {@link DelegatingElement} and the lookup contains a {@link Delegate}, the
     * element will use that Delegate.
     *
     * @param <T> the type of element
     * @param type the type of element
     * @param name the name of the element
     * @param lookup a container of additional services for the factory
     * @return the newly created element
     * @throws IllegalArgumentException if an element of type with name already
     * exists; if name is blank and lookup does not contain a Delegate; if no
     * {@link org.trainbeans.model.spi.ElementFactory} for type exists in model
     */
    public <T extends Element> T create(Class<T> type, String name, Lookup lookup);

    /**
     * Get all elements of a specific type from the model.
     *
     * @param <T> the type of element
     * @param type the type of element
     * @return a set of elements; this set is empty if there are no matching
     * elements
     */
    public <T extends Element> Set<T> getAll(Class<T> type);

    /**
     * Get an element of a specific type from the model.
     *
     * @param <T> the type of element
     * @param type the type of element
     * @param name the name of the element
     * @return the matching element or null if there is no such element
     */
    // TODO should the type be removed from this API?
    public <T extends Element> T get(Class<T> type, String name);

    /**
     * Get an element of a specific type from the model, creating it if needed.
     *
     * @param <T> the type of element
     * @param type the type of element
     * @param name the name of the element
     * @return the matching element, possibly newly created
     * @throws IllegalArgumentException if an element of type with name already
     * exists
     */
    public <T extends Element> T getOrCreate(Class<T> type, String name);

    /**
     * Put an existing element into the model.
     *
     * @param <T> the type of element
     * @param element the element
     * @throws IllegalArgumentException if an element of the same type with the
     * same name already exists
     */
    public <T extends Element> void put(T element);

    /**
     * Remove an element from the model. It is not an error if the element does
     * not exist in the model, nor is it an error to attempt to remove a null
     * element.
     *
     * @param <T> the type of element
     * @param element the element to remove
     */
    public <T extends Element> void remove(T element);

    @Override
    public default void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        if (evt.getPropertyName().equals("name") && get(Element.class, evt.getNewValue().toString()) != null) {
            // TODO: support I18N
            throw new PropertyVetoException("Element with name \"" + evt.getNewValue() + "\" already exists.", evt);
        }
    }
}
