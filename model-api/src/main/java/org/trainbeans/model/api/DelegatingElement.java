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
 * An {@link Element} that can have a {@link Delegate} assigned to it. Some
 * properties that must not be null in Elements can be null in a
 * DelegatingElement since the Delegate may provide the values for those
 * properties.
 *
 * @author rhwood
 */
public interface DelegatingElement extends Element {

    /**
     * Get the delegate for this element.
     *
     * @param <T> the type of element that the Delegate can support
     * @return the delegate or null if there is no delegate
     */
    public <T extends DelegatingElement> Delegate<T> getDelegate();

    /**
     * Set the delegate for this element.
     *
     * @param <T> the type of element that the Delegate can support
     * @param delegate the delegate or null if removing a delegate
     */
    // TODO what should this throw if the Delegate is the wrong class?
    //      inheritance rules for generics mean that I can't make T meaningfully
    //      specific in concrete implementations, so should this just have a
    //      runtime exception and no generics for this method?
    public <T extends DelegatingElement> void setDelegate(Delegate<T> delegate);
}
