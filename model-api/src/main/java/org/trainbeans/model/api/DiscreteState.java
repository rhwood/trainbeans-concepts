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
 * The DiscreteState is implemented by subclasses of
 * {@link DiscreteStateElement} as a public enum within the class so that
 * {@link DiscreteStateElement#getState()},
 * {@link DiscreteStateElement#setState(DiscreteState)}, and
 * {@link DiscreteStateElement#getRequestedState()} can all contain meaningful
 * values specific to the type of states allowed while using enums.
 *
 * @author rhwood
 */
public interface DiscreteState {

}
