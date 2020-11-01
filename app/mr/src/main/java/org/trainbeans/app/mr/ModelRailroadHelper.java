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

import java.io.IOException;
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.openide.util.EditableProperties;
import org.trainbeans.app.mr.impl.ModelRailroadHelperImpl;
import org.w3c.dom.Element;

/**
 *
 * @author rhwood
 */
public final class ModelRailroadHelper implements AuxiliaryConfiguration {

    /**
     * Delegate for the helper.
     */
    private final ModelRailroadHelperImpl delegate;

    /**
     * Create a helper.
     *
     * @param project the associated project
     */
    public ModelRailroadHelper(final ModelRailroadProject project) {
        delegate = new ModelRailroadHelperImpl(project);
    }

    /**
     * Get the properties associated with the project.
     *
     * @return the properties
     */
    public EditableProperties getProperties() {
        return delegate.getProperties();
    }

    /**
     * Retrieve properties associated with the project.
     *
     * @throws IOException if unable to read the properties
     */
    public void loadProperties() throws IOException {
        delegate.loadProperties();
    }

    /**
     * Save (store) any properties associated with the project.
     *
     * @throws IOException if unable to write the properties
     */
    public void storeProperties() throws IOException {
        delegate.storeProperties();
    }

    @Override
    public Element getConfigurationFragment(final String elementName,
            final String namespace,
            final boolean shared) {
        return delegate.getConfigurationFragment(elementName,
                namespace,
                shared);
    }

    @Override
    public void putConfigurationFragment(final Element fragment,
            final boolean shared) {
        delegate.putConfigurationFragment(fragment, shared);
    }

    @Override
    public boolean removeConfigurationFragment(final String elementName,
            final String namespace,
            final boolean shared) {
        return delegate.removeConfigurationFragment(elementName,
                namespace,
                shared);
    }
}
