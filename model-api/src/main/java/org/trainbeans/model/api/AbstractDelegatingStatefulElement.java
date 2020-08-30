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
package org.trainbeans.model.api;

import org.openide.util.Lookup;
import org.trainbeans.beans.VetoableBean;

/**
 *
 * @author rhwood
 * @param <E> type of element
 * @param <D> type of delegate
 */
public class AbstractDelegatingStatefulElement<E extends DelegatingElement & StatefulElement, D extends StatefulDelegate<E>> extends VetoableBean implements DelegatingElement, StatefulElement {

    int state = State.UNKNOWN;
    D delegate = null;
    private String name;
    
    protected AbstractDelegatingStatefulElement(Lookup lookup) {
        Delegate<?> aDelegate = lookup.lookup(Delegate.class);
    }

    @Override
    public D getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(Delegate delegate) {
        this.delegate = (D) delegate;
    }

    @Override
    public String getName() {
        if (name == null || name.trim().isEmpty()) {
            return delegate.getName();
        }
        return name;
    }

    @Override
    public void setName(String name) {
        if ((name == null || name.trim().isEmpty()) && delegate == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    @Override
    public int getState() {
        return delegate != null ? delegate.getState() : state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }
    
}
