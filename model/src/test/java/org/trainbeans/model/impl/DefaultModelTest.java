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

import java.beans.PropertyChangeEvent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.trainbeans.model.api.AbstractDelegatingDiscreteStateElement;
import org.trainbeans.model.api.Element;
import org.trainbeans.model.api.Turnout;

/**
 *
 * @author rhwood
 */
class DefaultModelTest {

    DefaultModel model;

    @BeforeEach
    void setUp() {
        model = new DefaultModel(Lookups.fixed(new TurnoutFactory()));
    }

    @AfterEach
    void tearDown() {
        // nothing to do
    }

    @Test
    void testCreate() {
        assertThat(model.get(Turnout.class, "foo")).isNull();
        Turnout turnout1 = model.create(Turnout.class, "foo", Lookup.EMPTY);
        assertThat(model.get(Turnout.class, "foo")).isEqualTo(turnout1);
        assertThatCode(() -> model.create(Element.class, "foo", Lookup.EMPTY)).isInstanceOf(IllegalArgumentException.class);
        assertThatCode(() -> model.create(Element.class, "bar", Lookup.EMPTY)).isInstanceOf(IllegalArgumentException.class);
        Turnout turnout2 = model.getOrCreate(Turnout.class, "bar", null);
        assertThat(turnout2).isNotNull();
        assertThat(model.getAll(Turnout.class)).containsExactlyInAnyOrder(turnout1, turnout2);
    }

    @Test
    void testGetAll() {
        assertThat(model.getCache(Turnout.class)).isNull();
        assertThat(model.getAll(Turnout.class)).isEmpty();
        assertThat(model.getCache(Turnout.class)).isEmpty();
        Turnout turnout = model.create(Turnout.class, "foo");
        // assert that set is gotten and cache is populated
        assertThat(model.getAll(Turnout.class)).containsExactly(turnout);
        assertThat(model.getCache(Turnout.class)).containsExactly(turnout);
        // assert that using cache works
        assertThat(model.getAll(Turnout.class)).containsExactly(turnout);
    }

    @Test
    void testGet() {
        assertThat(model.get(Turnout.class, "foo")).isNull();
        model.put(new AbstractDelegatingDiscreteStateElement() {
            @Override
            public String getName() {
                return "bar";
            }
        });
        assertThat(model.get(Turnout.class, "bar")).isNull();
        Turnout turnout = new Turnout();
        turnout.setName("foo");
        model.put(turnout);
        assertThat(model.get(Turnout.class, "foo")).isEqualTo(turnout);
    }

    @Test
    void testGetOrCreate() {
        assertThat(model.get(Turnout.class, "foo")).isNull();
        Turnout turnout1 = model.getOrCreate(Turnout.class, "foo", Lookup.EMPTY);
        assertThat(turnout1).isNotNull();
        assertThat(model.getAll(Turnout.class)).containsExactly(turnout1);
        Turnout turnout2 = model.getOrCreate(Turnout.class, "bar", null);
        assertThat(turnout2).isNotNull();
        assertThat(model.getAll(Turnout.class)).containsExactlyInAnyOrder(turnout1, turnout2);
        assertThat(model.getOrCreate(Turnout.class, "bar", Lookup.EMPTY)).isEqualTo(turnout2);
    }

    @Test
    void testPut() {
        Turnout turnout = new Turnout();
        turnout.setName("foo");
        assertThat(turnout.getPropertyChangeListeners()).isEmpty();
        assertThat(turnout.getVetoableChangeListeners()).isEmpty();
        assertThat(turnout.getPropertyChangeListeners("name")).isEmpty();
        assertThat(turnout.getVetoableChangeListeners("name")).isEmpty();
        assertThat(model.getAll(Turnout.class)).isEmpty();
        assertThat(model.getCache(Turnout.class)).isNotNull();
        model.put(turnout);
        assertThat(model.getCache(Turnout.class)).isNull();
        assertThat(model.getAll(Turnout.class)).containsExactly(turnout);
        assertThat(turnout.getPropertyChangeListeners()).doesNotContain(model);
        assertThat(turnout.getVetoableChangeListeners()).doesNotContain(model);
        assertThat(turnout.getPropertyChangeListeners("name")).containsExactly(model);
        assertThat(turnout.getVetoableChangeListeners("name")).containsExactly(model);
        assertThatCode(() -> model.put(turnout)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testRemove() {
        Turnout turnout = model.create(Turnout.class, "foo");
        assertThat(turnout.getPropertyChangeListeners("name")).containsExactly(model);
        assertThat(turnout.getVetoableChangeListeners("name")).containsExactly(model);
        assertThat(model.getAll(Turnout.class)).containsExactly(turnout);
        assertThat(model.getCache(Turnout.class)).isNotNull();
        model.remove(turnout);
        assertThat(turnout.getPropertyChangeListeners("name")).isEmpty();
        assertThat(turnout.getVetoableChangeListeners("name")).isEmpty();
        assertThat(model.getCache(Turnout.class)).isNull();
        assertThat(model.getAll(Turnout.class)).isEmpty();
    }

    @Test
    void testPropertyChange() {
        Turnout turnout = model.create(Turnout.class, "foo");
        assertThat(model.get(Element.class, "foo")).isEqualTo(turnout);
        assertThat(model.get(Element.class, "bar")).isNull();
        turnout.setName("bar");
        assertThat(model.get(Element.class, "foo")).isNull();
        assertThat(model.get(Element.class, "bar")).isEqualTo(turnout);
        // TODO: how to assert DefaultModel ignores changes to properties other
        // than name?
        model.propertyChange(new PropertyChangeEvent(turnout, "state", Turnout.State.UNKNOWN, Turnout.State.CLOSED));
        assertThat(model.get(Element.class, "foo")).isNull();
        assertThat(model.get(Element.class, "bar")).isEqualTo(turnout);
    }

}
