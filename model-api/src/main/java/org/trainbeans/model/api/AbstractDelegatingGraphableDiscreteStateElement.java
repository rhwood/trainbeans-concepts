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

import java.util.ArrayList;
import java.util.Arrays;


public abstract class AbstractDelegatingGraphableDiscreteStateElement<E extends DelegatingElement & DiscreteStateElement, G extends Element, D extends DiscreteStateDelegate<E>> extends AbstractDelegatingDiscreteStateElement<E, D> implements Graphable<G> {

    ArrayList<G> connections = new ArrayList<>();

    @Override
    public G[] getConnections() {
        return connections.toArray((G[]) new Element[connections.size()]);
    }

    @Override
    public G getConnection(int index) {
        return connections.get(index);
    }

    @Override
    public void setConnection(int index, G newConnection) {
        connections.add(index, newConnection);
    }

    @Override
    public void setConnections(G[] connections) {
        this.connections.clear();
        this.connections.addAll(Arrays.asList(connections));
    }
    
}
