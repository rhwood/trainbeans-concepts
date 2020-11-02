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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.EditableProperties;
import org.openide.util.Lookup;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Element;

/**
 *
 * @author rhwood
 */
class ModelRailroadHelperTest {

    private FileObject projectDir;
    private ModelRailroadProject project;
    private ModelRailroadHelper helper;

    @BeforeEach
    void setUp(@TempDir File testDir) throws IOException {
        projectDir = FileUtil.toFileObject(testDir.getCanonicalFile());
        MockLookup.setInstances(new ProjectState() {
            @Override
            public void markModified() {
                // nothing to do
            }

            @Override
            public void notifyDeleted() {
                // nothing to do
            }
        });
        project = new ModelRailroadProject(projectDir, Lookup.EMPTY);
        helper = new ModelRailroadHelper(project);
    }

    @Test
    void testGetProperties() {
        assertThat(helper.getProperties())
                .isNotNull()
                .isExactlyInstanceOf(EditableProperties.class);
    }

    @Test
    void testLoadProperties() {
        assertThatCode(() -> helper.loadProperties()).doesNotThrowAnyException();
    }

    @Test
    void testStoreProperties() {
        assertThatCode(() -> helper.storeProperties()).doesNotThrowAnyException();
    }

    @Test
    void testGetConfigurationFragment() {
        assertThat(helper.getConfigurationFragment("test", "test", true)).isNull();
    }

    @Test
    void testPutConfigurationFragment() {
        Element e = XMLUtil.createDocument("config", "http://www.netbeans.org/ns/auxiliary-configuration/1", null, null).createElementNS("test", "test");
        assertThatCode(() -> helper.putConfigurationFragment(e, true)).doesNotThrowAnyException();
    }

    @Test
    void testRemoveConfigurationFragment() {
        assertThat(helper.removeConfigurationFragment("test", "test", true)).isFalse();
    }

}
