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
import java.util.Date;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
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

    private File workspace;
    private Document document;
    private ModelRailroadProject project;
    private AuxiliaryConfiguration config;

    @BeforeEach
    void setUp(@TempDir File testDir) throws ParserConfigurationException {
        workspace = testDir;
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

    @Test
    void testGetConfigurationFragment_Shared() throws IOException {
        String id = Long.toString((new Date()).getTime());
        String elementName = "testElement";
        String namespace = "test";
        assertThat(config.getConfigurationFragment(elementName, namespace, true))
                .isNull();
        Element e = document.createElementNS(namespace, elementName);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, true);
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/project.xml")).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/private.xml")).isNull();
        e = config.getConfigurationFragment(elementName, namespace, true);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        config.removeConfigurationFragment(elementName, namespace, true);
        assertThat(config.getConfigurationFragment(elementName, namespace, true)).isNull();
    }

    @Test
    void testGetConfigurationFragment_Private() throws IOException {
        String id = Long.toString((new Date()).getTime());
        String elementName = "testElement";
        String namespace = "test";
        assertThat(config.getConfigurationFragment(elementName, namespace, false))
                .isNull();
        Element e = document.createElementNS(namespace, elementName);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, false);
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/project.xml")).isNull();
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/private.xml")).isNotNull();
        e = config.getConfigurationFragment(elementName, namespace, false);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        config.removeConfigurationFragment(elementName, namespace, false);
        assertThat(config.getConfigurationFragment(elementName, namespace, false)).isNull();
    }

    @Test
    void testPutConfigurationFragment_Shared() throws IOException {
        String id = Long.toString((new Date()).getTime());
        String elementName = "testElement";
        String namespace = "test";
        assertThat(config.getConfigurationFragment(elementName, namespace, true))
                .isNull();
        Element e = document.createElementNS(namespace, elementName);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, true);
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/project.xml")).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/private.xml")).isNull();
        e = config.getConfigurationFragment(elementName, namespace, true);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        config.removeConfigurationFragment(elementName, namespace, true);
        assertThat(config.getConfigurationFragment(elementName, namespace, true)).isNull();
    }

    @Test
    void testPutConfigurationFragment_Private() throws IOException {
        String id = Long.toString((new Date()).getTime());
        String elementName = "testElement";
        String namespace = "test";
        assertThat(config.getConfigurationFragment(elementName, namespace, false))
                .isNull();
        Element e = document.createElementNS(namespace, elementName);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, false);
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/project.xml")).isNull();
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/private.xml")).isNotNull();
        e = config.getConfigurationFragment(elementName, namespace, false);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        config.removeConfigurationFragment(elementName, namespace, false);
        assertThat(config.getConfigurationFragment(elementName, namespace, true)).isNull();
    }

    @Test
    void testRemoveConfigurationFragment_Shared() throws IOException {
        String id = Long.toString((new Date()).getTime());
        String elementName = "testElement";
        String namespace = "test";
        assertThat(config.getConfigurationFragment(elementName, namespace, true))
                .isNull();
        Element e = document.createElementNS(namespace, elementName);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, true);
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/project.xml")).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/private.xml")).isNull();
        e = config.getConfigurationFragment(elementName, namespace, true);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        config.removeConfigurationFragment(elementName, namespace, true);
        assertThat(config.getConfigurationFragment(elementName, namespace, true)).isNull();
    }

    @Test
    void testRemoveConfigurationFragment_Private() throws IOException {
        String id = Long.toString((new Date()).getTime());
        String elementName = "testElement";
        String namespace = "test";
        assertThat(config.getConfigurationFragment(elementName, namespace, false))
                .isNull();
        Element e = document.createElementNS(namespace, elementName);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, false);
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/project.xml")).isNull();
        assertThat(project.getProjectDirectory().getFileObject("trainbeans/private.xml")).isNotNull();
        e = config.getConfigurationFragment(elementName, namespace, false);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        config.removeConfigurationFragment(elementName, namespace, false);
        assertThat(config.getConfigurationFragment(elementName, namespace, false)).isNull();
    }

}
