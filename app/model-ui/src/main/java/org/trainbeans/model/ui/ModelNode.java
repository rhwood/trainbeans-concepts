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
import org.openide.util.NbBundle;
import org.trainbeans.model.api.Model;

/**
 *
 * @author rhwood
 */
class ModelNode extends BeanNode<Model> {

    @NbBundle.Messages("MSG_DESC=A model")
    public ModelNode(Model bean) throws IntrospectionException {
        super(bean, Children.create(new ElementClassChildFactory(bean), true));
        // TODO: determine how Models should be named and use that here
        setDisplayName("A model");
        setShortDescription(Bundle.MSG_DESC());
    }
    
}
