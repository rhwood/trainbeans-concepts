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
import org.netbeans.spi.project.ProjectState;
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
import org.trainbeans.app.mr.impl.MRAuxiliaryConfiguration;

/**
 *
 * @author rhwood
 */
public final class ModelRailroadProject implements Project {

    /**
     * The project directory.
     */
    private final FileObject projectDir;
    /**
     * The lookup for the project; generated as needed and cached here.
     */
    private Lookup projectLookup;
    /**
     * Lookup items passed in during construction.
     */
    private final Lookup additionalLookup;

    /**
     * Create a project.
     *
     * @param fo the path to the project
     * @param lookup a lookup containing additional items to add to the
     * project's lookup; use {@link Lookup#EMPTY} if not passing anything
     */
    public ModelRailroadProject(final FileObject fo, final Lookup lookup) {
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
            projectLookup = new ProxyLookup(Lookups.fixed(
                    this,
                    new Info(),
                    new ModelRailroadProjectLogicalView(this),
                    new ModelRailroadCustomizerProvider(this),
                    new MRAuxiliaryConfiguration(this,
                            Lookup.getDefault().lookup(ProjectState.class)),
                    new ModelRailroadHelper(this)),
                    additionalLookup);
        }
        return projectLookup;
    }

    /**
     * {@inheritDoc}
     *
     * @return true if the projects are in the same directory; false otherwise
     */
    @Override
    public boolean equals(final Object object) {
        if (object instanceof ModelRailroadProject) {
            return ((Project) object).getProjectDirectory()
                    .equals(this.getProjectDirectory());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return the hashCode of the project directory
     */
    @Override
    public int hashCode() {
        return getProjectDirectory().hashCode();
    }

    /**
     * Interface for services that need to be included in the Lookup for a
     * ModelRailroadProject. Implementing classes need to have a usable default
     * constructor and be listed in the results of
     * {@link Lookup#lookup(java.lang.Class)} for this interface in the Lookup
     * returned by {@link Lookup#getDefault()}.
     */
    public interface NeedsProject {

        /**
         * Set the project.
         *
         * @param project the project
         */
        void setProject(ModelRailroadProject project);
    }

    private final class Info implements ProjectInformation {

        /**
         * The path for the icon resource.
         */
        @StaticResource()
        public static final String MR_ICON = "org/trainbeans/app/mr/icon.png";
        /**
         * Property change support.
         */
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
        public void addPropertyChangeListener(final PropertyChangeListener pl) {
            propertyChangeSupport.addPropertyChangeListener(pl);
        }

        @Override
        public void removePropertyChangeListener(
                final PropertyChangeListener pl) {
            propertyChangeSupport.removePropertyChangeListener(pl);
        }

    }

    /**
     * The logical view for a ModelRailroadProject.
     */
    public final class ModelRailroadProjectLogicalView
            implements LogicalViewProvider {

        /**
         * The path for the icon resource.
         */
        @StaticResource()
        public static final String MR_ICON = "org/trainbeans/app/mr/icon.png";
        /**
         * The project this is the logical view of.
         */
        private final ModelRailroadProject project;

        /**
         * Create a logical view.
         *
         * @param aProject the project for this view
         */
        public ModelRailroadProjectLogicalView(
                final ModelRailroadProject aProject) {
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
        public Node findPath(final Node root, final Object target) {
            // not implemented for now
            return null;
        }

        private final class ProjectNode extends FilterNode {

            ProjectNode(final Node node) {
                super(node,
                        NodeFactorySupport.createCompositeChildren(project,
                                "Projects/org-trainbeans-app-mr/Nodes"),
                        new ProxyLookup(Lookups.singleton(project),
                                node.getLookup()));
            }

            @Override
            public Action[] getActions(final boolean arg0) {
                return new Action[]{
                    CommonProjectActions.newFileAction(),
                    CommonProjectActions.copyProjectAction(),
                    CommonProjectActions.deleteProjectAction(),
                    CommonProjectActions.customizeProjectAction(),
                    CommonProjectActions.closeProjectAction()
                };
            }

            @Override
            public Image getIcon(final int type) {
                return ImageUtilities.loadImage(MR_ICON);
            }

            @Override
            public Image getOpenedIcon(final int type) {
                return getIcon(type);
            }

            @Override
            public String getDisplayName() {
                return ProjectUtils.getInformation(project).getDisplayName();
            }
        }
    }
}
