/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.trainbeans.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import org.openide.util.Lookup;
import org.trainbeans.model.api.Element;
import org.trainbeans.model.api.Model;
import org.trainbeans.model.spi.ElementFactory;

/**
 *
 * @author rhwood
 */
public class DefaultModel implements Model {

    private final Map<Class<? extends Element>, ElementFactory<? extends Element>> factories = new HashMap<>();
    private final Map<Class<? extends Element>, SortedMap<String, ? extends Element>> elements = new HashMap<>();

    public DefaultModel(Lookup lookup) {
        lookup.lookupAll(ElementFactory.class).forEach(factory -> factories.put(factory.getElementClass(), factory));
    }
    
    @Override
    public <T extends Element> T create(Class<T> type, String name, Lookup lookup) {
        if (elements.get(type).containsKey(name)) {
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
        if (elements.containsKey(type)) {
            elements.get(type).values().stream().map(v -> (T) v).forEach(set::add);
        }
        return set;
    }

    @Override
    public <T extends Element> T get(Class<T> type, String name) {
        if (elements.containsKey(type)) {
            return (T) elements.get(type).get(name);
        }
        return null;
    }

    @Override
    public <T extends Element> T getOrCreate(Class<T> type, String name) {
        T element = get(type, name);
        return element != null ? element : create(type, name);
    }

    @Override
    public <T extends Element> void put(T element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T extends Element> void remove(T element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
