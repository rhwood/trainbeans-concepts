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
package org.trainbeans.connection.jmri.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
class ConstantTest {

    @Test
    void testConstructor() throws NoSuchMethodException {
        Constructor<Constant> constructor = Constant.class
                .getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThatCode(() -> constructor.newInstance())
                .isExactlyInstanceOf(InvocationTargetException.class)
                .hasCauseExactlyInstanceOf(UnsupportedOperationException.class);
    }

}
