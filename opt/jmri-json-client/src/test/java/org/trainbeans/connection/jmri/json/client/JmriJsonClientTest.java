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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trainbeans.connection.api.AbstractConnection;
import org.trainbeans.connection.api.Connection;

/**
 *
 * @author rhwood
 */
class JmriJsonClientTest {

    private JmriJsonClient client;
    private Connection.State state;
    private Throwable thrown;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        client = new JmriJsonClient();
        state = Connection.State.STOPPED;
        thrown = null;
        client.addListener(new Connection.Listener() {
            @Override
            public void connectionStarting(Connection source) {
                state = Connection.State.STARTING;
            }

            @Override
            public void connectionStarted(Connection source) {
                state = Connection.State.STARTED;
            }

            @Override
            public void connectionFailure(Connection source, Throwable cause) {
                state = Connection.State.FAILED;
                thrown = cause;
            }

            @Override
            public void connectionStopping(Connection source) {
                state = Connection.State.STOPPING;
            }

            @Override
            public void connectionStopped(Connection source) {
                state = Connection.State.STOPPED;
            }
        });
    }

    @Test
    void testGetSelf() {
        assertThat(client.getSelf())
                .isEqualTo(client)
                .isExactlyInstanceOf(JmriJsonClient.class);
    }

    @Test
    void testStartWithNullURI() {
        assertThat(client.getURI()).isNull();
        client.start();
        assertThat(state).isEqualTo(Connection.State.FAILED);
        assertThat(thrown).isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void testStartWithWsURI() throws URISyntaxException {
        client.setURI(new URI("ws://localhost:12080/json"));
        client.start();
        assertThat(state).isEqualTo(Connection.State.STARTED);
    }

    @Test
    void testStartWithHttpURI() throws URISyntaxException {
        client.setURI(new URI("http://localhost:12080/json"));
        client.start();
        assertThat(state).isEqualTo(Connection.State.FAILED);
        assertThat(thrown).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testStop() throws URISyntaxException {
        client.setURI(new URI("ws://localhost:12080/json"));
        client.start();
        assertThat(state).isEqualTo(Connection.State.STARTED);
        client.stop();
        assertThat(state).isEqualTo(Connection.State.STOPPED);
    }

    @Test
    void testLifeCycleStarting() throws Exception {
        WebSocketClient event = setNewWebSocketClient();
        assertThat(client.isStopped()).isTrue();
        client.lifeCycleStarting(event);
        assertThat(client.isStarting()).isTrue();
        setState(Connection.State.STOPPED, null);
        client.lifeCycleStarting(new WebSocketClient());
        assertThat(client.isStopped()).isTrue();
    }

    @Test
    void testLifeCycleStarted() throws Exception {
        WebSocketClient event = setNewWebSocketClient();
        assertThat(client.isStopped()).isTrue();
        client.lifeCycleStarted(event);
        assertThat(client.isStarted()).isTrue();
        setState(Connection.State.STOPPED, null);
        client.lifeCycleStarted(new WebSocketClient());
        assertThat(client.isStopped()).isTrue();
    }

    @Test
    void testLifeCycleFailure() throws Exception {
        WebSocketClient event = setNewWebSocketClient();
        assertThat(client.isStopped()).isTrue();
        client.lifeCycleFailure(event, new Exception());
        assertThat(client.isFailed()).isTrue();
        setState(Connection.State.STOPPED, null);
        client.lifeCycleFailure(new WebSocketClient(), null);
        assertThat(client.isStopped()).isTrue();
    }

    @Test
    void testLifeCycleStopping() throws Exception {
        WebSocketClient event = setNewWebSocketClient();
        assertThat(client.isStopped()).isTrue();
        client.lifeCycleStopping(event);
        assertThat(client.isStopping()).isTrue();
        setState(Connection.State.STOPPED, null);
        client.lifeCycleStopping(new WebSocketClient());
        assertThat(client.isStopped()).isTrue();
    }

    @Test
    void testLifeCycleStopped() throws Exception {
        WebSocketClient event = setNewWebSocketClient();
        assertThat(client.isStopped()).isTrue();
        client.lifeCycleStopped(event);
        assertThat(client.isStopped()).isTrue();
        setState(Connection.State.STARTING, null);
        client.lifeCycleStopped(new WebSocketClient());
        assertThat(client.isStarting()).isTrue();
    }

    WebSocketClient setNewWebSocketClient() throws Exception {
        Field field = client.getClass().getDeclaredField("client");
        field.setAccessible(true);
        field.set(client, new WebSocketClient());
        return client.getClient();
    }

    void setState(Connection.State state, Throwable throwable) throws Exception {
        Method method = AbstractConnection.class.getDeclaredMethod("setState", Connection.State.class, Throwable.class);
        method.setAccessible(true);
        method.invoke(client, state, throwable);
    }
}
