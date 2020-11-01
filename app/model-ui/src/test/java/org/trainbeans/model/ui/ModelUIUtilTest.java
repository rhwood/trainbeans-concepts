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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openide.util.Lookup;
import org.trainbeans.model.api.Turnout;

/**
 *
 * @author rhwood
 */
class ModelUIUtilTest {

    private ModelUIUtil util;

    @BeforeEach
    void setUp() {
        util = new ModelUIUtil(Lookup.getDefault());
    }

    @Test
    void testGetSingularName() {
        assertThat(util.getSingularName(Turnout.class))
                .isEqualTo(new TurnoutClassDescriptor().getSingularName());
        assertThatCode(() -> util.getSingularName(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void testGetPluralName() {
        assertThat(util.getPluralName(Turnout.class))
                .isEqualTo(new TurnoutClassDescriptor().getPluralName());
        assertThatCode(() -> util.getPluralName(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

}
