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

import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.annotations.common.NullAllowed;
import org.openide.util.Lookup;
import org.trainbeans.model.api.Element;

/**
 * A factory for creating an {@link Element} in a
 * {@link org.trainbeans.model.api.Model}.
 *
 * @author rhwood
 */
public interface ElementFactory<T extends Element> {

    @NonNull
    public Class<T> getElementClass();

    /**
     * Create an element with the given name. If this Factory supports a
     * {@link DelegatingElement} and the lookup contains a {@link Delegate}, the
     * element will use that Delegate.
     *
     * @param name the name of the element
     * @param lookup a container of additional services
     * @return the element
     * @throws IllegalArgumentException if name is blank and the lookup does not
     * contain a Delegate
     */
    @NonNull
    public T create(@NonNull String name, @NullAllowed Lookup lookup);
}
