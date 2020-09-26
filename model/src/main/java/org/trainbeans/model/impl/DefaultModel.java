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
 * The default implementation of a Model.
 *
 * @author rhwood
 */
public final class DefaultModel extends Bean implements Model,
        PropertyChangeListener, VetoableChangeListener {

    /**
     * Map of factories to be used to create elements, keyed by the Class the
     * factory can create.
     */
    @SuppressWarnings("checkstyle:linelength") // generic defintion on one line
    private final Map<Class<? extends Element>, ElementFactory<? extends Element>> factories = new HashMap<>();
    /**
     * Map of elements, keyed by name.
     */
    // TODO should I use a BidiSortedMap from Apache Commons Collections here?
    private final SortedMap<String, Element> elements = new TreeMap<>();
    /**
     * Cache of sets of all elements by requested interface. All caches are
     * invalidated by adding or removing an element.
     */
    @SuppressWarnings("checkstyle:linelength") // generic defintion on one line
    private final Map<Class<? extends Element>, Set<? extends Element>> cache = new HashMap<>();

    /**
     * Create a model.
     *
     * @param lookup the container with the
     * {@link ElementFactory ElementFactories} to use to create {@link Element}s
     */
    public DefaultModel(final Lookup lookup) {
        lookup.lookupAll(ElementFactory.class)
                .forEach(factory
                        -> factories.put(factory.getElementClass(), factory));
    }

    @Override
    public <T extends Element> T create(final Class<T> type,
            final String name,
            final Lookup lookup) {
        if (elements.get(name) != null) {
            throw new IllegalStateException();
        }
        if (factories.get(type) == null) {
            throw new IllegalArgumentException();
        }
        T element = (T) factories.get(type).create(name, lookup);
        put(element);
        return element;
    }

    @Override
    public <T extends Element> Set<T> getAll(final Class<T> type) {
        if (cache.containsKey(type)) {
            return (Set<T>) cache.get(type);
        } else {
            Set<T> set = new HashSet<>();
            elements.values().stream()
                    .filter(type::isInstance)
                    .forEach(e -> set.add((T) e));
            cache.put(type, set);
            return set;
        }
    }

    @Override
    public <T extends Element> T get(final Class<T> type, final String name) {
        Element element = elements.get(name);
        if (type.isInstance(element)) {
            return (T) element;
        }
        return null;
    }

    @Override
    public <T extends Element> T getOrCreate(final Class<T> type,
            final String name,
            final Lookup lookup) {
        T element = get(type, name);
        return element != null ? element : create(type, name, lookup);
    }

    @Override
    public void put(final Element element) {
        if (elements.containsKey(element.getName())) {
            throw new IllegalStateException();
        }
        element.addVetoableChangeListener("name", this);
        element.addPropertyChangeListener("name", this);
        elements.put(element.getName(), element);
        cache.clear();
    }

    @Override
    public void remove(final Element element) {
        elements.remove(element.getName());
        element.removeVetoableChangeListener("name", this);
        element.removePropertyChangeListener("name", this);
        cache.clear();
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("name")) {
            Element element = elements.get(evt.getOldValue().toString());
            elements.remove(evt.getOldValue().toString());
            elements.put(element.getName(), element);
        }
    }

    @Override
    public DefaultModel getSelf() {
        return this;
    }

    // package protected for tests
    <T extends Element> Set<T> getCache(final Class<T> type) {
        return (Set<T>) cache.get(type);
    }
}
