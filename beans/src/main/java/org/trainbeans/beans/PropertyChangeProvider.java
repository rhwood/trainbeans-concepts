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

import java.beans.PropertyChangeListener;

/**
 * Implementation of the {@link java.beans.PropertyChangeSupport} public API.
 * Implement this API to ensure complete support for registering listeners to
 * property changes.
 *
 * @author rhwood
 */
public interface PropertyChangeProvider {

    /**
     * Add a PropertyChangeListener that listens to all property changes. If the
     * same listener is added multiple times, it receives the change
     * notification multiple times.
     *
     * @param listener the listener to add; if null, no action is taken and no
     * exception is thrown
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Add a PropertyChangeListener that listens to changes in the named
     * property. If the same listener is added multiple times, it receives the
     * change notification multiple times.
     *
     * @param propertyName the name of the property to listen to
     * @param listener the listener to add; if null, no action is taken and no
     * exception is thrown
     */
    void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener);

    /**
     * Remove a PropertyChangeListener that listens to changes to all
     * properties. If the same listener was added multiple times, only removes
     * the first instance of that listener.
     *
     * @param listener the listener to remove; if null or not previously added,
     * no action is taken and no exception is thrown
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove a PropertyChangeListener that listens to changes to all
     * properties. If the same listener was added multiple times, only removes
     * the first instance of that listener.
     *
     * @param propertyName the name of the property to listen to
     * @param listener the listener to remove; if null or not previously added,
     * no action is taken and no exception is thrown
     */
    void removePropertyChangeListener(String propertyName,
            PropertyChangeListener listener);

    /**
     * Get all registered PropertyChangeListeners. See
     * {@link java.beans.PropertyChangeSupport#getPropertyChangeListeners()} for
     * more information.
     *
     * @return all PropertyChangeListeners or an empty array
     */
    PropertyChangeListener[] getPropertyChangeListeners();

    /**
     * Get PropertyChangeListeners registered to listen to the named property.
     * See {@link java.beans.PropertyChangeSupport#getPropertyChangeListeners()}
     * for more information.
     *
     * @param propertyName the named property
     * @return registered PropertyChangeListeners or an empty array; if
     * propertyName is null returns an empty array
     */
    PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

    /**
     * Check if there are any listeners registered against the named property.
     * If {@code propertyName} is null, checks if there are any listeners
     * registered to receive all property change events.
     *
     * @param propertyName the named property
     * @return true if there are registered listeners; false otherwise
     */
    boolean hasListeners(String propertyName);
}
