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

import java.beans.IntrospectionException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openide.util.lookup.Lookups;
import org.trainbeans.model.api.Turnout;
import org.trainbeans.model.impl.DefaultModel;
import org.trainbeans.model.impl.TurnoutFactory;

/**
 *
 * @author rhwood
 */
public class ElementClassNodeTest {

    private DefaultModel model;

    @BeforeEach
    void setUp() {
        model = new DefaultModel(Lookups.fixed(new TurnoutFactory()));
        model.create(Turnout.class, "foo");
        model.create(Turnout.class, "bar");
    }
    
    @Test
    public void testConstructor() throws IntrospectionException {
        ElementClassNode node = new ElementClassNode(model, Turnout.class);
        assertThat(node).isNotNull();
        assertThat(node.getChildren().getNodes().length).isEqualTo(2);
    }
    
}
