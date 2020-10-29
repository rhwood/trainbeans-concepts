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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
class JmriJsonUtilTest {

    @Test
    void testIsValidName() {
        assertThat(JmriJsonUtil.isValidName("ITfoo", "T")).isTrue();
        assertThat(JmriJsonUtil.isValidName("I2Tfoo", "T")).isTrue();
        assertThat(JmriJsonUtil.isValidName("IT1", "T")).isTrue();
        assertThat(JmriJsonUtil.isValidName("I22T1", "T")).isTrue();
        assertThat(JmriJsonUtil.isValidName("foo", "T")).isFalse();
        assertThat(JmriJsonUtil.isValidName("IT", "T")).isFalse();
        assertThat(JmriJsonUtil.isValidName("I2T", "T")).isFalse();
    }

    @Test
    void testConstructor() throws NoSuchMethodException {
        Constructor<JmriJsonUtil> util = JmriJsonUtil.class
                .getDeclaredConstructor();
        util.setAccessible(true);
        assertThatCode(() -> util.newInstance())
                .isExactlyInstanceOf(InvocationTargetException.class)
                .hasCauseExactlyInstanceOf(UnsupportedOperationException.class);
    }

}
