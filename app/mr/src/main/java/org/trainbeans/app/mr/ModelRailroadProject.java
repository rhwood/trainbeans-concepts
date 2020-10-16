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
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.NodeFactorySupport;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
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
    private Lookup lookup;

    public ModelRailroadProject(FileObject fo) {
        Objects.requireNonNull(fo, "Project directory must exist");
        projectDir = fo;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            lookup = Lookups.fixed( // add Project features/requirements here
                    this,
                    new Info(),
                    new ModelRailroadProjectLogicalView(this),
                    new ModelRailroadCustomizerProvider(this));
        }
        return lookup;
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
            try {
                FileObject projectDirectory = project.getProjectDirectory();
                DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
                Node projectFolderNode = projectFolder.getNodeDelegate();
                return new ProjectNode(projectFolderNode, project);
            } catch (DataObjectNotFoundException ex) {
                // the directory could not be created; likely read-only filesystem
                Exceptions.printStackTrace(ex);
                return new AbstractNode(Children.LEAF);
            }
        }

        @Override
        public Node findPath(Node root, Object target) {
            // not implemented for now
            return null;
        }

        private final class ProjectNode extends FilterNode {

            final ModelRailroadProject project;

            public ProjectNode(Node node, ModelRailroadProject aProject)
                    throws DataObjectNotFoundException {
                super(node,
                        NodeFactorySupport.createCompositeChildren(aProject,
                                "Projects/org-trainbeans-app-mr/Nodes"),
                        new ProxyLookup(Lookups.singleton(aProject),
                                node.getLookup()));
                project = aProject;
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
                return project.getProjectDirectory().getName();
            }
        }
    }
}
