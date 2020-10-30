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
import java.io.FileNotFoundException;
import java.io.IOException;
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.EditableProperties;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.trainbeans.app.mr.ModelRailroadProject;
import static org.trainbeans.app.mr.impl.MRConstants.PROJECT_CONFIG_DIR;
import org.w3c.dom.Element;

/**
 *
 * @author rhwood
 */
public class ModelRailroadHelperImpl {

    private final ModelRailroadProject project;
    private EditableProperties properties;
    private AuxiliaryConfiguration configuration;

    public ModelRailroadHelperImpl(ModelRailroadProject aProject) {
        project = aProject;
    }

    public EditableProperties getProperties() {
        if (properties == null) {
            properties = new EditableProperties(true);
        }
        return properties;
    }

    public FileObject getPropertiesFile() throws FileNotFoundException {
        File file = new File(new File(
                FileUtil.toFile(project.getProjectDirectory()),
                PROJECT_CONFIG_DIR),
                "project.properties");
        try {
            return FileUtil.createData(file);
        } catch (IOException ex) {
            throw new FileNotFoundException(file.getPath());
        }
    }

    public void loadProperties() throws IOException {
        properties.load(getPropertiesFile().getInputStream());
    }

    public void storeProperties() throws IOException {
        properties.store(getPropertiesFile().getOutputStream());
    }

    public AuxiliaryConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = new MRAuxiliaryConfiguration(project, Lookup.getDefault().lookup(ProjectState.class));
        }
        return configuration;
    }

    public Element getConfigurationFragment(String elementName, String namespace, boolean shared) {
        return getConfiguration().getConfigurationFragment(elementName, namespace, shared);
    }

    public void putConfigurationFragment(Element fragment, boolean shared) {
        getConfiguration().putConfigurationFragment(fragment, shared);
    }

    public boolean removeConfigurationFragment(String elementName, String namespace, boolean shared) {
        return getConfiguration().removeConfigurationFragment(elementName, namespace, shared);
    }

}
