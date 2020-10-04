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

import java.io.IOException;
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
    private WebSocketClient client;
    /**
     * The web socket handler.
     */
    private JmriJsonSocket socket;

    @Override
    public JmriJsonClient getSelf() {
        return this;
    }

    @Override
    public void start() {
        setState(State.STARTING, null);
        if (client == null) {
            client = new WebSocketClient();
            client.addLifeCycleListener(this);
        }
        if (socket == null) {
            socket = new JmriJsonSocket();
        }
        if (!client.isRunning()) {
            try {
                client.connect(socket, getURI());
            } catch (IOException ex) {
                setState(State.FAILED, ex);
            }
        }
    }

    @Override
    public void stop() {
        if (client != null) {
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

}
