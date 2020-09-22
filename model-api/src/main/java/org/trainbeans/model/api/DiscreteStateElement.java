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
 * An element that has a set number of states that can be represented as
 * integers.
 *
 * @author rhwood
 */
public interface DiscreteStateElement extends Element {

    /**
     * Get the state.
     *
     * @return the state
     */
    DiscreteState getState();

    /**
     * Set the state.
     *
     * @param <T> the returned type
     * @param state the state to set
     * @return this object
     */
    <T extends DiscreteStateElement> T setState(DiscreteState state);

    /**
     * Get the the state that was last set. This may differ from
     * {@link #getState()} if a delegate has set the state of an external
     * element and awaiting confirmation that the state was set correctly. If no
     * delegate is present, this should always be the same as
     * {@link #getState()}.
     *
     * @return the requested set
     */
    DiscreteState getRequestedState();
}
