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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.ParserConfigurationException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assumptions.assumeThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.openide.xml.XMLUtil;
import org.trainbeans.app.mr.ModelRailroadProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author rhwood
 */
class MRAuxiliaryConfigurationTest {

    private final String PROJECT_XML = "trainbeans/project.xml";
    private final String PRIVATE_XML = "trainbeans/private.xml";
    private final String ELEMENT_NAME1 = "element1";
    private final String ELEMENT_NAME2 = "element2";
    private final String XML_NS1 = "ns1";
    private final String XML_NS2 = "ns2";

    private Document document;
    private ModelRailroadProject project;
    private AuxiliaryConfiguration config;
    private final ProjectState state = new ProjectState() {
        @Override
        public void markModified() {
            // empty implementation
        }

        @Override
        public void notifyDeleted() {
            // empty implementation
        }
    };

    @BeforeEach
    void setUp(@TempDir File testDir) throws ParserConfigurationException {
        document = XMLUtil.createDocument("config", "http://www.netbeans.org/ns/auxiliary-configuration/1", null, null);
        project = new ModelRailroadProject(FileUtil.toFileObject(testDir), Lookup.EMPTY);
        config = new MRAuxiliaryConfiguration(project, state);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testGetConfigurationFragment(boolean arg) throws IOException {
        // test "normal" conditions (read / write capable)
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg))
                .isNull();
        Element e = document.createElementNS(XML_NS1, ELEMENT_NAME1);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        assertThat(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML)).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject(arg ? PRIVATE_XML : PROJECT_XML)).isNull();
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testGetConfigurationFragmentUnreadable(boolean arg) throws IOException {
        // test unreadable conditions (not expected in normal operations)
        // unable to set file unreadable by owner in Windows?
        assumeThat(Utilities.isWindows()).isFalse();
        Element e = document.createElementNS(XML_NS1, ELEMENT_NAME1);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        assertThat(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML)).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject(arg ? PRIVATE_XML : PROJECT_XML)).isNull();
        File file = FileUtil.toFile(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML));
        file.setReadable(false);
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNull();
        file.setReadable(true);
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testPutConfigurationFragment(boolean arg) throws IOException {
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg))
                .isNull();
        Element e = document.createElementNS(XML_NS1, ELEMENT_NAME1);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        assertThat(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML)).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject(arg ? PRIVATE_XML : PROJECT_XML)).isNull();
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        e = document.createElementNS(XML_NS1, ELEMENT_NAME2);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        e = config.getConfigurationFragment(ELEMENT_NAME2, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        e = document.createElementNS(XML_NS1, ELEMENT_NAME2);
        e.setAttribute("bar", "foo");
        config.putConfigurationFragment(e, arg);
        e = config.getConfigurationFragment(ELEMENT_NAME2, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("bar")).isEqualTo("foo");
        assertThat(e.getAttribute("foo")).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testPutConfigurationFragment_ComplexDocument(boolean arg) throws IOException, InterruptedException {
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg))
                .isNull();
        // create a document to read
        Element e = document.createElementNS(XML_NS1, ELEMENT_NAME1);
        e.setAttribute("foo", "bar");
        Element de = document.getDocumentElement();
        de.setAttribute("foo", "bar");
        de.setTextContent("some text");
        de.appendChild(e);
        File file = new File(FileUtil.toFile(project.getProjectDirectory()), arg ? PROJECT_XML : PRIVATE_XML);
        file.getParentFile().mkdirs();
        XMLUtil.write(document, new FileOutputStream(file), StandardCharsets.UTF_8.name());
        // read the document into MRAuxiliaryConfiguration
        config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        // test that put handles everything in document correctly
        e = document.createElementNS(XML_NS2, ELEMENT_NAME1);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS2, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        e = document.createElementNS(XML_NS2, ELEMENT_NAME1);
        e.setAttribute("bar", "foo");
        config.putConfigurationFragment(e, arg);
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS2, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("bar")).isEqualTo("foo");
        assertThat(e.getAttribute("foo")).isEmpty();
        assertThat(FileUtil.toFileObject(file.getCanonicalFile()).asText()).contains("some text");
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testPutConfigurationFragment_Unwritable(boolean arg) throws IOException {
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg))
                .isNull();
        Element e = document.createElementNS(XML_NS1, ELEMENT_NAME1);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        assertThat(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML)).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject(arg ? PRIVATE_XML : PROJECT_XML)).isNull();
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        // make file unwritable
        File file = FileUtil.toFile(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML));
        file.setWritable(false);
        // add a fragment and fail
        Element e2 = document.createElementNS(XML_NS1, ELEMENT_NAME2);
        e2.setAttribute("foo", "bar");
        assertThatCode(() -> config.putConfigurationFragment(e2, arg)).doesNotThrowAnyException();
        config = new MRAuxiliaryConfiguration(project, state);
        assertThat(config.getConfigurationFragment(ELEMENT_NAME2, XML_NS1, arg)).isNull();
        assertThat(e).isNotNull();
        // allow cleanup
        file.setWritable(true);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testRemoveConfigurationFragment(boolean arg) throws IOException {
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg))
                .isNull();
        Element e = document.createElementNS(XML_NS1, ELEMENT_NAME1);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        assertThat(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML)).isNotNull();
        assertThat(project.getProjectDirectory().getFileObject(arg ? PRIVATE_XML : PROJECT_XML)).isNull();
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        config.removeConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg)).isNull();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testRemoveConfigurationFragment_NonExistant(boolean arg) throws IOException {
        // first create a fragment to generate XML to remove fragement from
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg))
                .isNull();
        Element e = document.createElementNS(XML_NS1, ELEMENT_NAME1);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        // remove that fragment and remove it again
        config.removeConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg)).isNull();
        assertThatCode(() -> config.removeConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg))
                .doesNotThrowAnyException();
        // remove a different fragment
        assertThat(config.getConfigurationFragment(ELEMENT_NAME2, XML_NS1, arg)).isNull();
        assertThatCode(() -> config.removeConfigurationFragment(ELEMENT_NAME2, XML_NS1, arg))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testRemoveConfigurationFragment_Unwritable(boolean arg) throws IOException {
        // first create a fragment to generate XML to remove fragement from
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg))
                .isNull();
        Element e = document.createElementNS(XML_NS1, ELEMENT_NAME1);
        e.setAttribute("foo", "bar");
        config.putConfigurationFragment(e, arg);
        e = config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg);
        assertThat(e).isNotNull();
        assertThat(e.getAttribute("foo")).isEqualTo("bar");
        // make file unwritable
        File file = FileUtil.toFile(project.getProjectDirectory().getFileObject(arg ? PROJECT_XML : PRIVATE_XML));
        file.setWritable(false);
        // remove that fragment (and fail)
        assertThatCode(() -> config.removeConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg))
                .doesNotThrowAnyException();
        assertThat(config.getConfigurationFragment(ELEMENT_NAME1, XML_NS1, arg)).isNotNull();
        // remove a different fragment
        assertThat(config.getConfigurationFragment(ELEMENT_NAME2, XML_NS1, arg)).isNull();
        assertThatCode(() -> config.removeConfigurationFragment(ELEMENT_NAME2, XML_NS1, arg))
                .doesNotThrowAnyException();
    }
}
