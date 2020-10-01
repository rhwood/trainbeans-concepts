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

import java.beans.IntrospectionException;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.trainbeans.model.api.Model;
import org.trainbeans.model.api.Turnout;
import org.trainbeans.model.impl.DefaultModel;

/**
 *
 * @author rhwood
 */
public class ModelChildFactory extends ChildFactory<Model> {

    public ModelChildFactory() {
        // nothing to do
    }

    @Override
    protected boolean createKeys(List<Model> list) {
        // TODO: dynamically get list of Models
        //       for now, generate a new Model
        Model model = new DefaultModel(Lookup.getDefault());
        model.create(Turnout.class, "North Boylan Siding");
        model.create(Turnout.class, "South Boylan Siding");
        list.add(model);
        return true;
    }

    @Override
    protected Node createNodeForKey(Model key) {
        ModelNode node = null;
        try {
            node = new ModelNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }
}
