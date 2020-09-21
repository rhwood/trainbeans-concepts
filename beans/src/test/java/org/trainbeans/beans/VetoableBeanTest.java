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
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
class VetoableBeanTest {

    private VetoableBean bean;
    private VetoableChangeListener vetoer;
    private PropertyChangeListener listener;
    private int heard;
    private int passed;
    private int vetoed;

    @BeforeEach
    void setUp() {
        bean = new VetoableBean();
        vetoed = 0;
        passed = 0;
        heard = 0;
        vetoer = (PropertyChangeEvent evt) -> {
            if (evt.getPropertyName().equals("veto")) {
                vetoed++;
                throw new PropertyVetoException("", evt);
            } else {
                passed++;
            }
        };
        listener = (PropertyChangeEvent evt) -> heard++;
    }

    @AfterEach
    void tearDown() {
        // nothing to do
    }

    @Test
    void testAddVetoableChangeListener_VetoableChangeListener() {
        assertThat(bean.getVetoableChangeListeners()).isEmpty();
        assertThat(bean.getVetoableChangeListeners("foo")).isEmpty();
        bean.addVetoableChangeListener(vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(1);
        assertThat(bean.getVetoableChangeListeners("foo")).isEmpty();
        bean.addVetoableChangeListener(vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(2);
        assertThat(bean.getVetoableChangeListeners("foo")).isEmpty();
    }

    @Test
    void testAddVetoableChangeListener_String_VetoableChangeListener() {
        assertThat(bean.getVetoableChangeListeners()).isEmpty();
        assertThat(bean.getVetoableChangeListeners("foo")).isEmpty();
        bean.addVetoableChangeListener("foo", vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(1);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(1);
        bean.addVetoableChangeListener("foo", vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(2);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(2);
    }

    @Test
    void testGetVetoableChangeListeners() {
        bean.addVetoableChangeListener("foo", vetoer);
        bean.addVetoableChangeListener("foo", vetoer);
        bean.addVetoableChangeListener(vetoer);
        bean.addVetoableChangeListener(vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(4);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(2);
        bean.removeVetoableChangeListener(vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(3);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(2);
        bean.removeVetoableChangeListener(vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(2);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(2);
    }

    @Test
    void testGetVetoableChangeListeners_String() {
        assertThat(bean.getVetoableChangeListeners()).isEmpty();
        assertThat(bean.getVetoableChangeListeners("foo")).isEmpty();
        bean.addVetoableChangeListener("foo", vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(1);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(1);
        bean.removeVetoableChangeListener("foo", vetoer);
        assertThat(bean.getVetoableChangeListeners()).isEmpty();
        assertThat(bean.getVetoableChangeListeners("foo")).isEmpty();
    }

    @Test
    void testRemoveVetoableChangeListener_VetoableChangeListener() {
        bean.addVetoableChangeListener("foo", vetoer);
        bean.addVetoableChangeListener("foo", vetoer);
        bean.addVetoableChangeListener(vetoer);
        bean.addVetoableChangeListener(vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(4);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(2);
        bean.removeVetoableChangeListener(vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(3);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(2);
        bean.removeVetoableChangeListener(vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(2);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(2);
    }

    @Test
    void testRemoveVetoableChangeListener_String_VetoableChangeListener() {
        bean.addVetoableChangeListener("foo", vetoer);
        bean.addVetoableChangeListener("foo", vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(2);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(2);
        bean.removeVetoableChangeListener("foo", vetoer);
        assertThat(bean.getVetoableChangeListeners()).hasSize(1);
        assertThat(bean.getVetoableChangeListeners("foo")).hasSize(1);
        bean.removeVetoableChangeListener("foo", vetoer);
        assertThat(bean.getVetoableChangeListeners()).isEmpty();
        assertThat(bean.getVetoableChangeListeners("foo")).isEmpty();
    }

    @Test
    void testFireVetoableChange_PropertyChangeEvent() throws Exception {
        bean.addVetoableChangeListener(vetoer);
        bean.addPropertyChangeListener(listener);
        assertThat(passed).isZero();
        assertThat(vetoed).isZero();
        assertThat(heard).isZero();
        // differing objects get fired
        bean.fireVetoableChange(new PropertyChangeEvent(bean, "foo", new Object(), new Object()));
        assertThat(passed).isEqualTo(1);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(1);
        // same objects do not get fired
        Object foo = new Object();
        bean.fireVetoableChange(new PropertyChangeEvent(bean, "foo", foo, foo));
        assertThat(passed).isEqualTo(1);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(1);
        // both objects null fires
        bean.fireVetoableChange(new PropertyChangeEvent(bean, "foo", null, null));
        assertThat(passed).isEqualTo(2);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(2);
        PropertyChangeEvent thrown = new PropertyChangeEvent(bean, "veto", null, null);
        PropertyVetoException ex = catchThrowableOfType(() -> bean.fireVetoableChange(thrown), PropertyVetoException.class);
        assertThat(ex.getPropertyChangeEvent()).isEqualTo(thrown);
        assertThat(heard).isEqualTo(2);
        assertThat(vetoed).isEqualTo(1);
        assertThat(passed).isEqualTo(2);
    }

    @Test
    void testFireVetoableChange_3args_1() throws Exception {
        bean.addVetoableChangeListener(vetoer);
        bean.addPropertyChangeListener(listener);
        assertThat(passed).isZero();
        assertThat(vetoed).isZero();
        assertThat(heard).isZero();
        // differing objects get fired
        bean.fireVetoableChange("foo", new Object(), new Object());
        assertThat(passed).isEqualTo(1);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(1);
        // same objects do not get fired
        Object foo = new Object();
        bean.fireVetoableChange("foo", foo, foo);
        assertThat(passed).isEqualTo(1);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(1);
        // both objects null fires
        bean.fireVetoableChange("foo", null, null);
        assertThat(passed).isEqualTo(2);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(2);
        // veto is thrown
        assertThatCode(() -> bean.fireVetoableChange("veto", null, null)).isInstanceOf(PropertyVetoException.class);
        assertThat(heard).isEqualTo(2);
        assertThat(vetoed).isEqualTo(1);
        assertThat(passed).isEqualTo(2);
    }

    @Test
    void testFireVetoableChange_3args_2() throws Exception {
        bean.addVetoableChangeListener(vetoer);
        bean.addPropertyChangeListener(listener);
        assertThat(passed).isZero();
        assertThat(vetoed).isZero();
        assertThat(heard).isZero();
        // differing integers get fired
        bean.fireVetoableChange("foo", 1, 2);
        assertThat(passed).isEqualTo(1);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(1);
        // same integers do not get fired
        Object foo = new Object();
        bean.fireVetoableChange("foo", 1, 1);
        assertThat(passed).isEqualTo(1);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(1);
        // veto is thrown
        assertThatCode(() -> bean.fireVetoableChange("veto", 1, 2)).isInstanceOf(PropertyVetoException.class);
        assertThat(heard).isEqualTo(1);
        assertThat(vetoed).isEqualTo(1);
        assertThat(passed).isEqualTo(1);
    }

    @Test
    void testFireVetoableChange_3args_3() throws Exception {
        bean.addVetoableChangeListener(vetoer);
        bean.addPropertyChangeListener(listener);
        assertThat(passed).isZero();
        assertThat(vetoed).isZero();
        assertThat(heard).isZero();
        // differing booleans get fired
        bean.fireVetoableChange("foo", true, false);
        assertThat(passed).isEqualTo(1);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(1);
        // same booleans do not get fired
        Object foo = new Object();
        bean.fireVetoableChange("foo", false, false);
        assertThat(passed).isEqualTo(1);
        assertThat(vetoed).isZero();
        assertThat(heard).isEqualTo(1);
        // veto is thrown
        assertThatCode(() -> bean.fireVetoableChange("veto", true, false)).isInstanceOf(PropertyVetoException.class);
        assertThat(heard).isEqualTo(1);
        assertThat(vetoed).isEqualTo(1);
        assertThat(passed).isEqualTo(1);
    }

    @Test
    void testHasListeners() {
        // test pass through
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
        bean.removePropertyChangeListener("foo", listener);
        // test vetoables
        assertThat(bean.hasListeners(null)).isFalse();
        assertThat(bean.hasListeners("foo")).isFalse();
        bean.addVetoableChangeListener(vetoer);
        assertThat(bean.hasListeners(null)).isTrue();
        assertThat(bean.hasListeners("foo")).isTrue();
        bean.addVetoableChangeListener("foo", vetoer);
        assertThat(bean.hasListeners(null)).isTrue();
        assertThat(bean.hasListeners("foo")).isTrue();
        bean.removeVetoableChangeListener(vetoer);
        assertThat(bean.hasListeners(null)).isFalse();
        assertThat(bean.hasListeners("foo")).isTrue();
    }
    
}
