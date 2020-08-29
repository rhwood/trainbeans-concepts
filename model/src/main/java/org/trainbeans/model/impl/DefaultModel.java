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
package org.trainbeans.model.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.openide.util.Lookup;
import org.trainbeans.beans.Bean;
import org.trainbeans.model.api.Element;
import org.trainbeans.model.api.Model;
import org.trainbeans.model.spi.ElementFactory;

/**
 *
 * @author rhwood
 */
public class DefaultModel extends Bean implements Model, PropertyChangeListener, VetoableChangeListener {

    private final Map<Class<? extends Element>, ElementFactory<? extends Element>> factories = new HashMap<>();
    // TODO should I use a BidiSortedMap from Apache Commons Collections here?
    private final SortedMap<String, Element> elements = new TreeMap<>();

    public DefaultModel(Lookup lookup) {
        lookup.lookupAll(ElementFactory.class).forEach(factory -> factories.put(factory.getElementClass(), factory));
    }
    
    @Override
    public <T extends Element> T create(Class<T> type, String name, Lookup lookup) {
        if (elements.get(name) != null) {
            throw new IllegalArgumentException();
        }
        if (factories.get(type) == null) {
            throw new IllegalArgumentException();
        }
        T element = (T) factories.get(type).create(name, lookup);
        put(element);
        return element;
    }

    @Override
    public <T extends Element> Set<T> getAll(Class<T> type) {
        Set<T> set = new HashSet<>();
        // TODO should this be cached?
        elements.values().stream().filter(type::isInstance).map(v -> (T) v).forEach(set::add);
        return set;
    }

    @Override
    public <T extends Element> T get(Class<T> type, String name) {
        Element element = elements.get(name);
        if (type.isInstance(element)) {
            return (T) element;
        }
        return null;
    }

    @Override
    public <T extends Element> T getOrCreate(Class<T> type, String name) {
        T element = get(type, name);
        return element != null ? element : create(type, name);
    }

    @Override
    public void put(Element element) {
        if (elements.containsKey(element.getName())) {
            throw new IllegalArgumentException();
        }
        element.addVetoableChangeListener("name", this);
        elements.put(element.getName(), element);
    }

    @Override
    public void remove(Element element) {
        elements.remove(element.getName());
        element.removeVetoableChangeListener("name", this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("name")) {
            Element element = elements.get(evt.getOldValue().toString());
            elements.remove(evt.getOldValue().toString());
            elements.put(element.getName(), element);
        }
    }
}
