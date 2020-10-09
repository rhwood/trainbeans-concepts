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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.openide.filesystems.FileObject;
import org.openide.xml.XMLUtil;
import org.trainbeans.app.mr.ModelRailroadProject;
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

    public static final String NAMESPACE = "http://www.netbeans.org/ns/auxiliary-configuration/1"; // NOI18N
    private final ModelRailroadProject project;

    public MRAuxiliaryConfiguration(ModelRailroadProject aProject) {
        project = aProject;
    }

    private FileObject getConfigurationFile(boolean shared) {
        // non-shared location should be in different directory
        return project.getProjectDirectory().getFileObject(shared
                ? "trainbeans/project.xml"
                : "trainbeans/private.xml");
    }

    @Override
    public Element getConfigurationFragment(String elementName, String namespace, boolean shared) {
        // Should use NetBeans Mutex to lock file
        FileObject file = this.getConfigurationFile(shared);
        if (file != null && file.canRead()) {
            try {
                try (final InputStream is = file.getInputStream()) {
                    InputSource input = new InputSource(is);
                    input.setSystemId(file.toURI().toURL().toString());
                    Element root = XMLUtil.parse(input, false, true, null, null).getDocumentElement();
                    return XMLUtil.findElement(root, elementName, namespace);
                }
            } catch (IOException | SAXException | IllegalArgumentException ex) {
                // log parsing warning
            }
        }
        return null;
    }

    @Override
    public void putConfigurationFragment(Element fragment, boolean shared) throws IllegalArgumentException {
        // Should use NetBeans Mutex to lock file
        String elementName = fragment.getLocalName();
        String namespace = fragment.getNamespaceURI();
        if (namespace == null) {
            throw new IllegalArgumentException();
        }
        FileObject file = this.getConfigurationFile(shared);
        Document doc = null;
        if (file != null && file.canRead()) {
            try {
                try (final InputStream is = file.getInputStream()) {
                    InputSource input = new InputSource(is);
                    input.setSystemId(file.toURI().toURL().toString());
                    doc = XMLUtil.parse(input, false, true, null, null);
                }
            } catch (IOException | SAXException ex) {
                // log parsing warning
            }
        }
        if (doc == null) {
            doc = XMLUtil.createDocument("auxiliary-configuration", NAMESPACE, null, null); // NOI18N
        }
        Element root = doc.getDocumentElement();
        Element oldFragment = XMLUtil.findElement(root, elementName, namespace);
        if (oldFragment != null) {
            root.removeChild(oldFragment);
        }
        Node ref = null;
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                int comparison = node.getNodeName().compareTo(elementName);
                if (comparison == 0) {
                    comparison = node.getNamespaceURI().compareTo(namespace);
                }
                if (comparison > 0) {
                    ref = node;
                    break;
                }
            }
        }
        root.insertBefore(root.getOwnerDocument().importNode(fragment, true), ref);
        if (file != null && file.canWrite()) {
            try {
                // this.backup(shared); // should we backup the configuration?
                try (final OutputStream os = file.getOutputStream()) {
                    XMLUtil.write(doc, os, StandardCharsets.UTF_8.name());
                }
            } catch (IOException ex) {
                // log write warning
            }
        }
    }

    @Override
    public boolean removeConfigurationFragment(String elementName, String namespace, boolean shared) throws IllegalArgumentException {
        // Should use NetBeans Mutex to lock file
        FileObject file = this.getConfigurationFile(shared);
        if (file.canWrite()) {
            try {
                Document doc;
                try (final InputStream is = file.getInputStream()) {
                    InputSource input = new InputSource(is);
                    input.setSystemId(file.toURI().toURL().toString());
                    doc = XMLUtil.parse(input, false, true, null, null);
                }
                Element root = doc.getDocumentElement();
                Element toRemove = XMLUtil.findElement(root, elementName, namespace);
                if (toRemove != null) {
                    root.removeChild(toRemove);
                    // this.backup(shared); // should we backup the configuration?
                    if (root.getElementsByTagName("*").getLength() > 0) {
                        // NOI18N
                        try (final OutputStream os = file.getOutputStream()) {
                            XMLUtil.write(doc, os, StandardCharsets.UTF_8.name());
                        }
                    }
                    return true;
                }
            } catch (IOException | SAXException | DOMException ex) {
                // log removal error
            }
        }
        return false;
    }

}
