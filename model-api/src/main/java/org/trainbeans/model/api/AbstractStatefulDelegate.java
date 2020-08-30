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
 *
 * @author rhwood
 * @param <E> the type of supported element
 */
public abstract class AbstractStatefulDelegate<E extends DelegatingElement & StatefulElement> extends VetoableBean implements StatefulDelegate<E> {

    private E delagator;
    private String name;
    private int state;

    /**
     * Test if the provided name is valid in this context.
     * 
     * @param name the name to test
     * @return true if name is valid; false otherwise
     */
    protected abstract boolean isValidName(String name);

    @Override
    public void setDelagator(DelegatingElement delagator) {
        this.delagator = (E) delagator;
    }

    @Override
    public E getDelagator() {
        return delagator;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String newName) {
        String oldName = name;
        if (isValidName(newName)) {
            name = newName;
        } else {
            throw new IllegalArgumentException();
        }
        firePropertyChange("name", oldName, newName);
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void setState(int newState) {
        int oldState = state;
        state = newState;
        firePropertyChange("state", oldState, newState);
    }
    
    @Override
    public int getRequestedState() {
        return getState();
    }

}
