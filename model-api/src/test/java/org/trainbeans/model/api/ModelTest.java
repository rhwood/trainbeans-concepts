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
import java.beans.PropertyVetoException;
import java.util.HashSet;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openide.util.Lookup;
import org.trainbeans.beans.Bean;
import org.trainbeans.beans.VetoableBean;

/**
 * Test of the default methods of Model and nothing else.
 * 
 * @author rhwood
 */
class ModelTest {
    
    ModelImpl model;

    @BeforeEach
    void setUp() {
        model = new ModelImpl();
    }
    
    @AfterEach
    void tearDown() {
        // nothing to do
    }

    @Test
    void testCreate_Class_String() {
        // only really testing that null Lookup does not cause error
        Element element = model.create(ElementImpl.class, "foo");
        assertThat(model.last).isNull();
        assertThat(element.getName()).isEqualTo("foo");
    }

    @Test
    void testVetoableChange() throws Exception {
        // test the vetoableChange throws if PropertyChangeEvent has property
        // "name" and an element with the same name
        // first verify does not throw
        ElementImpl element = new ElementImpl();
        PropertyChangeEvent event = new PropertyChangeEvent(element, "name", null, "foo");
        model.vetoableChange(event);
        // did not throw, create an element named "foo" and try again
        model.create(ElementImpl.class, "foo");
        PropertyVetoException ex = catchThrowableOfType(() -> model.vetoableChange(event), PropertyVetoException.class);
        assertThat(ex.getPropertyChangeEvent()).isEqualTo(event);
        assertThat(ex.getMessage()).isEqualTo("Element with name \"foo\" already exists.");
        assertThat(model.getAll(ElementImpl.class)).hasSize(1);
    }

    class ElementImpl extends VetoableBean implements Element {

        String name;

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }
    }

    class ModelImpl extends Bean implements Model {

        Lookup last = Lookup.EMPTY;
        Set<Element> elements = new HashSet<>();
        
        @Override
        public <T extends Element> T create(Class<T> type, String name, Lookup lookup) {
            last = lookup;
            // do not use factory in test model
            if (ElementImpl.class.equals(type)) {
                ElementImpl impl = new ElementImpl();
                impl.setName(name);
                elements.add(impl);
                return (T) impl;
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public <T extends Element> Set<T> getAll(Class<T> type) {
            return (Set<T>) elements;
        }

        @Override
        public <T extends Element> T get(Class<T> type, String name) {
            for (Element element : elements) {
                if (element.getName().equals(name)) {
                    return (T) element;
                }
            }
            return null;
        }

        @Override
        public <T extends Element> T getOrCreate(Class<T> type, String name) {
            Element element = get(type, name);
            if (element == null) {
                return create(type, name);
            }
            return null;
        }

        @Override
        public <T extends Element> void put(T element) {
            // not implemented
        }

        @Override
        public <T extends Element> void remove(T element) {
            // not implemented
        }
    }
    
}
