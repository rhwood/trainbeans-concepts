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
import java.beans.PropertyChangeSupport;

/**
 *
 * @author rhwood
 */
public abstract class Bean implements PropertyChangeProvider {

    /**
     * Supporting class that manages {@link PropertyChangeListeners} and
     * {@link PropertyChangeEvent} propagation.
     */
    private final PropertyChangeSupport propertyChangeSupport
            = new PropertyChangeSupport(this);

    /**
     * {@inheritDoc}
     */
    @Override
    public <P extends PropertyChangeProvider> P addPropertyChangeListener(
            final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
        return getSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P extends PropertyChangeProvider> P addPropertyChangeListener(
            final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
        return getSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P extends PropertyChangeProvider> P removePropertyChangeListener(
            final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
        return getSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P extends PropertyChangeProvider> P removePropertyChangeListener(
            final String propertyName, final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName,
                listener);
        return getSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return propertyChangeSupport.getPropertyChangeListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyChangeListener[] getPropertyChangeListeners(
            final String propertyName) {
        return propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasListeners(final String propertyName) {
        return propertyChangeSupport.hasListeners(propertyName);
    }

    /**
     * Notify all listeners of a property change event.
     *
     * @param event the property change event to pass in the notification
     */
    protected void firePropertyChange(final PropertyChangeEvent event) {
        propertyChangeSupport.firePropertyChange(event);
    }

    /**
     * Notify all listeners of a property change event.
     *
     * @param propertyName the name of the changed property
     * @param oldValue the old value
     * @param newValue the new value
     */
    protected void firePropertyChange(final String propertyName,
            final Object oldValue, final Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName,
                oldValue, newValue);
    }

    /**
     * Notify all listeners of a property change event.
     *
     * @param propertyName the name of the changed property
     * @param oldValue the old value
     * @param newValue the new value
     */
    protected void firePropertyChange(final String propertyName,
            final int oldValue, final int newValue) {
        propertyChangeSupport.firePropertyChange(propertyName,
                oldValue, newValue);
    }

    /**
     * Notify all listeners of a property change event.
     *
     * @param propertyName the name of the changed property
     * @param oldValue the old value
     * @param newValue the new value
     */
    protected void firePropertyChange(final String propertyName,
            final boolean oldValue, final boolean newValue) {
        propertyChangeSupport.firePropertyChange(propertyName,
                oldValue, newValue);
    }

    /**
     * Notify all listeners of an indexed property change.
     *
     * @param propertyName the name of the property
     * @param index the index in the property that changed
     * @param oldValue the old value
     * @param newValue the new value
     */
    protected void fireIndexedPropertyChange(final String propertyName,
            final int index, final Object oldValue, final Object newValue) {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName,
                index, oldValue, newValue);
    }

    /**
     * Notify all listeners of an indexed property change.
     *
     * @param propertyName the name of the property
     * @param index the index in the property that changed
     * @param oldValue the old value
     * @param newValue the new value
     */
    protected void fireIndexedPropertyChange(final String propertyName,
            final int index, final int oldValue, final int newValue) {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName,
                index, oldValue, newValue);
    }

    /**
     * Notify all listeners of an indexed property change.
     *
     * @param propertyName the name of the property
     * @param index the index in the property that changed
     * @param oldValue the old value
     * @param newValue the new value
     */
    protected void fireIndexedPropertyChange(final String propertyName,
            final int index, final boolean oldValue, final boolean newValue) {
        propertyChangeSupport.fireIndexedPropertyChange(propertyName,
                index, oldValue, newValue);
    }
}
