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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

/**
 *
 * @author rhwood
 */
public class VetoableBean extends Bean implements VetoableChangeProvider {

    private final VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

    @Override
    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    @Override
    public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(propertyName, listener);
    }

    @Override
    public VetoableChangeListener[] getVetoableChangeListeners() {
        return vetoableChangeSupport.getVetoableChangeListeners();
    }

    @Override
    public VetoableChangeListener[] getVetoableChangeListeners(String propertyName) {
        return vetoableChangeSupport.getVetoableChangeListeners(propertyName);
    }

    @Override
    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }

    @Override
    public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(propertyName, listener);
    }

    /**
     * {@inheritDoc}
     *
     * @param propertyName the property
     * @return true if the property has {@link PropertyChangeListener}s or
     * {@link VetoableChangeListener}s
     */
    @Override
    public boolean hasListeners(String propertyName) {
        return vetoableChangeSupport.hasListeners(propertyName) || super.hasListeners(propertyName);
    }

    /**
     * Notify listeners of a change that can be vetoed. Calls
     * {@link #firePropertyChange(PropertyChangeEvent)} to notify any interested
     * {@link java.beans.PropertyChangeListener}s if the event is not vetoed.
     * Does not notify if {@link PropertyChangeEvent#getOldValue()} is not null
     * and equals {@link PropertyChangeEvent#getNewValue()}.
     *
     * @param event the event that can be vetoed
     * @throws PropertyVetoException if the event is vetoed
     */
    protected void fireVetoableChange(PropertyChangeEvent event) throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(event);
        firePropertyChange(event);
    }

    /**
     * Notify listeners of a change that can be vetoed. Calls
     * {@link #fireVetoableChange(String, Object, Object)} to notify any
     * interested {@link java.beans.PropertyChangeListener}s if the event is not
     * vetoed. Does not notify if oldValue is not null and equals newValue.
     *
     * @param propertyName the name of the property
     * @param oldValue the old value
     * @param newValue the new value
     * @throws PropertyVetoException if the event is vetoed
     */
    protected void fireVetoableChange(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(propertyName, oldValue, newValue);
        firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Notify listeners of a change that can be vetoed. Calls
     * {@link #fireVetoableChange(String, Object, Object)} to notify any
     * interested {@link java.beans.PropertyChangeListener}s if the event is not
     * vetoed. Does not notify if oldValue equals newValue.
     *
     * @param propertyName the name of the property
     * @param oldValue the old value
     * @param newValue the new value
     * @throws PropertyVetoException if the event is vetoed
     */
    protected void fireVetoableChange(String propertyName, int oldValue, int newValue) throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(propertyName, oldValue, newValue);
        firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Notify listeners of a change that can be vetoed. Calls
     * {@link #fireVetoableChange(String, Object, Object)} to notify any
     * interested {@link java.beans.PropertyChangeListener}s if the event is not
     * vetoed. Does not notify if oldValue equals newValue.
     *
     * @param propertyName the name of the property
     * @param oldValue the old value
     * @param newValue the new value
     * @throws PropertyVetoException if the event is vetoed
     */
    protected void fireVetoableChange(String propertyName, boolean oldValue, boolean newValue) throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(propertyName, oldValue, newValue);
        firePropertyChange(propertyName, oldValue, newValue);
    }
}
