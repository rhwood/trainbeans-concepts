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

/**
 * Get the localized name of the supported Element class or interface.
 *
 * @author rhwood
 */
public interface ClassDescriptor {

    /**
     * Get the supported class.
     *
     * @return the supported class
     */
    Class<?> getElementClass();

    /**
     * Get the type name for a single item of the supported class.
     *
     * @return the singular name
     */
    String getSingularName();

    /**
     * Get the type name for a collection of items of the supported class.
     *
     * @return the plural name
     */
    String getPluralName();
}
