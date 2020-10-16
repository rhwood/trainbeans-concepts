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
package org.trainbeans.app.mr.customizer;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ProjectManagerImplementation;
import org.openide.filesystems.FileObject;
import org.openide.util.Mutex;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author rhwood
 */
public class TestUtil {

    @ServiceProvider(service = ProjectManagerImplementation.class)
    public static final class MockProjectManager implements ProjectManagerImplementation {

        private final Mutex MUTEX = new Mutex();

        @Override
        public void init(ProjectManagerCallBack callBack) {
        }

        @Override
        public Mutex getMutex() {
            return MUTEX;
        }

        @Override
        public Mutex getMutex(boolean autoSave, Project project, Project... otherProjects) {
            return MUTEX;
        }

        @Override
        public Project findProject(FileObject projectDirectory) throws IOException, IllegalArgumentException {
            return null;
        }

        @Override
        public ProjectManager.Result isProject(FileObject projectDirectory) throws IllegalArgumentException {
            return null;
        }

        @Override
        public void clearNonProjectCache() {
        }

        @Override
        public Set<Project> getModifiedProjects() {
            return Collections.emptySet();
        }

        @Override
        public boolean isModified(Project p) {
            return false;
        }

        @Override
        public boolean isValid(Project p) {
            return true;
        }

        @Override
        public void saveProject(Project p) throws IOException {
        }

        @Override
        public void saveAllProjects() throws IOException {
        }
    }
}
