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

/**
 * Mark an element as capable of being included in a graph of like elements.
 * 
 * @author rhwood
 * @param <T> the type of Element supported in the graph
 */
public interface Graphable<T extends Element> {

    public T[] getConnections();

    public T getConnection(int index);

    public void setConnection(int index, T newConnection);

    public void setConnections(T[] connections);
}
