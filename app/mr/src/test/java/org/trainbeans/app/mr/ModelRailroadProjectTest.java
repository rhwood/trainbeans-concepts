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
package org.trainbeans.app.mr;

import java.io.File;
import javax.swing.ImageIcon;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.netbeans.api.project.ProjectInformation;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

/**
 *
 * @author rhwood
 */
class ModelRailroadProjectTest {

    private ModelRailroadProject project;
    private File testDir;

    @BeforeEach
    void setUp(@TempDir File projectDir) {
        testDir = projectDir;
        project = new ModelRailroadProject(FileUtil.toFileObject(projectDir));
    }

    @Test
    void testConstructor() {
        assertThatCode(() -> new ModelRailroadProject(null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void testGetProjectDirectory() {
        assertThat(project.getProjectDirectory())
                .isNotNull()
                .isEqualTo(FileUtil.toFileObject(testDir));
    }

    @Test
    void testGetLookup() {
        assertThat(project.getLookup()).isNotNull();
        Lookup lookup = project.getLookup();
        assertThat(lookup.lookup(ModelRailroadProject.class)).isEqualTo(project);
    }

    @Test
    void testProjectInformationGetName() {
        ProjectInformation pi = project.getLookup().lookup(ProjectInformation.class);
        assertThat(pi.getName()).isEqualTo(testDir.getName());
        assertThat(pi.getDisplayName()).isEqualTo(pi.getName());
    }

    @Test
    void testProjectInformationGetDisplayName() {
        ProjectInformation pi = project.getLookup().lookup(ProjectInformation.class);
        assertThat(pi.getDisplayName()).isEqualTo(pi.getName());
    }

    @Test
    void testProjectInformationGetIcon() {
        ProjectInformation pi = project.getLookup().lookup(ProjectInformation.class);
        assertThat(pi.getIcon()).isNotNull();
    }

    @Test
    void testProjectInformationGetProject() {
        ProjectInformation pi = project.getLookup().lookup(ProjectInformation.class);
        assertThat(pi.getProject()).isEqualTo(project);
    }
}
