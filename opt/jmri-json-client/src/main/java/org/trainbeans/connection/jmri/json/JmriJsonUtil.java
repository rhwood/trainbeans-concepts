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
package org.trainbeans.connection.jmri.json;

/**
 * Utility methods for JMRI JSON connections.
 *
 * @author rhwood
 */
public final class JmriJsonUtil {

    /**
     * Common method to validate a Delegate name. Note that this method cannot
     * tell if the name would be valid for a specific type of JMRI connection,
     * only that the name matches the pattern of all valid JMRI system names.
     *
     * @param name the name to validate
     * @param typeLetter the JMRI NamedBean type letter
     * @return true if valid; false otherwise
     */
    public static boolean isValidName(final String name,
            final String typeLetter) {
        return name.matches("[A-Z][0-9]*" + typeLetter + ".+");
    }

    private JmriJsonUtil() {
        // prevent construction
        throw new UnsupportedOperationException();
    }
}
