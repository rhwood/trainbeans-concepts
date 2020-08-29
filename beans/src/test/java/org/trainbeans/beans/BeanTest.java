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
public class BeanTest {

    private Bean bean;
    private PropertyChangeListener listener;
    private int heard;
    
    @BeforeEach
    public void setUp() {
        bean = new Bean();
        heard = 0;
        listener = (PropertyChangeEvent evt) -> heard++;
    }
    
    @AfterEach
    public void tearDown() {
        // nothing to do
    }

    @Test
    public void testAddPropertyChangeListener_PropertyChangeListener() {
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(0);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
        bean.addPropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(1);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
        bean.addPropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(2);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
    }

    @Test
    public void testAddPropertyChangeListener_String_PropertyChangeListener() {
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(0);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
        bean.addPropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(1);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(1);
        bean.addPropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(2);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(2);
    }

    @Test
    public void testRemovePropertyChangeListener_PropertyChangeListener() {
        bean.addPropertyChangeListener("foo", listener);
        bean.addPropertyChangeListener("foo", listener);
        bean.addPropertyChangeListener(listener);
        bean.addPropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(4);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(2);
        bean.removePropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(3);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(2);
        bean.removePropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(2);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(2);
    }

    @Test
    public void testRemovePropertyChangeListener_String_PropertyChangeListener() {
        bean.addPropertyChangeListener("foo", listener);
        bean.addPropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(2);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(2);
        bean.removePropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(1);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(1);
        bean.removePropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(0);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
    }

    @Test
    public void testGetPropertyChangeListeners() {
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(0);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
        bean.addPropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(1);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
        bean.removePropertyChangeListener(listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(0);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
    }

    @Test
    public void testGetPropertyChangeListeners_String() {
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(0);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
        bean.addPropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(1);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(1);
        bean.removePropertyChangeListener("foo", listener);
        assertThat(bean.getPropertyChangeListeners().length).isEqualTo(0);
        assertThat(bean.getPropertyChangeListeners("foo").length).isEqualTo(0);
    }

    @Test
    public void testFirePropertyChange_PropertyChangeEvent() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isEqualTo(0);
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
    public void testFirePropertyChange_3args_1() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isEqualTo(0);
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
    public void testFirePropertyChange_3args_2() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isEqualTo(0);
        // differing numbers get fired
        bean.firePropertyChange("foo", 0, 1);
        assertThat(heard).isEqualTo(1);
        // same numbers do not get fired
        bean.firePropertyChange("foo", 1, 1);
        assertThat(heard).isEqualTo(1);
    }

    @Test
    public void testFirePropertyChange_3args_3() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isEqualTo(0);
        // differing booleans get fired
        bean.firePropertyChange("foo", true, false);
        assertThat(heard).isEqualTo(1);
        // same booleans do not get fired
        bean.firePropertyChange("foo", true, true);
        assertThat(heard).isEqualTo(1);
    }

    @Test
    public void testFireIndexedPropertyChange_4args_1() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isEqualTo(0);
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
    public void testFireIndexedPropertyChange_4args_2() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isEqualTo(0);
        // differing integers get fired
        bean.fireIndexedPropertyChange("foo", 0, 1, 2);
        assertThat(heard).isEqualTo(1);
        // same integers do not get fired
        bean.fireIndexedPropertyChange("foo", 0, 1, 1);
        assertThat(heard).isEqualTo(1);
    }

    @Test
    public void testFireIndexedPropertyChange_4args_3() {
        bean.addPropertyChangeListener(listener);
        assertThat(heard).isEqualTo(0);
        // differing booleans get fired
        bean.fireIndexedPropertyChange("foo", 0, true, false);
        assertThat(heard).isEqualTo(1);
        // same booleans do not get fired
        bean.fireIndexedPropertyChange("foo", 0, true, true);
        assertThat(heard).isEqualTo(1);
    }
    
}
