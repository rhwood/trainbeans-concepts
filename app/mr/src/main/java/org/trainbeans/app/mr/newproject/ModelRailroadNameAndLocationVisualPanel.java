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

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import org.openide.awt.Mnemonics;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle;

@NbBundle.Messages({"VP_NAME=Name and Location",
    "TITLE_BROWSE_LOCATION=Select Project Location"})
public final class ModelRailroadNameAndLocationVisualPanel extends JPanel {

    private JTextField tfFolder;
    private JTextField tfLocation;
    private JTextField tfName;
    private JButton btnBrowse;

    public ModelRailroadNameAndLocationVisualPanel() {
        initComponents();
        Listener listener = new Listener();
        tfName.getDocument().addDocumentListener(listener);
        tfLocation.getDocument().addDocumentListener(listener);
        tfFolder.getDocument().addDocumentListener(listener);
    }

    @Override
    public String getName() {
        return Bundle.VP_NAME();
    }

    private void initComponents() {

        tfName = new JTextField();
        tfLocation = new JTextField();
        tfFolder = new JTextField();
        btnBrowse = new JButton();
        JLabel lblName = new JLabel();
        JLabel lblLocation = new JLabel();
        JLabel lblFolder = new JLabel();

        Mnemonics.setLocalizedText(lblName, NbBundle.getMessage(ModelRailroadNameAndLocationVisualPanel.class, "ModelRailroadNameAndLocationVisualPanel.lblName.text")); // NOI18N

        tfName.setText(NbBundle.getMessage(ModelRailroadNameAndLocationVisualPanel.class, "ModelRailroadNameAndLocationVisualPanel.tfName.text")); // NOI18N

        tfLocation.setText(NbBundle.getMessage(ModelRailroadNameAndLocationVisualPanel.class, "ModelRailroadNameAndLocationVisualPanel.tfLocation.text")); // NOI18N

        Mnemonics.setLocalizedText(lblLocation, NbBundle.getMessage(ModelRailroadNameAndLocationVisualPanel.class, "ModelRailroadNameAndLocationVisualPanel.lblLocation.text")); // NOI18N

        tfFolder.setText(NbBundle.getMessage(ModelRailroadNameAndLocationVisualPanel.class, "ModelRailroadNameAndLocationVisualPanel.tfFolder.text")); // NOI18N

        Mnemonics.setLocalizedText(lblFolder, NbBundle.getMessage(ModelRailroadNameAndLocationVisualPanel.class, "ModelRailroadNameAndLocationVisualPanel.lblFolder.text")); // NOI18N

        Mnemonics.setLocalizedText(btnBrowse, NbBundle.getMessage(ModelRailroadNameAndLocationVisualPanel.class, "ModelRailroadNameAndLocationVisualPanel.btnBrowse.text")); // NOI18N
        btnBrowse.addActionListener(this::btnBrowseActionPerformed);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lblFolder)
                                        .addComponent(lblLocation)
                                        .addComponent(lblName))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(tfName, GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                                        .addComponent(tfLocation)
                                        .addComponent(tfFolder))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBrowse)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblName)
                                        .addComponent(tfName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(tfLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblLocation)
                                        .addComponent(btnBrowse))
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblFolder)
                                        .addComponent(tfFolder, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    private void btnBrowseActionPerformed(ActionEvent evt) {
        if (evt.getSource() == btnBrowse) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(null);
            chooser.setDialogTitle(Bundle.TITLE_BROWSE_LOCATION());
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String path = tfLocation.getText();
            if (path.length() > 0) {
                File f = new File(path);
                if (f.exists()) {
                    chooser.setSelectedFile(f);
                }
            }
            if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(this)) {
                File projectDir = chooser.getSelectedFile();
                tfLocation.setText(FileUtil.normalizeFile(projectDir).getAbsolutePath());
            }
        }
    }

    void setProjectLocation(String text) {
        tfLocation.setText(text);
    }

    String getProjectLocation() {
        return tfLocation.getText();
    }

    void setProjectName(String text) {
        tfName.setText(text);
    }

    String getProjectName() {
        return tfName.getText();
    }

    void setProjectFolder(String text) {
        tfFolder.setText(text);
    }

    String getProjectFolder() {
        return tfFolder.getText();
    }

    private class Listener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateTextFields(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateTextFields(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateTextFields(e);
        }

        private void updateTextFields(DocumentEvent e) {
            Document doc = e.getDocument();
            if (tfName.getDocument() == doc || tfLocation.getDocument() == doc) {
                File location = new File(tfLocation.getText());
                if (location.isDirectory()) {
                    tfFolder.setText(tfLocation.getText() + File.separator + tfName.getText());
                } else {
                    tfFolder.setText(tfLocation.getText());
                }
            }

            if (tfName.getDocument() == doc) {
                firePropertyChange(ModelRailroadNameAndLocationPanel.PROP_PROJECT_NAME, null, tfName.getText());
            }
        }

    }
}
