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
package org.trainbeans.model.impl;

import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.trainbeans.model.api.Turnout;
import org.trainbeans.model.api.TurnoutDelegate;
import org.trainbeans.model.spi.ElementFactory;

/**
 *
 * @author rhwood
 */
@ServiceProvider(service = ElementFactory.class)
public class TurnoutFactory implements ElementFactory<Turnout> {

    @Override
    public Class<Turnout> getElementClass() {
        return Turnout.class;
    }

    @Override
    public Turnout create(String name, Lookup lookup) {
        Turnout turnout = new Turnout();
        if (lookup != null) {
            turnout.setDelegate(lookup.lookup(TurnoutDelegate.class));
        }
        turnout.setName(name);
        return turnout;
    }
    
}
