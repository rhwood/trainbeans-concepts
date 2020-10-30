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
package org.trainbeans.app.mr.newproject;

import java.io.File;
import javax.swing.event.ChangeListener;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

public class ModelRailroadNameAndLocationPanel implements WizardDescriptor.Panel<WizardDescriptor> {

    public static final String PROP_PROJECT_NAME = "projectName"; // NOI18N
    public static final String PROP_PROJECT_FOLDER = "projectFolder"; // NOI18N
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private ModelRailroadNameAndLocationVisualPanel component;
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public ModelRailroadNameAndLocationVisualPanel getComponent() {
        if (component == null) {
            component = new ModelRailroadNameAndLocationVisualPanel();
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public boolean isValid() {
        return (component != null && !(new File(component.getProjectFolder()).exists()));
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        File location = (File) wiz.getProperty(CommonProjectActions.PROJECT_PARENT_FOLDER);
        if (location == null || location.getParentFile() == null || !location.getParentFile().isDirectory()) {
            location = ProjectChooser.getProjectsFolder();
        }
        component.setProjectLocation(location.getAbsolutePath());
        String name = (String) wiz.getProperty(PROP_PROJECT_NAME);
        component.setProjectName(name != null ? name : firstAvailableName(location, "modelRailroad"));
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        wiz.putProperty(PROP_PROJECT_NAME, component.getProjectName());
        wiz.putProperty(PROP_PROJECT_FOLDER, component.getProjectFolder());
    }

    private String firstAvailableName(File location, String baseName) {
        int index = 1;
        String name;
        File folder;
        do {
            name = baseName + Integer.toString(index);
            folder = new File(location, name);
            index++;
        } while (folder.exists());
        return name;
    }
}
