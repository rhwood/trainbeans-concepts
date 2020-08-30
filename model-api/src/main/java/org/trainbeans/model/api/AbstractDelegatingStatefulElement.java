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
public class AbstractDelegatingStatefulElement<E extends DelegatingElement & StatefulElement, D extends StatefulDelegate<E>> extends VetoableBean implements DelegatingElement, StatefulElement {

    int state = State.UNKNOWN;
    D delegate = null;
    private String name;

    @Override
    public D getDelegate() {
        return delegate;
    }

    @Override
    public final void setDelegate(Delegate newDelegate) {
        D oldDelegate = delegate;
        if (oldDelegate != null) {
            oldDelegate.removePropertyChangeListener(this);
        }
        delegate = (D) newDelegate;
        if (delegate != null) {
            delegate.removePropertyChangeListener(this);
        }
        firePropertyChange("delegate", oldDelegate, newDelegate);
    }

    @Override
    public String getName() {
        if (name == null) {
            return delegate.getName();
        }
        return name;
    }

    @Override
    public final void setName(String newName) {
        if ((newName == null && delegate == null) || (newName != null && newName.trim().isEmpty())) {
            throw new IllegalArgumentException();
        }
        String oldName = this.name;
        try {
            fireVetoableChange("name", oldName, newName);
        } catch (PropertyVetoException ex) {
            throw new IllegalArgumentException();
        }
        this.name = newName;
        firePropertyChange("name", oldName, newName);
    }

    @Override
    public int getState() {
        return delegate != null ? delegate.getState() : state;
    }

    @Override
    public int getRequestedState() {
        return delegate != null ? delegate.getRequestedState() : state;
    }

    @Override
    public void setState(int newState) {
        int oldState = state;
        state = newState;
        if (delegate != null) {
            delegate.setState(newState);
        } else {
            firePropertyChange("state", oldState, newState);
        }
    }

    /**
     * Propagate PropertyChangeEvents from the delegate as if they are changes
     * to this object.
     *
     * @param evt {@inheritDoc}
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (delegate == evt.getSource()) {
            PropertyChangeEvent propagation = new PropertyChangeEvent(this, evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            propagation.setPropagationId(evt.getPropagationId());
            firePropertyChange(propagation);
        }
    }

}
