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
import org.trainbeans.beans.VetoableBean;

/**
 *
 * @author rhwood
 * @param <E> type of element
 * @param <D> type of delegate
 */
@SuppressWarnings("linelength") // generic definitions on single line
public class AbstractDelegatingDiscreteStateElement<E extends DelegatingElement & DiscreteStateElement, D extends DiscreteStateDelegate<E>>
        extends VetoableBean
        implements DelegatingElement<E, D>, DiscreteStateElement {

    /**
     * The state if not handled by a delegate.
     */
    private DiscreteState state;
    /**
     * The delegate; if not null, most property access defers to the delegate.
     */
    private D delegate = null;
    /**
     * The name of the element; ignored if null or empty and the delegate is not
     * null.
     */
    private String name;

    /**
     * {@inheritDoc}
     */
    @Override
    public D getDelegate() {
        return delegate;
    }

    @Override
    public final AbstractDelegatingDiscreteStateElement<E, D>
            setDelegate(final D newDelegate) {
        D oldDelegate = delegate;
        if (oldDelegate != null) {
            oldDelegate.removePropertyChangeListener(this);
        }
        delegate = newDelegate;
        if (delegate != null) {
            delegate.addPropertyChangeListener(this);
        }
        firePropertyChange("delegate", oldDelegate, newDelegate);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        if (name == null) {
            return delegate.getName();
        }
        return name;
    }

    @Override
    public final AbstractDelegatingDiscreteStateElement<E, D>
            setName(final String newName) {
        if ((newName == null && delegate == null)
                || (newName != null && newName.trim().isEmpty())) {
            throw new IllegalArgumentException();
        }
        String oldName = this.name;
        try {
            fireVetoableChange("name", oldName, newName);
        } catch (PropertyVetoException ex) {
            throw new IllegalStateException();
        }
        this.name = newName;
        firePropertyChange("name", oldName, newName);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiscreteState getState() {
        return delegate != null ? delegate.getState() : state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiscreteState getRequestedState() {
        return delegate != null ? delegate.getRequestedState() : state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractDelegatingDiscreteStateElement<E, D>
            setState(final DiscreteState newState) {
        DiscreteState oldState = state;
        state = newState;
        if (delegate != null) {
            delegate.setState(newState);
        } else {
            firePropertyChange("state", oldState, newState);
        }
        return this;
    }

    /**
     * Propagate PropertyChangeEvents from the delegate as if they are changes
     * to this object.
     *
     * @param evt {@inheritDoc}
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (delegate == evt.getSource()) {
            PropertyChangeEvent propagation = new PropertyChangeEvent(this,
                    evt.getPropertyName(),
                    evt.getOldValue(),
                    evt.getNewValue());
            propagation.setPropagationId(evt.getPropagationId());
            firePropertyChange(propagation);
        }
    }

    /**
     * Get the state, bypassing any assigned delegate.
     *
     * @return the state
     */
    // package protected for unit testing
    DiscreteState getNonDelegatedState() {
        return state;
    }

    /**
     * Set the state, bypassing any assigned delegate and without notifying
     * listeners of the change in the state.
     *
     * @param newState the new state
     */
    // package protected for unit testing
    void setNonDelegatedState(final DiscreteState newState) {
        this.state = newState;
    }
}
