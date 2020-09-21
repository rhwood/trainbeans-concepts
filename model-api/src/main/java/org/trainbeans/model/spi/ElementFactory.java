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
package org.trainbeans.model.spi;

import org.openide.util.Lookup;
import org.trainbeans.model.api.Element;

/**
 * A factory for creating an {@link Element} in a
 * {@link org.trainbeans.model.api.Model}.
 *
 * @author rhwood
 * @param <T> the Element type this ElementFactory supports
 */
public interface ElementFactory<T extends Element> {

    /**
     * Get the most specific class of object created by the
     * {@link #create(String, Lookup)} method of this factory.
     *
     * @return the class of object created by this factory
     */
    Class<T> getElementClass();

    /**
     * Create an element with the given name. If this Factory supports a
     * {@link org.trainbeans.model.api.DelegatingElement} and the lookup
     * contains a {@link org.trainbeans.model.api.Delegate}, the element will
     * use that Delegate.
     *
     * @param name the name of the element
     * @param lookup a container of additional services
     * @return the element
     * @throws IllegalArgumentException if name is blank and the lookup does not
     * contain a Delegate
     */
    T create(String name, Lookup lookup);
}
