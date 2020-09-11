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
package org.trainbeans.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author rhwood
 */
class BeanTest {

    private Bean bean;
    private PropertyChangeListener listener;
    private int heard;
    
    @BeforeEach
    void setUp() {
        bean = new Bean();
        heard = 0;
        listener = (PropertyChangeEvent evt) -> heard++;
    }
    
    @AfterEach
    void tearDown() {
        // nothing to do
    }

    @Test
    void testAddPropertyChangeListener_PropertyChangeListener() {
        assertThat(bean.getPropertyChangeListeners()).isEmpty();
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
        bean.addPropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(1);
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
        bean.addPropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(2);
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
    }

    @Test
    void testHasListeners() {
        assertThat(bean.hasListeners(null)).isFalse();
        assertThat(bean.hasListeners("foo")).isFalse();
        bean.addPropertyChangeListener(listener);
        assertThat(bean.hasListeners(null)).isTrue();
        assertThat(bean.hasListeners("foo")).isTrue();
        bean.addPropertyChangeListener("foo", listener);
        assertThat(bean.hasListeners(null)).isTrue();
        assertThat(bean.hasListeners("foo")).isTrue();
        bean.removePropertyChangeListener(listener);
        assertThat(bean.hasListeners(null)).isFalse();
        assertThat(bean.hasListeners("foo")).isTrue();
    }

    @Test
    void testAddPropertyChangeListener_String_PropertyChangeListener() {
        assertThat(bean.getPropertyChangeListeners()).isEmpty();
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
        bean.addPropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(1);
        assertThat(bean.getPropertyChangeListeners("foo")).hasSize(1);
        bean.addPropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(2);
        assertThat(bean.getPropertyChangeListeners("foo")).hasSize(2);
    }

    @Test
    void testRemovePropertyChangeListener_PropertyChangeListener() {
        bean.addPropertyChangeListener("foo", listener);
        bean.addPropertyChangeListener("foo", listener);
        bean.addPropertyChangeListener(listener);
        bean.addPropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(4);
        assertThat(bean.getPropertyChangeListeners("foo")).hasSize(2);
        bean.removePropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(3);
        assertThat(bean.getPropertyChangeListeners("foo")).hasSize(2);
        bean.removePropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(2);
        assertThat(bean.getPropertyChangeListeners("foo")).hasSize(2);
    }

    @Test
    void testRemovePropertyChangeListener_String_PropertyChangeListener() {
        bean.addPropertyChangeListener("foo", listener);
        bean.addPropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(2);
        assertThat(bean.getPropertyChangeListeners("foo")).hasSize(2);
        bean.removePropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(1);
        assertThat(bean.getPropertyChangeListeners("foo")).hasSize(1);
        bean.removePropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners()).isEmpty();
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
    }

    @Test
    void testGetPropertyChangeListeners() {
        assertThat(bean.getPropertyChangeListeners()).isEmpty();
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
        bean.addPropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(1);
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
        bean.removePropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners()).isEmpty();
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
    }

    @Test
    void testGetPropertyChangeListeners_String() {
        assertThat(bean.getPropertyChangeListeners()).isEmpty();
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
        bean.addPropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners()).hasSize(1);
        assertThat(bean.getPropertyChangeListeners("foo")).hasSize(1);
        bean.removePropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners()).isEmpty();
        assertThat(bean.getPropertyChangeListeners("foo")).isEmpty();
    }

    @Test
    void testFirePropertyChange_PropertyChangeEvent() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isZero();
        // differing objects get fired
        bean.firePropertyChange(new PropertyChangeEvent(bean, "foo", new Object(), new Object()));
        assertThat(heard).isEqualTo(1);
        // same objects do not get fired
        Object foo = new Object();
        bean.firePropertyChange(new PropertyChangeEvent(bean, "foo", foo, foo));
        assertThat(heard).isEqualTo(1);
        // both objects null fires
        bean.firePropertyChange(new PropertyChangeEvent(bean, "foo", null, null));
        assertThat(heard).isEqualTo(2);
    }

    @Test
    void testFirePropertyChange_3args_1() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isZero();
        // differing objects get fired
        bean.firePropertyChange("foo", new Object(), new Object());
        assertThat(heard).isEqualTo(1);
        // same objects do not get fired
        Object foo = new Object();
        bean.firePropertyChange("foo", foo, foo);
        assertThat(heard).isEqualTo(1);
        // both objects null fires
        bean.firePropertyChange("foo", null, null);
        assertThat(heard).isEqualTo(2);
    }

    @Test
    void testFirePropertyChange_3args_2() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isZero();
        // differing numbers get fired
        bean.firePropertyChange("foo", 0, 1);
        assertThat(heard).isEqualTo(1);
        // same numbers do not get fired
        bean.firePropertyChange("foo", 1, 1);
        assertThat(heard).isEqualTo(1);
    }

    @Test
    void testFirePropertyChange_3args_3() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isZero();
        // differing booleans get fired
        bean.firePropertyChange("foo", true, false);
        assertThat(heard).isEqualTo(1);
        // same booleans do not get fired
        bean.firePropertyChange("foo", true, true);
        assertThat(heard).isEqualTo(1);
    }

    @Test
    void testFireIndexedPropertyChange_4args_1() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isZero();
        // differing objects get fired
        bean.fireIndexedPropertyChange("foo", 0, new Object(), new Object());
        assertThat(heard).isEqualTo(1);
        // same objects do not get fired
        Object foo = new Object();
        bean.fireIndexedPropertyChange("foo", 0, foo, foo);
        assertThat(heard).isEqualTo(1);
        // both objects null fires
        bean.fireIndexedPropertyChange("foo", 0, null, null);
        assertThat(heard).isEqualTo(2);
    }

    @Test
    void testFireIndexedPropertyChange_4args_2() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isZero();
        // differing integers get fired
        bean.fireIndexedPropertyChange("foo", 0, 1, 2);
        assertThat(heard).isEqualTo(1);
        // same integers do not get fired
        bean.fireIndexedPropertyChange("foo", 0, 1, 1);
        assertThat(heard).isEqualTo(1);
    }

    @Test
    void testFireIndexedPropertyChange_4args_3() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isZero();
        // differing booleans get fired
        bean.fireIndexedPropertyChange("foo", 0, true, false);
        assertThat(heard).isEqualTo(1);
        // same booleans do not get fired
        bean.fireIndexedPropertyChange("foo", 0, true, true);
        assertThat(heard).isEqualTo(1);
    }
    
}
