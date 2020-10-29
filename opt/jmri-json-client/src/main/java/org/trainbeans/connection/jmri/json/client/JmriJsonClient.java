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
package org.trainbeans.connection.jmri.json.client;

import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.trainbeans.connection.api.AbstractNetworkClientConnection;

/**
 *
 * @author rhwood
 */
public final class JmriJsonClient extends AbstractNetworkClientConnection
        implements LifeCycle.Listener {

    /**
     * The web socket client.
     */
    private final WebSocketClient client;
    /**
     * The web socket handler.
     */
    private final JmriJsonSocket socket = new JmriJsonSocket();

    /**
     * Create the client.
     */
    public JmriJsonClient() {
        client = new WebSocketClient();
        client.addLifeCycleListener(this);
    }

    @Override
    public JmriJsonClient getSelf() {
        return this;
    }

    @Override
    public void start() {
        if (!isRunning()) {
            setState(State.STARTING, null);
            if (!client.isStarted()) {
                try {
                    client.start();
                    client.connect(socket, getURI());
                } catch (Exception ex) {
                    setState(State.FAILED, ex);
                }
            }
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            setState(State.STOPPING, null);
            try {
                client.stop();
            } catch (Exception ex) {
                setState(State.FAILED, ex);
            }
        }
    }

    @Override
    public void lifeCycleStarting(final LifeCycle event) {
        if (event.equals(client)) {
            setState(State.STARTING, null);
        }
    }

    @Override
    public void lifeCycleStarted(final LifeCycle event) {
        if (event.equals(client)) {
            setState(State.STARTED, null);
        }
    }

    @Override
    public void lifeCycleFailure(final LifeCycle event, final Throwable cause) {
        if (event.equals(client)) {
            setState(State.FAILED, cause);
        }
    }

    @Override
    public void lifeCycleStopping(final LifeCycle event) {
        if (event.equals(client)) {
            setState(State.STOPPING, null);
        }
    }

    @Override
    public void lifeCycleStopped(final LifeCycle event) {
        if (event.equals(client)) {
            setState(State.STOPPED, null);
        }
    }

    // package protected for testing
    WebSocketClient getClient() {
        return client;
    }

    // package protected for testing
    JmriJsonSocket getSocket() {
        return socket;
    }
}
