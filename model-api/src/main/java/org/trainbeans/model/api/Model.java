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

import java.util.Set;
import org.netbeans.api.annotations.common.CheckForNull;
import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.annotations.common.NullAllowed;
import org.openide.util.Lookup;

/**
 * A Model is the internal model of an external Model Railroad.
 *
 * @author Randall Wood
 */
public interface Model {

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
    @NonNull
    public default <T extends Element> T create(@NonNull Class<T> type, @NonNull String name) {
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
    @NonNull
    public <T extends Element> T create(@NonNull Class<T> type, @NonNull String name, @NullAllowed Lookup lookup);

    /**
     * Get all elements of a specific type from the model.
     *
     * @param <T> the type of element
     * @param type the type of element
     * @return a set of elements; this set is empty if there are no matching
     * elements
     */
    @NonNull
    public <T extends Element> Set<T> getAll(@NonNull Class<T> type);

    /**
     * Get an element of a specific type from the model.
     *
     * @param <T> the type of element
     * @param type the type of element
     * @param name the name of the element
     * @return the matching element or null if there is no such element
     */
    @CheckForNull
    public <T extends Element> T get(@NonNull Class<T> type, @NonNull String name);

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
    @NonNull
    public <T extends Element> T getOrCreate(@NonNull Class<T> type, @NonNull String name);

    /**
     * Put an existing element into the model.
     *
     * @param <T> the type of element
     * @param element the element
     * @throws IllegalArgumentException if an element of the same type with the
     * same name already exists
     */
    public <T extends Element> void put(@NonNull T element);

    /**
     * Remove an element from the model. It is not an error if the element does
     * not exist in the model, nor is it an error to attempt to remove a null
     * element.
     *
     * @param <T> the type of element
     * @param element the element to remove
     */
    public <T extends Element> void remove(@NullAllowed T element);
}
