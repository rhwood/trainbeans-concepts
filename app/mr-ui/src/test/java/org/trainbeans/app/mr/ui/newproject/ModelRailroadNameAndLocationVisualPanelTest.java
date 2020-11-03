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
package org.trainbeans.app.mr.ui.newproject;

import java.io.File;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.assertj.swing.core.BasicComponentFinder;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 *
 * @author rhwood
 */
class ModelRailroadNameAndLocationVisualPanelTest {

    private ModelRailroadNameAndLocationVisualPanel panel;
    private File testDir;

    @BeforeAll
    static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    void setUp(@TempDir File tempDir) {
        testDir = tempDir;
        panel = GuiActionRunner.execute(() -> new ModelRailroadNameAndLocationVisualPanel());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetName() {
        assertThat(panel.getName()).isEqualTo("Name and Location");
    }

    @Test
    void testGetProjectLocation() throws IOException {
        assertThat(panel.getProjectLocation()).isEmpty();
        GuiActionRunner.execute(() -> assertThatCode(
                () -> panel.setProjectLocation(testDir.getCanonicalPath()))
                .doesNotThrowAnyException());
        assertThat(panel.getProjectLocation()).isEqualTo(testDir.getCanonicalPath());
    }

    @Test
    void testGetProjectName() {
        assertThat(panel.getProjectName()).isEqualTo("modelrailroad1");
        GuiActionRunner.execute(() -> assertThatCode(() -> panel.setProjectName("test"))
                .doesNotThrowAnyException());
        assertThat(panel.getProjectName()).isEqualTo("test");
    }

    @Test
    void testGetProjectFolder() throws IOException {
        assertThat(panel.getProjectFolder()).isEmpty();
        GuiActionRunner.execute(() -> assertThatCode(
                () -> panel.setProjectFolder(testDir.getCanonicalPath()))
                .doesNotThrowAnyException());
        assertThat(panel.getProjectFolder()).isEqualTo(testDir.getCanonicalPath());
    }

}
