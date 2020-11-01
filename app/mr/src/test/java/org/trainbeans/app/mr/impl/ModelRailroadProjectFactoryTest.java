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
package org.trainbeans.app.mr.impl;

import java.io.File;
import java.io.IOException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.trainbeans.app.mr.ModelRailroadProject;

/**
 *
 * @author rhwood
 */
class ModelRailroadProjectFactoryTest {

    private ModelRailroadProjectFactory factory;
    private ModelRailroadProject project;
    private FileObject projectDir;

    @BeforeEach
    void setUp(@TempDir File testDir) {
        factory = new ModelRailroadProjectFactory();
        projectDir = FileUtil.toFileObject(testDir);
        project = new ModelRailroadProject(FileUtil.toFileObject(testDir), Lookup.EMPTY);
    }

    @Test
    void testIsProject() throws IOException {
        // test with empty directory
        assertThat(factory.isProject(projectDir)).isFalse();
        // populate minimal project
        FileUtil.createData(projectDir, MRConstants.PROJECT_XML_PATH);
        // test with populated directory
        assertThat(factory.isProject(projectDir)).isTrue();
    }

    @Test
    void testLoadProject() throws IOException {
        ProjectState state = new ProjectState() {
            @Override
            public void markModified() {
                // nothing to do
            }

            @Override
            public void notifyDeleted() {
                //nothing to do
            }
        };
        // test with empty directory
        assertThat(factory.loadProject(projectDir, state)).isNull();
        // populate minimal project
        FileUtil.createData(projectDir, MRConstants.PROJECT_XML_PATH);
        // test with populated directory
        assertThat(factory.loadProject(projectDir, state))
                .isNotNull()
                .isEqualTo(project);
    }

    @Test
    void testSaveProject() {
        assertThatCode(() -> factory.saveProject(project)).doesNotThrowAnyException();
    }

}
