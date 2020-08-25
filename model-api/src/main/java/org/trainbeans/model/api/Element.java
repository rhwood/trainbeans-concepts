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

import org.netbeans.api.annotations.common.NonNull;
import org.netbeans.api.annotations.common.NullAllowed;

/**
 * An element in the internal model of a model railroad
 *
 * @author rhwood
 */
public interface Element {

    /**
     * Get the name of the element.
     *
     * @return the name
     */
    @NonNull
    public String getName();

    /**
     * Set the name of the element.
     *
     * @param name the new name
     * @throws IllegalArgumentException if another element in the same model has
     * the same name or the name is null or blank (empty or all white space
     * characters) and the Element does not have a {@link Delegate}.
     */
    public void setName(@NullAllowed String name);
}
