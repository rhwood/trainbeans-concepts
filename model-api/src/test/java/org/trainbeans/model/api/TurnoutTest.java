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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
class TurnoutTest extends AbstractDelegatingDiscreteStateElementTest<Turnout, TurnoutDelegate> {

    @BeforeEach
    void setUp() {
        element = new Turnout();
        delegate = new TestTurnoutDelegate();
        setUpEventListeners();
    }

    @AfterEach
    void tearDown() {
        // nothing to do
    }

    @Test
    @Override
    void testGetState() {
        // default state is UNKNOWN without delegate
        assertThat(element.getDelegate()).isNull();
        assertThat(element.getState()).isEqualTo(Turnout.State.UNKNOWN);
        element.state = Turnout.State.CONFLICTED;
        assertThat(element.getState()).isEqualTo(Turnout.State.CONFLICTED);
        element.setDelegate(delegate);
        assertThat(element.getDelegate()).isEqualTo(delegate);
        assertThat(delegate.getState()).isEqualTo(Turnout.State.UNKNOWN);
        assertThat(delegate.getState()).isNotEqualTo(element.state);
        assertThat(element.getState()).isEqualTo(Turnout.State.UNKNOWN);
        assertThat(element.getState()).isNotEqualTo(element.state);
    }

    @Test
    @Override
    void testGetRequestedState() {
        // default state is UNKNOWN without delegate
        assertThat(element.getDelegate()).isNull();
        assertThat(element.getRequestedState()).isEqualTo(element.getState());
        element.state = Turnout.State.CONFLICTED;
        assertThat(element.getRequestedState()).isEqualTo(element.getState());
        element.setDelegate(delegate);
        assertThat(element.getDelegate()).isEqualTo(delegate);
        assertThat(delegate.getRequestedState()).isEqualTo(Turnout.State.UNKNOWN);
        assertThat(delegate.getRequestedState()).isNotEqualTo(element.state);
        assertThat(element.getRequestedState()).isEqualTo(Turnout.State.UNKNOWN);
        assertThat(element.getRequestedState()).isNotEqualTo(element.state);
    }

    @Test
    @Override
    void testSetState() {
        assertThat(element.getDelegate()).isNull();
        assertThat(lastEvent).isNull();
        element.setState(Turnout.State.CLOSED);
        assertThat(lastEvent).isNotNull();
        assertThat(lastEvent.getPropertyName()).isEqualTo("state");
        assertThat(lastEvent.getOldValue()).isEqualTo(Turnout.State.UNKNOWN);
        assertThat(lastEvent.getNewValue()).isEqualTo(Turnout.State.CLOSED);
        element.setDelegate(delegate);
        assertThat(element.getDelegate()).isEqualTo(delegate);
        assertThat(delegate.getState()).isEqualTo(Turnout.State.UNKNOWN);
        lastEvent = null;
        element.setState(Turnout.State.THROWN);
        assertThat(lastEvent).isNotNull();
        assertThat(lastEvent.getPropertyName()).isEqualTo("state");
        assertThat(lastEvent.getOldValue()).isEqualTo(Turnout.State.UNKNOWN);
        assertThat(lastEvent.getNewValue()).isEqualTo(Turnout.State.THROWN);
    }

    private static class TestTurnoutDelegate extends AbstractDiscreteStateDelegate<Turnout> implements TurnoutDelegate {

        TestTurnoutDelegate() {
            setState(Turnout.State.UNKNOWN);
        }

        @Override
        protected boolean isValidName(String name) {
            return true;
        }
    }
}
