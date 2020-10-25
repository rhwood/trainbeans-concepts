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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.trainbeans.app.mr.ModelRailroadProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rhwood
 */
class MRAuxiliaryConfigurationTest {

    private Document document;
    private ModelRailroadProject project;
    private AuxiliaryConfiguration config;

    @BeforeEach
    void setUp(@TempDir File testDir) throws ParserConfigurationException {
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        project = new ModelRailroadProject(FileUtil.toFileObject(testDir), Lookup.EMPTY);
        config = new MRAuxiliaryConfiguration(project, new ProjectState() {
            @Override
            public void markModified() {
                // empty implementation
            }

            @Override
            public void notifyDeleted() {
                // empty implementation
            }
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testGetConfigurationFragment(boolean arg) throws IOException {
        String PROJECT_XML = "trainbeans/project.xml";
        String PRIVATE_XML = "trainbeans/private.xml";
        String elementName = "testElement";
        String namespace = "test";
        // test "normal" conditions (read / write capable)
        assertThat(config.getConfigurationFragment(elementName, namespace, arg))
                .isNull();
        Element e = document.createElementNS(namespace, elementName);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        assertThat(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML)).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject(arg ? PRIVATE_XML : PROJECT_XML)).isNull();
        e = config.getConfigurationFragment(elementName, namespace, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        // test unreadable conditions (not expected in normal operations)
        File file = FileUtil.toFile(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML));
        file.setReadable(false);
        e = config.getConfigurationFragment(elementName, namespace, arg);
        assertThat(e).isNull();
        file.setReadable(true);
        e = config.getConfigurationFragment(elementName, namespace, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
    }


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testPutConfigurationFragment(boolean arg) throws IOException {
        String PROJECT_XML = "trainbeans/project.xml";
        String PRIVATE_XML = "trainbeans/private.xml";
        String elementName1 = "testElement1";
        String elementName2 = "testElement2";
        String namespace = "test";
        assertThat(config.getConfigurationFragment(elementName1, namespace, arg))
                .isNull();
        Element e = document.createElementNS(namespace, elementName1);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        assertThat(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML)).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject(arg ? PRIVATE_XML : PROJECT_XML)).isNull();
        e = config.getConfigurationFragment(elementName1, namespace, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        e = document.createElementNS(namespace, elementName2);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        e = config.getConfigurationFragment(elementName1, namespace, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        e = config.getConfigurationFragment(elementName2, namespace, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        e = document.createElementNS(namespace, elementName2);
        e.setAttribute("bar", "foo");
        config.putConfigurationFragment(e, arg);
        e = config.getConfigurationFragment(elementName2, namespace, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("bar")).isEqualTo("foo");
        assertThat(e.getAttribute("foo")).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testRemoveConfigurationFragment(boolean arg) throws IOException {
        String PROJECT_XML = "trainbeans/project.xml";
        String PRIVATE_XML = "trainbeans/private.xml";
        String elementName = "testElement";
        String namespace = "test";
        assertThat(config.getConfigurationFragment(elementName, namespace, arg))
                .isNull();
        Element e = document.createElementNS(namespace, elementName);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        assertThat(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML)).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject(arg ? PRIVATE_XML : PROJECT_XML)).isNull();
        e = config.getConfigurationFragment(elementName, namespace, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        config.removeConfigurationFragment(elementName, namespace, arg);
        assertThat(config.getConfigurationFragment(elementName, namespace, arg)).isNull();
    }

}
