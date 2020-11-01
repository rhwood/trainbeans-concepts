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
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.trainbeans.app.mr.ModelRailroadProject;

/**
 *
 * @author rhwood
 */
@ServiceProvider(service = ProjectFactory.class)
public class ModelRailroadProjectFactory implements ProjectFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isProject(final FileObject fo) {
        return fo.getFileObject(MRConstants.PROJECT_XML_PATH) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Project loadProject(final FileObject fo, final ProjectState ps)
            throws IOException {
        return isProject(fo)
                ? new ModelRailroadProject(fo, Lookup.getDefault())
                : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProject(final Project prjct) throws IOException {
        // nothing to do
    }

}
