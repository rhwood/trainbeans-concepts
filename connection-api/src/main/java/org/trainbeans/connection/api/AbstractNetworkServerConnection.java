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

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author rhwood
 */
public abstract class AbstractNetworkServerConnection extends AbstractConnection
        implements NetworkServerConnection {

    /**
     * The port for this server to listen on; defaults to 0 to allow automatic
     * starting with an ephemeral port.
     */
    private int port = 0;
    /**
     * Valid ports are in the range 0 to {@value #MAX_PORT}.
     */
    public static final int MAX_PORT = 65535;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends NetworkServerConnection> C setPort(final int newPort) {
        if (newPort < 0 || newPort > MAX_PORT) {
            throw new IllegalArgumentException();
        }
        if (!isStopped()) {
            throw new IllegalStateException();
        }
        try {
            // if the port cannot be opened (it is in use or protected),
            // catch the exception, and immediately close if opened.
            new ServerSocket(newPort).close();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        int oldPort = port;
        port = newPort;
        firePropertyChange("port", oldPort, newPort);
        return getSelf();
    }

}
