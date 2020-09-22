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
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

/**
 *
 * @author rhwood
 */
public class VetoableBean extends Bean implements VetoableChangeProvider {

    /**
     * Supporting class that manages {@link VetoableChangeListeners} and
     * {@link PropertyChangeEvent} propagation.
     */
    private final VetoableChangeSupport vetoableChangeSupport
            = new VetoableChangeSupport(this);

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVetoableChangeListener(
            final VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addVetoableChangeListener(final String propertyName,
            final VetoableChangeListener listener) {
        vetoableChangeSupport.addVetoableChangeListener(propertyName, listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VetoableChangeListener[] getVetoableChangeListeners() {
        return vetoableChangeSupport.getVetoableChangeListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VetoableChangeListener[] getVetoableChangeListeners(
            final String propertyName) {
        return vetoableChangeSupport.getVetoableChangeListeners(propertyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeVetoableChangeListener(
            final VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeVetoableChangeListener(final String propertyName,
            final VetoableChangeListener listener) {
        vetoableChangeSupport.removeVetoableChangeListener(propertyName,
                listener);
    }

    /**
     * {@inheritDoc}
     *
     * @param propertyName the property
     * @return true if the property has
     * {@link java.beans.PropertyChangeListener}s or
     * {@link VetoableChangeListener}s
     */
    @Override
    public boolean hasListeners(final String propertyName) {
        return vetoableChangeSupport.hasListeners(propertyName)
                || super.hasListeners(propertyName);
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
    protected void fireVetoableChange(final PropertyChangeEvent event)
            throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(event);
        firePropertyChange(event);
    }

    /**
     * Notify listeners of a change that can be vetoed. Calls
     * {@link #firePropertyChange(String, Object, Object)} to notify any
     * interested {@link java.beans.PropertyChangeListener}s if the event is not
     * vetoed. Does not notify if oldValue is not null and equals newValue.
     *
     * @param propertyName the name of the property
     * @param oldValue the old value
     * @param newValue the new value
     * @throws PropertyVetoException if the event is vetoed
     */
    protected void fireVetoableChange(final String propertyName,
            final Object oldValue, final Object newValue)
            throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(propertyName,
                oldValue, newValue);
        firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Notify listeners of a change that can be vetoed. Calls
     * {@link #firePropertyChange(String, int, int)} to notify any interested
     * {@link java.beans.PropertyChangeListener}s if the event is not vetoed.
     * Does not notify if oldValue equals newValue.
     *
     * @param propertyName the name of the property
     * @param oldValue the old value
     * @param newValue the new value
     * @throws PropertyVetoException if the event is vetoed
     */
    protected void fireVetoableChange(final String propertyName,
            final int oldValue, final int newValue)
            throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(propertyName,
                oldValue, newValue);
        firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
     * Notify listeners of a change that can be vetoed. Calls
     * {@link #firePropertyChange(String, boolean, boolean)} to notify any
     * interested {@link java.beans.PropertyChangeListener}s if the event is not
     * vetoed. Does not notify if oldValue equals newValue.
     *
     * @param propertyName the name of the property
     * @param oldValue the old value
     * @param newValue the new value
     * @throws PropertyVetoException if the event is vetoed
     */
    protected void fireVetoableChange(final String propertyName,
            final boolean oldValue, final boolean newValue)
            throws PropertyVetoException {
        vetoableChangeSupport.fireVetoableChange(propertyName,
                oldValue, newValue);
        firePropertyChange(propertyName, oldValue, newValue);
    }
}
