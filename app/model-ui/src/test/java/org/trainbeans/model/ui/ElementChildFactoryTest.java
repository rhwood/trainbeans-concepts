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
package org.trainbeans.model.ui;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openide.util.lookup.Lookups;
import org.trainbeans.model.api.Element;
import org.trainbeans.model.api.Model;
import org.trainbeans.model.api.Turnout;
import org.trainbeans.model.impl.DefaultModel;
import org.trainbeans.model.impl.TurnoutFactory;

/**
 *
 * @author rhwood
 */
class ElementChildFactoryTest {

    private ElementChildFactory factory;
    private DefaultModel model;

    @BeforeEach
    void setUp() {
        model = new DefaultModel(Lookups.fixed(new TurnoutFactory()));
        model.create(Turnout.class, "foo");
        model.create(Turnout.class, "bar");
        factory = new ElementChildFactory(model, Turnout.class);
    }

    @Test
    void testCreateKeys() {
        List<Element> list = new ArrayList<>();
        assertThat(factory.createKeys(list)).isTrue();
        assertThat(list)
                .containsExactlyInAnyOrder(model.getAll(Turnout.class)
                        .toArray(new Element[0]));
    }

    @Test
    void testCreateNodeForKey() {
        assertThat(factory.createNodeForKey(model.get(Turnout.class, "foo")))
                .isNotNull()
                .isInstanceOf(ElementNode.class);
        assertThatCode(()
                -> factory.createNodeForKey(model.get(Turnout.class, "null")))
                .isExactlyInstanceOf(NullPointerException.class);
    }

}
