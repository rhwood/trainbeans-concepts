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
package org.trainbeans.jmri.json.client;

/**
 *
 * @author rhwood
 */
final class Constant {

    /**
     * JMRI JSON unknown value. {@value #UNKNOWN}
     */
    static final int UNKNOWN = 0;
    /**
     * JMRI JSON on/thrown value. {@value #ON}
     */
    static final int ON = 4;
    /**
     * JMRI JSON off/closed value. {@value #OFF}
     */
    static final int OFF = 2;
    /**
     * JMRI JSON conflicted/indeterminate value. {@value #CONFLICTED}
     */
    static final int CONFLICTED = 8;

    private Constant() {
        // prevent construction
    }
}
