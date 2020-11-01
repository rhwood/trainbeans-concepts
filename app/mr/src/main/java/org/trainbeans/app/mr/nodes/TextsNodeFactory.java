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
package org.trainbeans.app.mr.nodes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.trainbeans.app.mr.ModelRailroadProject;
import static org.trainbeans.app.mr.nodes.TextsNodeFactory.POSITION;

/**
 * Dumb sample to understand how this all works.
 *
 * @author rhwood
 */
@NodeFactory.Registration(projectType = "org-trainbeans-app-mr",
        position = POSITION)
public class TextsNodeFactory implements NodeFactory {

    /**
     * Position in nodes list.
     */
    static final int POSITION = 10;

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeList<?> createNodes(final Project project) {
        ModelRailroadProject mrp = project.getLookup().
                lookup(ModelRailroadProject.class);
        assert mrp != null;
        return new TextsNodeList(mrp);
    }

    private class TextsNodeList implements NodeList<Node> {

        /**
         * The associated project.
         */
        private ModelRailroadProject project;
        /**
         * The supporting change support helper.
         */
        private final ChangeSupport cs = new ChangeSupport(this);

        TextsNodeList(final ModelRailroadProject mrp) {
            project = mrp;
        }

        @Override
        public List<Node> keys() {
            FileObject textsFolder = project.getProjectDirectory()
                    .getFileObject("texts");
            List<Node> result = new ArrayList<>();
            if (textsFolder != null) {
                for (FileObject textsFolderFile : textsFolder.getChildren()) {
                    try {
                        result.add(DataObject.find(textsFolderFile)
                                .getNodeDelegate());
                    } catch (DataObjectNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
            return result;
        }

        @Override
        public void addChangeListener(final ChangeListener l) {
            cs.addChangeListener(l);
        }

        @Override
        public void removeChangeListener(final ChangeListener l) {
            cs.removeChangeListener(l);
        }

        @Override
        public Node node(final Node key) {
            return new FilterNode(key);
        }

        @Override
        public void addNotify() {
            // nothing to do
        }

        @Override
        public void removeNotify() {
            // nothing to do
        }
    }

}
