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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
class TurnoutDelegateTest extends AbstractDiscreteStateDelegateTest<Turnout, TurnoutDelegate> {

    boolean valid;

    @BeforeEach
    void setUp() {
        element = new Turnout();
        delegate = new TurnoutDelegateImpl();
        valid = true;
        setUpEventListeners();
    }

    @Test
    @Override
    void testSetName() {
        assertThat(delegate.getName()).isNull();
        delegate.setName("foo");
        assertThat(delegate.getName()).isEqualTo("foo");
        assertThat(lastEvent).isNotNull();
        assertThat(lastEvent.getPropertyName()).isEqualTo("name");
        assertThat(lastEvent.getNewValue()).isEqualTo("foo");
        lastEvent = null;
        valid = false;
        assertThatCode(() -> delegate.setName("bar")).isInstanceOf(IllegalArgumentException.class);
        assertThat(lastEvent).isNull();
        assertThat(delegate.getName()).isEqualTo("foo");
    }

    @Override
    void testGetName() {
        // no need to test, tested in testSetName()
    }

    @Override
    void testGetState() {
        // no need to test, tested in TurnoutTest#testGetState()
    }

    @Override
    void testSetState() {
        // no need to test, tested in TurnoutTest#testSetState()
    }

    @Override
    void testGetRequestedState() {
        // no need to test, tested in TurnoutTest#testGetRequestedState()
    }
    
    private class TurnoutDelegateImpl extends AbstractDiscreteStateDelegate<Turnout.State, Turnout> implements TurnoutDelegate {

        @Override
        protected boolean isValidName(String name) {
            return valid;
        }

        @Override
        public TurnoutDelegateImpl getSelf() {
            return this;
        }

    }

}
