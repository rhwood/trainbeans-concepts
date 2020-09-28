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
package org.trainbeans.model.ui;

import java.beans.IntrospectionException;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.trainbeans.model.api.Element;
import org.trainbeans.model.api.Model;

/**
 *
 * @author rhwood
 */
public class ElementClassNode extends BeanNode<Class<? extends Element>> {

    ElementClassNode(Model model, Class<? extends Element> elementClass) throws IntrospectionException {
        super(elementClass, Children.create(new ElementChildFactory(model, elementClass), true));
        // TODO: create mechanism for getting "nice" names for elementClasses
        setName(elementClass.getSimpleName());
    }
    
}
