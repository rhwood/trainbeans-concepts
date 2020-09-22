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

import java.beans.VetoableChangeListener;

/**
 *
 * @author rhwood
 */
public interface VetoableChangeProvider {

    /**
     * Add a VetoableChangeListener to this object. If a listener is added
     * multiple times, it's {@link
     * VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)}
     * method will be called as many times as the listener was added. If the
     * listener is null, no exception is thrown and no action is taken.
     *
     * @param listener the listener to add
     */
    void addVetoableChangeListener(VetoableChangeListener listener);

    /**
     * Add a VetoableChangeListener to a specific property of this object. If a
     * listener is added multiple times, it's {@link
     * VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)}
     * method will be called as many times as the listener was added. If the
     * listener is null, no exception is thrown and no action is taken.
     *
     * @param propertyName the property to add the listener to
     * @param listener the listener to add to the property
     */
    void addVetoableChangeListener(String propertyName,
            VetoableChangeListener listener);

    /**
     * Get the VetoableChangeListeners to this object. If a listener was added
     * to a named property, that listener is returned as a
     * {@link java.beans.VetoableChangeListenerProxy}.
     *
     * @return the listeners or an empty array if none
     * @see java.beans.VetoableChangeSupport#getVetoableChangeListeners()
     */
    VetoableChangeListener[] getVetoableChangeListeners();

    /**
     * Get the VetoableChangeListeners to a named property of this object.
     *
     * @param propertyName the named property
     * @return the listeners or an empty array if none or propertyName is null
     */
    VetoableChangeListener[] getVetoableChangeListeners(String propertyName);

    /**
     * Remove the VetoableChangeListener from the list of listeners. Only
     * removes the first instance of the listener that is not listening to a
     * named property. If listener is null, no exception is thrown and no action
     * is taken.
     *
     * @param listener the listener to remove
     */
    void removeVetoableChangeListener(VetoableChangeListener listener);

    /**
     * Remove the VetoableChangeListener from this object for the named
     * property. Only removes the first instance of the listener is listening to
     * the named property. If listener is null, no exception is thrown and no
     * action is taken.
     *
     * @param propertyName the property name
     * @param listener the listener to remove
     */
    void removeVetoableChangeListener(String propertyName,
            VetoableChangeListener listener);

    /**
     * Are any listeners for a named property present?
     *
     * @param propertyName the property to check
     * @return true if listeners are present; false otherwise
     */
    boolean hasListeners(String propertyName);
}
