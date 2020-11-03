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
package org.trainbeans.app.mr.ui.newproject;

import java.io.File;
import javax.swing.event.ChangeListener;
import org.apiguardian.api.API;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

@API(status = API.Status.INTERNAL)
public class ModelRailroadNameAndLocationPanel
        implements WizardDescriptor.Panel<WizardDescriptor> {

    /**
     * Name of property for project name.
     */
    public static final String PROP_PROJECT_NAME = "projectName"; // NOI18N
    /**
     * Name of property for project folder.
     */
    public static final String PROP_PROJECT_FOLDER = "projectFolder"; // NOI18N
    /**
     * The visual component that displays this panel.
     */
    private ModelRailroadNameAndLocationVisualPanel component;
    /**
     * Change support helper.
     */
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    /**
     * {@inheritDoc}
     */
    @Override
    public ModelRailroadNameAndLocationVisualPanel getComponent() {
        if (component == null) {
            component = new ModelRailroadNameAndLocationVisualPanel();
        }
        return component;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return component != null
                && !(new File(component.getProjectFolder()).exists());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addChangeListener(final ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeChangeListener(final ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readSettings(final WizardDescriptor wiz) {
        File location = (File) wiz
                .getProperty(CommonProjectActions.PROJECT_PARENT_FOLDER);
        if (location == null
                || location.getParentFile() == null
                || !location.getParentFile().isDirectory()) {
            location = ProjectChooser.getProjectsFolder();
        }
        component.setProjectLocation(location.getAbsolutePath());
        String name = (String) wiz.getProperty(PROP_PROJECT_NAME);
        component.setProjectName(name != null
                ? name
                : firstAvailableName(location, "modelRailroad"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeSettings(final WizardDescriptor wiz) {
        wiz.putProperty(PROP_PROJECT_NAME, component.getProjectName());
        wiz.putProperty(PROP_PROJECT_FOLDER, component.getProjectFolder());
    }

    private String firstAvailableName(final File location, final String base) {
        int index = 1;
        String name;
        File folder;
        do {
            name = base + Integer.toString(index);
            folder = new File(location, name);
            index++;
        } while (folder.exists());
        return name;
    }
}
