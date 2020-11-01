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

import java.util.HashMap;
import java.util.Map;
import org.openide.util.Lookup;

/**
 *
 * @author rhwood
 */
public final class ModelUIUtil {

    /**
     * Map of classes to descriptors.
     */
    private final Map<Class<?>, ClassDescriptor> descriptors = new HashMap<>();

    /**
     * Create an instance of the ModelUIUtil with the given Lookup.
     *
     * @param lookup the Lookup used to populate this class
     */
    public ModelUIUtil(final Lookup lookup) {
        lookup.lookupAll(ClassDescriptor.class)
                .forEach(ecd -> descriptors.put(ecd.getElementClass(), ecd));
    }

    /**
     * Get the localized name of the class.
     *
     * @param clazz the class to get the name for
     * @return the singular name
     */
    public String getSingularName(final Class<?> clazz) {
        return descriptors.get(clazz).getSingularName();
    }

    /**
     * Get the localized name of the class in plural.
     *
     * @param clazz the class to get the name for
     * @return the plural name
     */
    public String getPluralName(final Class<?> clazz) {
        return descriptors.get(clazz).getPluralName();
    }
}
