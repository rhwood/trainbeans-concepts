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
package org.trainbeans.model.ui.explorer;

import org.trainbeans.model.ui.explorer.ModelTopComponent;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.swing.edt.GuiActionRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
class ModelTopComponentTest {

    private ModelTopComponent mtc;
    
    @BeforeEach
    void setUp() {
        mtc = GuiActionRunner.execute(() -> new ModelTopComponent());
    }

    @Test
    void testConstructor() {
        assertThat(mtc.getName()).isEqualTo(Bundle.CTL_ModelTopComponent());
        assertThat(mtc.getToolTipText()).isEqualTo(Bundle.HINT_ModelTopComponent());
    }

    @Test
    void testGetExplorerManager() {
        assertThat(mtc.getExplorerManager()).isNotNull();
    }
    
}
