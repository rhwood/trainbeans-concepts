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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.xml.XMLUtil;
import org.trainbeans.app.mr.ModelRailroadProject;
import static org.trainbeans.app.mr.MRConstants.PROJECT_XML_PATH;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author rhwood
 */
public final class MRAuxiliaryConfiguration implements AuxiliaryConfiguration {

    /**
     * XML name space of the root element. Note the URL does not actually exist,
     * but only serves as a unique identifier for the name space.
     */
    public static final String ROOT_XML_NS
            = "http://www.netbeans.org/ns/auxiliary-configuration/1"; // NOI18N
    /**
     * The project this class instance is supporting.
     */
    private final ModelRailroadProject project;
    /**
     * The project state listener.
     */
    private final ProjectState state;
    /**
     * The standard (shared) XML document. This is expected to contain project
     * configuration that can be used in multiple computers.
     */
    private Document projectXml;
    /**
     * The private (not shared) XML document. This is expected to contain
     * project configuration that is specific to a single computer.
     */
    private Document privateXml;
    /**
     * Write locks for XML documents.
     */
    private final Set<String> modifiedMetadataPaths = new HashSet<>();
    /**
     * Not shared XML path relative to project root.
     */
    private static final String PRIVATE_XML_PATH = "trainbeans/private.xml";

    /**
     * Create an {@link AuxiliaryConfiguration} for a
     * {@link ModelRailroadProject}.
     *
     * @param aProject the project this configuration is for
     * @param aProjectState the project state to notify of changes
     */
    public MRAuxiliaryConfiguration(final ModelRailroadProject aProject,
            final ProjectState aProjectState) {
        Objects.requireNonNull(aProject);
        Objects.requireNonNull(aProjectState);
        project = aProject;
        state = aProjectState;
    }

    @Override
    public Element getConfigurationFragment(final String elementName,
            final String namespace, final boolean shared) {
        return ProjectManager.mutex().readAccess(() -> {
            FileObject file = this.getConfigurationFile(shared, false);
            if (file != null && file.canRead()) {
                Element root = getConfigurationDataRoot(shared);
                return XMLUtil.findElement(root, elementName, namespace);
            }
            return null;
        });
    }

    @Override
    public void putConfigurationFragment(final Element fragment,
            final boolean shared) {
        ProjectManager.mutex().writeAccess(() -> {
            synchronized (modifiedMetadataPaths) {
                Element root = getConfigurationDataRoot(shared);
                Element existing = XMLUtil.findElement(root,
                        fragment.getLocalName(), fragment.getNamespaceURI());
                if (existing != null) {
                    root.removeChild(existing);
                }
                // nodes are alphabetized, so find correct place to insert node
                Node ref = null;
                NodeList list = root.getChildNodes();
                for (int i = 0; i < list.getLength(); i++) {
                    Node node = list.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        int comparison = node.getNodeName()
                                .compareTo(fragment.getNodeName());
                        if (comparison == 0) {
                            comparison = node.getNamespaceURI()
                                    .compareTo(fragment.getNamespaceURI());
                        }
                        if (comparison > 0) {
                            ref = node;
                            break;
                        }
                    }
                }
                root.insertBefore(root.getOwnerDocument()
                        .importNode(fragment, true), ref);
                write(shared);
            }
        });
    }

    @Override
    public boolean removeConfigurationFragment(final String elementName,
            final String namespace,
            final boolean shared) {
        return ProjectManager.mutex().writeAccess(() -> {
            FileObject file = this.getConfigurationFile(shared, false);
            if (file != null && file.canWrite()) {
                try {
                    Document doc = getConfigurationXml(shared);
                    Element root = doc.getDocumentElement();
                    Element toRemove = XMLUtil.findElement(root,
                            elementName,
                            namespace);
                    if (toRemove != null) {
                        root.removeChild(toRemove);
                        // this.backup(shared); // should we backup?
                        write(shared);
                        return true;
                    }
                } catch (DOMException ex) {
                    // log removal error
                    Exceptions.printStackTrace(ex);
                }
            }
            return false;
        });
    }

    private FileObject getConfigurationFile(final boolean shared,
            final boolean create) {
        // non-shared location should be in different directory
        String path = shared ? PROJECT_XML_PATH : PRIVATE_XML_PATH;
        if (create) {
            try {
                return FileUtil.createData(project.getProjectDirectory(), path);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
                return null;
            }
        } else {
            return project.getProjectDirectory().getFileObject(path);
        }
    }

    /**
     * Get the <code>&lt;configuration&gt;</code> element of project.xml or the
     * document element of private.xml. Beneath this point you can load and
     * store configuration fragments.
     *
     * @param shared if true, use project.xml, else private.xml
     * @return the data root
     */
    private Element getConfigurationDataRoot(final boolean shared) {
        return getConfigurationXml(shared).getDocumentElement();
    }

    /**
     * Retrieve project.xml or private.xml, loading from disk as needed.
     * private.xml is created as a skeleton on demand.
     *
     * @param shared true if using shared configuration; false otherwise
     * @return the XML document
     */
    private Document getConfigurationXml(final boolean shared) {
        if (shared && projectXml != null) {
            return projectXml;
        } else if (!shared && privateXml != null) {
            return privateXml;
        }
        Document doc = loadXml(shared);
        if (shared) {
            projectXml = doc != null ? doc
                    : XMLUtil.createDocument("config", ROOT_XML_NS, null, null);
        } else {
            privateXml = doc != null ? doc
                    : XMLUtil.createDocument("config", ROOT_XML_NS, null, null);
        }
        return shared ? projectXml : privateXml;
    }

    private Document loadXml(final boolean shared) {
        FileObject xml = getConfigurationFile(shared, false);
        if (xml == null || !xml.isData()) {
            return null;
        }
        try {
            // change first boolean to true to validate XML
            return XMLUtil.parse(new InputSource(xml.getInputStream()),
                    false, true, XMLUtil.defaultErrorHandler(), null);
        } catch (IOException | SAXException e) {
            Logger.getLogger(this.getClass().getName())
                    .log(Level.WARNING, null, e);
        }
        return null;
    }

    private void write(final boolean shared) {
        FileObject file = getConfigurationFile(shared, true);
        if (file == null) {
            Logger.getLogger(this.getClass().getName())
                    .log(Level.WARNING, "Unable to get file to write");
            return;
        }
        try (OutputStream out = file.getOutputStream()) {
            XMLUtil.write(shared ? projectXml : privateXml, out,
                    StandardCharsets.UTF_8.name());
            state.markModified();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

}
