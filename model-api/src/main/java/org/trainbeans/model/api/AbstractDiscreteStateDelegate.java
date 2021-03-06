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

import org.trainbeans.beans.VetoableBean;

/**
 * This is a simplistic abstract delegate for {@link DiscreteStateElement}s that
 * is primarily useful for simulation and unit testing.Other implementations of
 {@link DiscreteStateDelegate} can use this as a template.
 *
 * @author rhwood
 * @param <S> the type of supported state
 * @param <E> the type of supported element
 */
@SuppressWarnings("checkstyle:linelength") // generic definition on one line
public abstract class AbstractDiscreteStateDelegate<S extends DiscreteState, E extends DelegatingElement & DiscreteStateElement<S>>
        extends VetoableBean implements DiscreteStateDelegate<S, E> {

    /**
     * The delegating element.
     */
    private E delegator;
    /**
     * The name of the delegate. Will be used by the delegator if the delegator
     * is not overriding it.
     */
    private String name;
    /**
     * The current state.
     */
    private S state;

    /**
     * Test if the provided name is valid in this context.
     *
     * @param aName the name to test
     * @return true if name is valid; false otherwise
     */
    protected abstract boolean isValidName(String aName);

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDelagator(final E delagator) {
        this.delegator = delagator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getDelagator() {
        return delegator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <D extends Element> D setName(final String newName) {
        String oldName = name;
        if (isValidName(newName)) {
            name = newName;
        } else {
            throw new IllegalArgumentException();
        }
        firePropertyChange("name", oldName, newName);
        return getSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S getState() {
        return state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <D extends DiscreteStateElement> D
            setState(final S newState) {
        DiscreteState oldState = state;
        state = newState;
        firePropertyChange("state", oldState, newState);
        return getSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S getRequestedState() {
        return getState();
    }

}
