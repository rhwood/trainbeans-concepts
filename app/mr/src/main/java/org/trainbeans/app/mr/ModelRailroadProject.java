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

import java.awt.Image;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.trainbeans.app.mr.customizer.ModelRailroadCustomizerProvider;

/**
 *
 * @author rhwood
 */
public class ModelRailroadProject implements Project {

    private final FileObject projectDir;
    private Lookup projectLookup;
    private final Lookup additionalLookup;

    /**
     * Create a project.
     *
     * @param fo the path to the project
     * @param lookup a lookup containing additional items to add to the
     * project's lookup; use {@link Lookup#EMPTY} if not passing anything
     */
    public ModelRailroadProject(FileObject fo, Lookup lookup) {
        Objects.requireNonNull(fo, "Project directory must exist");
        Objects.requireNonNull(lookup, "Project must have a lookup");
        projectDir = fo;
        additionalLookup = lookup;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (projectLookup == null) {
            additionalLookup.lookupAll(NeedsProject.class)
                    .forEach(i -> i.setProject(this));
            projectLookup = new ProxyLookup(Lookups.fixed( // add Project features/requirements here
                    this,
                    new Info(),
                    new ModelRailroadProjectLogicalView(this),
                    new ModelRailroadCustomizerProvider(this)),
                    additionalLookup);
        }
        return projectLookup;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ModelRailroadProject) {
            return ((Project) object).getProjectDirectory().equals(this.getProjectDirectory());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getProjectDirectory().hashCode();
    }

    public interface NeedsProject {

        public void setProject(ModelRailroadProject project);
    }

    private final class Info implements ProjectInformation {

        @StaticResource()
        public static final String MR_ICON = "org/trainbeans/app/mr/icon.png";
        private final PropertyChangeSupport propertyChangeSupport
                = new PropertyChangeSupport(this);

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            // should be something provided by user
            return getName();
        }

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(MR_ICON));
        }

        @Override
        public Project getProject() {
            return ModelRailroadProject.this;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pl) {
            propertyChangeSupport.addPropertyChangeListener(pl);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pl) {
            propertyChangeSupport.removePropertyChangeListener(pl);
        }

    }

    public class ModelRailroadProjectLogicalView implements LogicalViewProvider {

        @StaticResource()
        public static final String MR_ICON = "org/trainbeans/app/mr/icon.png";
        private final ModelRailroadProject project;

        public ModelRailroadProjectLogicalView(ModelRailroadProject aProject) {
            project = aProject;
        }

        @Override
        public Node createLogicalView() {
            FileObject projectDirectory = project.getProjectDirectory();
            DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
            Node projectFolderNode = projectFolder.getNodeDelegate();
            return new ProjectNode(projectFolderNode);
        }

        @Override
        public Node findPath(Node root, Object target) {
            // not implemented for now
            return null;
        }

        private final class ProjectNode extends FilterNode {

            public ProjectNode(Node node) {
                super(node,
                        NodeFactorySupport.createCompositeChildren(project,
                                "Projects/org-trainbeans-app-mr/Nodes"),
                        new ProxyLookup(Lookups.singleton(project),
                                node.getLookup()));
            }

            @Override
            public Action[] getActions(boolean arg0) {
                return new Action[]{
                    CommonProjectActions.newFileAction(),
                    CommonProjectActions.copyProjectAction(),
                    CommonProjectActions.deleteProjectAction(),
                    CommonProjectActions.customizeProjectAction(),
                    CommonProjectActions.closeProjectAction()
                };
            }

            @Override
            public Image getIcon(int type) {
                return ImageUtilities.loadImage(MR_ICON);
            }

            @Override
            public Image getOpenedIcon(int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return ProjectUtils.getInformation(project).getDisplayName();
            }
        }
    }
}
