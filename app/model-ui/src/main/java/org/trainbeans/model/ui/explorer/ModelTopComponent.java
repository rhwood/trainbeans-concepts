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
package org.trainbeans.model.ui.explorer;

import java.awt.BorderLayout;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
        preferredID = "ModelTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.trainbeans.model.ui.ModelTopComponent")
@ActionReference(path = "Menu/Window", position = 333)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ModelAction",
        preferredID = "ModelTopComponent"
)
@Messages({
    "CTL_ModelAction=Layouts",
    "CTL_ModelTopComponent=Layouts",
    "HINT_ModelTopComponent=This is a Model window"
})
public final class ModelTopComponent extends TopComponent implements ExplorerManager.Provider {

    ExplorerManager modelExplorerManager = new ExplorerManager();

    public ModelTopComponent() {
        setName(Bundle.CTL_ModelTopComponent());
        setToolTipText(Bundle.HINT_ModelTopComponent());
        setLayout(new BorderLayout());
        BeanTreeView view = new BeanTreeView();
        view.setRootVisible(false);
        add(view, BorderLayout.CENTER);
        Children modelChildren = Children.create(new ModelChildFactory(), true);
        Node rootNode = new AbstractNode(modelChildren);
        modelExplorerManager.setRootContext(rootNode);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return modelExplorerManager;
    }
}
