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
 * An {@link Element} that can have a {@link Delegate} assigned to it.
 * 
 * @author rhwood
 */
public interface DelegatingElement extends Element {

    public <T extends DelegatingElement> Delegate<T> getDelegate();

    public <T extends DelegatingElement> void setDelegate(Delegate<T> delegate);
}
