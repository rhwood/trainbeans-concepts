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

import org.trainbeans.app.mr.MockLookup;
import org.trainbeans.app.mr.TestUtil;
import java.awt.Dialog;
import java.io.File;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.modules.openide.util.NbMutexEventProvider;
import org.netbeans.spi.project.ProjectInformationProvider;
import org.openide.filesystems.FileUtil;
import org.openide.util.lookup.Lookups;
import org.trainbeans.app.mr.ModelRailroadProject;

/**
 *
 * @author rhwood
 */
class ModelRailroadCustomizerProviderTest {

    private ModelRailroadCustomizerProvider provider;
    private ModelRailroadProject project;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    void setUp(@TempDir File projectDir) {
        project = new ModelRailroadProject(FileUtil.toFileObject(projectDir));
        MockLookup.setLookup(Lookups.fixed(project.getLookup()));
        MockLookup.setInstances(new TestUtil.MockProjectManager(),
                new NbMutexEventProvider(),
                new ProjectInformationProviderImpl());
        provider = new ModelRailroadCustomizerProvider(project);
    }

    @Test
    void testShowCustomizer() {
        assertThat(provider.getDialog()).isNull();
        Dialog dialog = GuiActionRunner.execute(() -> {
            provider.showCustomizer();
            return provider.getDialog();
        });
        assertThat(provider.getDialog()).isNotNull().isInstanceOf(Dialog.class);
        DialogFixture fixture = new DialogFixture(dialog);
        fixture.button(JButtonMatcher.withText("OK")).click();
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
