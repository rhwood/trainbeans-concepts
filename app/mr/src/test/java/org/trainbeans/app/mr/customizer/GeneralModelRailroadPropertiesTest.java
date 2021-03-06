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
package org.trainbeans.app.mr.customizer;

import javax.swing.JPanel;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openide.util.Lookup;

/**
 *
 * @author rhwood
 */
class GeneralModelRailroadPropertiesTest {

    private GeneralModelRailroadProperties gmrp;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    void setUp() {
        gmrp = GeneralModelRailroadProperties.createGeneral();
    }

    @Test
    void testCreateGeneral() {
        assertThat(GeneralModelRailroadProperties.createGeneral())
                .isNotNull()
                .isExactlyInstanceOf(GeneralModelRailroadProperties.class)
                .isNotEqualTo(gmrp);
    }

    @Test
    void testCreateCategory() {
        assertThat(gmrp.createCategory(Lookup.EMPTY)).isNotNull();
    }

    @Test
    void testCreateComponent() {
        GuiActionRunner.execute(() ->
            assertThat(gmrp.createComponent(gmrp.createCategory(Lookup.EMPTY),
                    Lookup.EMPTY)).isExactlyInstanceOf(JPanel.class));
    }

}
