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

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.ui.CustomizerProvider;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.awt.StatusDisplayer;
import org.openide.util.lookup.Lookups;
import org.trainbeans.app.mr.ModelRailroadProject;

/**
 *
 * @author rhwood
 */
public class ModelRailroadCustomizerProvider implements CustomizerProvider {

    /**
     * The logical path for this customizer in the NetBeans path structure.
     */
    public static final String CUSTOMIZER_FOLDER_PATH
            = "Projects/org-trainbeans-app-mr/Customizer";
    /**
     * The project this customizer is for.
     */
    private final ModelRailroadProject project;
    /**
     * The dialog containing the customizer.
     */
    private Dialog dialog;

    /**
     * Create a customizer provider.
     *
     * @param aProject the customizable project
     */
    public ModelRailroadCustomizerProvider(
            final ModelRailroadProject aProject) {
        project = aProject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showCustomizer() {
        dialog = ProjectCustomizer.createCustomizerDialog(
                // path to layer folder
                CUSTOMIZER_FOLDER_PATH,
                // Lookup that contains, at a minimum, the project
                Lookups.fixed(project),
                // the preselected category
                "",
                // OK button listener
                new OKOptionListener(),
                // Save button listener
                null,
                // Help context
                null);
        dialog.setTitle(ProjectUtils.getInformation(project).getDisplayName());
        dialog.setVisible(true);
    }

    /**
     * Get the dialog.
     *
     * @return the last dialog shown or null if {@link #showCustomizer()} has
     * not been called
     */
    // package protected for testing
    Dialog getDialog() {
        return dialog;
    }

    private class OKOptionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            StatusDisplayer.getDefault().setStatusText("OK button clicked for "
                    + project.getProjectDirectory().getName() + " customizer!");
            // release dialog for eventual garbage collection
            dialog = null;
        }
    }

}
