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
import org.trainbeans.model.api.Element;
import org.trainbeans.model.api.Model;

/**
 *
 * @author rhwood
 */
public final class ElementClassChildFactory
        extends ChildFactory<Class<? extends Element>> {

    /**
     * The model containing classes this factory supports.
     */
    private final Model model;

    /**
     * Create the factory for the given model.
     *
     * @param bean the model
     */
    public ElementClassChildFactory(final Model bean) {
        model = bean;
    }

    @Override
    protected boolean createKeys(final List<Class<? extends Element>> list) {
        // sort getCreatableClasses() results before adding
        list.addAll(model.getCreatableClasses());
        return true;
    }

    @Override
    protected Node createNodeForKey(final Class<? extends Element> key) {
        ElementClassNode node = null;
        try {
            node = new ElementClassNode(model, key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }
}
