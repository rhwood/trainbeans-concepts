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
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
abstract class AbstractDiscreteStateDelegateTest<E extends DelegatingElement & DiscreteStateElement, D extends Delegate<E>> {

    D delegate;
    E element;
    PropertyChangeEvent lastEvent;

    void setUpEventListeners() {
        delegate.addPropertyChangeListener(evt -> lastEvent = evt);
        lastEvent = null;
    }

    @Test
    public void testSetDelagator() {
        assertThat(delegate.getDelagator()).isNull();
        delegate.setDelagator(element);
        assertThat(delegate.getDelagator()).isEqualTo(element);
        delegate.setDelagator(null);
        assertThat(delegate.getDelagator()).isNull();
    }

    @Test
    public void testGetDelagator() {
        assertThat(delegate.getDelagator()).isNull();
        delegate.setDelagator(element);
        assertThat(delegate.getDelagator()).isEqualTo(element);
        delegate.setDelagator(null);
        assertThat(delegate.getDelagator()).isNull();
    }

    abstract void testGetName();

    /**
     * Needs to be tested with known valid and invalid names for the delegate.
     */
    abstract void testSetName();

    abstract void testGetState();

    abstract void testSetState();

    abstract void testGetRequestedState();

}
