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
package org.trainbeans.app.mr;

import org.apiguardian.api.API;

/**
 *
 * @author rhwood
 */
// package protected
@API(status = API.Status.INTERNAL)
public final class MRConstants {

    /**
     * Folder in a project that contains configuration.
     */
    public static final String PROJECT_CONFIG_DIR = "trainbeans";
    /**
     * Shared XML path relative to project root.
     */
    public static final String PROJECT_XML_PATH
            = PROJECT_CONFIG_DIR + "/project.xml";
    /**
     * Shared properties file in project.
     */
    public static final String PROJECT_PROPERTIES
            = PROJECT_CONFIG_DIR + "/project.properties";

    private MRConstants() {
        // prevent instantiation
    }
}
