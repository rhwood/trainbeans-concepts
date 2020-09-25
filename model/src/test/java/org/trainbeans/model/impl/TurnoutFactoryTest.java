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
package org.trainbeans.model.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.trainbeans.model.api.AbstractDiscreteStateDelegate;
import org.trainbeans.model.api.Element;
import org.trainbeans.model.api.Turnout;
import org.trainbeans.model.api.TurnoutDelegate;

/**
 *
 * @author rhwood
 */
class TurnoutFactoryTest {

    private TurnoutFactory factory;
    
    @BeforeEach
    public void setUp() {
        factory = new TurnoutFactory();
    }
    
    @AfterEach
    public void tearDown() {
        // nothing to do
    }

    @Test
    void testGetElementClass() {
        assertThat(factory.getElementClass()).isEqualTo(Turnout.class);
    }

    @Test
    void testCreate() {
        // empty Lookup
        assertThat(factory.create("test", Lookup.EMPTY).getName()).isEqualTo("test");
        assertThatThrownBy(() -> factory.create(null, Lookup.EMPTY)).isInstanceOf(IllegalArgumentException.class);
        // null Lookup
        assertThat(factory.create("test", null).getName()).isEqualTo("test");
        assertThatThrownBy(() -> factory.create(null, null)).isInstanceOf(IllegalArgumentException.class);
        // populated Lookup
        TurnoutDelegate delegate = new TestTurnoutDelegate();
        delegate.setName("delegate");
        Lookup lookup = Lookups.fixed(delegate);
        assertThat(factory.create(null, lookup).getName()).isEqualTo("delegate");
        assertThat(factory.create("test", lookup).getName()).isEqualTo("test");
    }
    
    private static class TestTurnoutDelegate extends AbstractDiscreteStateDelegate<Turnout> implements TurnoutDelegate {

        @Override
        protected boolean isValidName(String name) {
            return true;
        }

        @Override
        public TestTurnoutDelegate getSelf() {
            return this;
        }

    }
}
