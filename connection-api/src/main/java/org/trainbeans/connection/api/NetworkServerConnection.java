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

/**
 *
 * @author rhwood
 */
public interface NetworkServerConnection extends Connection {

    /**
     * Get the port.
     *
     * @return the port
     */
    int getPort();

    /**
     * Set the port. Setting the port to 0 will cause a port in the range 49152
     * to 65535 to be used instead.
     *
     * @param <C> the type of connection
     * @param port the port for the connection to listen on in the range 0 to
     * 65535
     * @return this connection
     * @throws IllegalArgumentException if port is not in range
     * @throws IllegalStateException if port is not assignable or if
     * {@link #getState()} is not equal to {@link State#STOPPED}
     */
    <C extends NetworkServerConnection> C setPort(int port);
}