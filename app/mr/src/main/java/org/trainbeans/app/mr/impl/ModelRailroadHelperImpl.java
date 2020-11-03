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
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.EditableProperties;
import org.openide.util.Lookup;
import org.trainbeans.app.mr.ModelRailroadProject;
import static org.trainbeans.app.mr.MRConstants.PROJECT_PROPERTIES;
import org.w3c.dom.Element;

/**
 *
 * @author rhwood
 */
public class ModelRailroadHelperImpl {

    /**
     * The associated project.
     */
    private final ModelRailroadProject project;
    /**
     * The properties.
     */
    private EditableProperties properties;
    /**
     * The configuration XML.
     */
    private AuxiliaryConfiguration configuration;

    /**
     * Create a helper.
     *
     * @param aProject the project this is the helper for
     */
    public ModelRailroadHelperImpl(final ModelRailroadProject aProject) {
        project = aProject;
    }

    /**
     * Get the properties associated with the project.
     *
     * @return the properties
     */
    public EditableProperties getProperties() {
        if (properties == null) {
            properties = new EditableProperties(true);
        }
        return properties;
    }

    /**
     * Get the properties file.
     *
     * @return the file
     * @throws IOException if the properties file could not be read or created
     */
    public FileObject getPropertiesFile() throws IOException {
        return FileUtil.createData(project.getProjectDirectory(),
                PROJECT_PROPERTIES);
    }

    /**
     * Retrieve properties associated with the project.
     *
     * @throws IOException if unable to read the properties
     */
    public void loadProperties() throws IOException {
        try (InputStream stream = getPropertiesFile().getInputStream()) {
            getProperties().load(stream);
        }
    }

    /**
     * Save (store) any properties associated with the project.
     *
     * @throws IOException if unable to write the properties
     */
    public void storeProperties() throws IOException {
        try (OutputStream stream = getPropertiesFile().getOutputStream()) {
            getProperties().store(stream);
        }
    }

    /**
     * Get the configuration XML handler.
     *
     * @return the handler
     */
    public AuxiliaryConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = new MRAuxiliaryConfiguration(project,
                    Lookup.getDefault().lookup(ProjectState.class));
        }
        return configuration;
    }

    /**
     * Get a configuration XML fragment.
     *
     * @param elementName the name of the fragment
     * @param namespace the namespace for the fragment
     * @param shared true if in the shared XML; false otherwise
     * @return the fragment or null if no matching fragment exists
     */
    public Element getConfigurationFragment(final String elementName,
            final String namespace,
            final boolean shared) {
        return getConfiguration().getConfigurationFragment(elementName,
                namespace,
                shared);
    }

    /**
     * Put a configuration XML fragment.
     *
     * @param fragment the fragment; it must have a namespace
     * @param shared true if in the shared XML; false otherwise
     */
    public void putConfigurationFragment(final Element fragment,
            final boolean shared) {
        getConfiguration().putConfigurationFragment(fragment, shared);
    }

    /**
     * Delete a configuration XML fragment.
     *
     * @param elementName the name of the fragment
     * @param namespace the namespace for the fragment
     * @param shared true if in the shared XML; false otherwise
     * @return true if deleted; false otherwise
     */
    public boolean removeConfigurationFragment(final String elementName,
            final String namespace,
            final boolean shared) {
        return getConfiguration().removeConfigurationFragment(elementName,
                namespace,
                shared);
    }

}
