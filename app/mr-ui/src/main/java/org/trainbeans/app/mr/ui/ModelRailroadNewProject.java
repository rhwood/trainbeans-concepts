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
package org.trainbeans.app.mr.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.templates.TemplateRegistration;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileUtil;
import org.openide.util.ChangeSupport;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.trainbeans.app.mr.ModelRailroadHelper;
import org.trainbeans.app.mr.ModelRailroadProject;
import static org.trainbeans.app.mr.ui.ModelRailroadNewProject.POSITION;
import org.trainbeans.app.mr.ui.newproject.ModelRailroadNameAndLocationPanel;
import static org.trainbeans.app.mr.ui.newproject.ModelRailroadNameAndLocationPanel.PROP_PROJECT_FOLDER;
import static org.trainbeans.app.mr.ui.newproject.ModelRailroadNameAndLocationPanel.PROP_PROJECT_NAME;

@NbBundle.Messages("New_MR_Title=New Model Railroad")
@TemplateRegistration(folder = "Project/Standard",
        displayName = "Model Railroad",
        description = "newproject/description.html",
        iconBase = "org/trainbeans/app/mr/icon.png",
        position = POSITION,
        content = "newproject/trainbeans/project.xml"
)
public final class ModelRailroadNewProject
        implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    /**
     * The position of this project in the new project selector.
     */
    static final int POSITION = 10;
    /**
     * Change support helper for this class.
     */
    private final ChangeSupport changeSupport = new ChangeSupport(this);
    /**
     * Index of the current wizard panel.
     */
    private int index;
    /**
     * The wizard descriptor used when creating a new project.
     */
    private WizardDescriptor descriptor;
    /**
     * The set of wizard panels.
     */
    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;

    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        if (panels == null) {
            panels = new ArrayList<>();
            panels.add(new ModelRailroadNameAndLocationPanel());
            String[] steps = new String[panels.size()];
            for (int i = 0; i < panels.size(); i++) {
                // assume all components are JComponents
                JComponent jc = (JComponent) panels.get(i).getComponent();
                steps[i] = jc.getName();
                jc.putClientProperty(
                        WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, 0);
                jc.putClientProperty(
                        WizardDescriptor.PROP_CONTENT_DATA, steps);
                jc.putClientProperty(
                        WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                jc.putClientProperty(
                        WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                jc.putClientProperty(
                        WizardDescriptor.PROP_CONTENT_NUMBERED, true);
            }
        }
        return panels;
    }

    @Override
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return getPanels().get(index);
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().size();
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    @Override
    public void addChangeListener(final ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(final ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    @Override
    public Set<?> instantiate() throws IOException {
        File dir = new File(
                (String) descriptor.getProperty(PROP_PROJECT_FOLDER));
        dir.mkdirs();
        ModelRailroadProject project = new ModelRailroadProject(
                FileUtil.toFileObject(dir), Lookup.EMPTY);
        ModelRailroadHelper helper = new ModelRailroadHelper(project);
        helper.getProperties().setProperty("project.name",
                (String) descriptor.getProperty(PROP_PROJECT_NAME));
        helper.storeProperties();
        return new HashSet<>();
    }

    @Override
    public void initialize(final WizardDescriptor wizard) {
        index = 0;
        descriptor = wizard;
        wizard.putProperty("NewProjectWizard_Title", // NOI18N
                Bundle.New_MR_Title());
        getPanels();
    }

    @Override
    public void uninitialize(final WizardDescriptor wizard) {
        panels = null;
    }

}
