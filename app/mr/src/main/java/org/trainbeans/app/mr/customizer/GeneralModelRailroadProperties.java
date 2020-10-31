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

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.netbeans.spi.project.ui.support.ProjectCustomizer;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * Panel for general properties of a Model Railroad project.
 *
 * @author rhwood
 */
public final class GeneralModelRailroadProperties
        implements ProjectCustomizer.CompositeCategoryProvider {

    /**
     * The keyword for grouping these properties in the customizer.
     */
    private static final String GENERAL = "General";
    /**
     * The position of the properties in the customizer menu.
     */
    private static final int POSITION = 10;

    /**
     * Create the properties.
     *
     * @return a new instance of this class
     */
    @ProjectCustomizer.CompositeCategoryProvider.Registration(
            projectType = "org-trainbeans-app-mr", position = POSITION)
    public static GeneralModelRailroadProperties createGeneral() {
        return new GeneralModelRailroadProperties();
    }

    @NbBundle.Messages("LBL_Config_General=General")
    @Override
    public ProjectCustomizer.Category createCategory(final Lookup context) {
        return ProjectCustomizer.Category.create(GENERAL,
                Bundle.LBL_Config_General(),
                null);
    }

    @Override
    public JComponent createComponent(final ProjectCustomizer.Category category,
            final Lookup context) {
        return new JPanel();
    }

}
