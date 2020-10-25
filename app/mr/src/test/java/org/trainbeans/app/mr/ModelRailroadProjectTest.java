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
import java.io.IOException;
import java.nio.file.Files;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectInformationProvider;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileUtil;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author rhwood
 */
class ModelRailroadProjectTest {

    private ModelRailroadProject project;
    private File projectDir;

    @BeforeEach
    void setUp(@TempDir File testDir) throws IOException {
        projectDir = testDir.getCanonicalFile();
        project = new ModelRailroadProject(FileUtil.toFileObject(projectDir), Lookup.EMPTY);
        MockLookup.setLookup(Lookups.fixed(project.getLookup()));
        MockLookup.setInstances(new TestUtil.MockProjectManager(),
                new ProjectInformationProviderImpl());
    }

    @AfterEach
    void tearDown() throws IOException {
        projectDir.setWritable(true);
        Files.walk(projectDir.toPath()).forEach(path -> path.toFile().setWritable(true));
    }

    @Test
    void testConstructor() {
        assertThatCode(() -> new ModelRailroadProject(null, Lookup.EMPTY)).isExactlyInstanceOf(NullPointerException.class);
        assertThatCode(() -> new ModelRailroadProject(FileUtil.toFileObject(projectDir), null)).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void testGetProjectDirectory() {
        assertThat(project.getProjectDirectory())
                .isNotNull()
                .isEqualTo(FileUtil.toFileObject(projectDir));
    }

    @Test
    void testGetLookup() {
        assertThat(project.getLookup()).isNotNull();
        Lookup lookup = project.getLookup();
        assertThat(lookup.lookup(ModelRailroadProject.class)).isEqualTo(project);
        assertThat(lookup.lookup(ProjectInformation.class)).isNotNull();
        assertThat(lookup.lookup(LogicalViewProvider.class)).isNotNull();
    }

    @Test
    void testProjectInformationGetName() {
        ProjectInformation pi = project.getLookup().lookup(ProjectInformation.class);
        assertThat(pi.getName()).isEqualTo(projectDir.getName());
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

    @Test
    void testLogicalViewProviderFindPath() {
        assertThat(project.getLookup().lookup(LogicalViewProvider.class).findPath(Node.EMPTY, this)).isNull();
    }

    @Test
    void testLogicalViewProviderGetActions() {
        Node node = project.getLookup().lookup(LogicalViewProvider.class).createLogicalView();
        assertThat(node.getActions(true)).hasSize(5);
        assertThat(node.getActions(false)).hasSize(5);
    }

    @Test
    void testLogicalViewProviderGetDisplayName() {
        Node node = project.getLookup().lookup(LogicalViewProvider.class).createLogicalView();
        assertThat(node.getDisplayName()).isEqualTo(projectDir.getName());
    }

    @Test
    void testLogicalViewProviderGetIcon() {
        Node node = project.getLookup().lookup(LogicalViewProvider.class).createLogicalView();
        for (int i = 0; i < 10; i++) {
            assertThat(node.getIcon(i)).isEqualTo(ImageUtilities.loadImage("org/trainbeans/app/mr/icon.png"));
        }
    }

    @Test
    void testLogicalViewProviderOpenedIcon() {
        Node node = project.getLookup().lookup(LogicalViewProvider.class).createLogicalView();
        for (int i = 0; i < 10; i++) {
            assertThat(node.getOpenedIcon(i)).isEqualTo(node.getIcon(i));
        }
    }

    private static class ProjectInformationProviderImpl implements ProjectInformationProvider {

        @Override
        public ProjectInformation getProjectInformation(final Project project) {
            // DO NOT use the recommended method
            // org.netbeans.api.project.ProjectUtils.getInformation(project)
            // since that requires excessive setup for a test
            return project.getLookup().lookup(ProjectInformation.class);
        }
    }
}
