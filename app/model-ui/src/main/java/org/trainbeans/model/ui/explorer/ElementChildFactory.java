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
public final class ElementChildFactory extends ChildFactory<Element> {

    /**
     * The model containing the elements that this factory can create.
     */
    private final Model model;
    /**
     * The class or element that this factory can create.
     */
    private final Class<? extends Element> elementClass;

    /**
     * Create the factory for the given model and class.
     *
     * @param bean the model containing the elements
     * @param clazz the class of element
     */
    public ElementChildFactory(final Model bean,
            final Class<? extends Element> clazz) {
        model = bean;
        elementClass = clazz;
    }

    @Override
    protected boolean createKeys(final List<Element> list) {
        list.addAll(model.getAll(elementClass));
        return true;
    }

    @Override
    protected Node createNodeForKey(final Element key) {
        ElementNode node = null;
        try {
            node = new ElementNode(key);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return node;
    }
}
