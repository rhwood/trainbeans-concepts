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
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
class ElementTest {

    @Test
    void testCompareTo() {
        Element e1 = new TestElementImpl().setName("foo");
        Element e2 = new TestElementImpl().setName("bar");
        Element e3 = new TestElementImpl().setName("foo");
        assertThat(e1.compareTo(e2)).isPositive();
        assertThat(e2.compareTo(e1)).isNegative();
        assertThat(e1.compareTo(e3)).isZero();
        assertThat(e3.compareTo(e1)).isZero();
    }
}
