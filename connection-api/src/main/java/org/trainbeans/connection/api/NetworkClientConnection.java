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
package org.trainbeans.connection.api;

import java.net.URI;

/**
 *
 * @author rhwood
 */
public interface NetworkClientConnection extends Connection {

    /**
     * Get the URI for the connection.
     *
     * @return the URI
     */
    URI getURI();

    /**
     * Set the URI for the connection.
     *
     * @param <C> the type of connection
     * @param uri the URI to set
     * @return the connection
     * @throws IllegalArgumentException if the URI is not valid for this
     * connection
     */
    <C extends NetworkClientConnection> C setURI(URI uri);
}
