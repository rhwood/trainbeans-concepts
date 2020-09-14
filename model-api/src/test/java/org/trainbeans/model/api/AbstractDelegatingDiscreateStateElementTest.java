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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 * @param <E> the type of element in the test
 * @param <D> the type of delegate in the test
 */
abstract class AbstractDelegatingDiscreateStateElementTest<E extends AbstractDelegatingDiscreteStateElement, D extends DiscreteStateDelegate> {

    E element;
    D delegate;
    PropertyChangeEvent lastEvent;

    void setUpEventListeners() {
        element.addPropertyChangeListener(evt -> lastEvent = evt);
        lastEvent = null;
    }

    @Test
    void testGetDelegate() {
        assertThat(element.getDelegate()).isNull();
        element.setDelegate(delegate);
        assertThat(element.getDelegate()).isEqualTo(delegate);
    }

    @Test
    void testSetDelegate() {
        element.setDelegate(delegate);
        assertThat(element.getDelegate()).isEqualTo(delegate);
        element.setDelegate(null);
        assertThat(element.getDelegate()).isNull();
    }

    @Test
    void testGetName() {
        element.setDelegate(delegate);
        element.setName(null);
        assertThat(element.getName()).isEqualTo(delegate.getName());
        element.setName("test");
        assertThat(element.getName()).isEqualTo("test");
    }

    @Test
    void testSetName() {
        element.setDelegate(null);
        assertThatThrownBy(() -> element.setName(null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> element.setName("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> element.setName(" ")).isInstanceOf(IllegalArgumentException.class);
        element.setName("test");
        assertThat(element.getName()).isEqualTo("test");
        element.setDelegate(delegate);
        assertThatCode(() -> element.setName(null)).doesNotThrowAnyException();
        assertThatThrownBy(() -> element.setName("")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> element.setName(" ")).isInstanceOf(IllegalArgumentException.class);
    }

    abstract void testGetRequestedState();

    abstract void testGetState();

    abstract void testSetState();
    
}
