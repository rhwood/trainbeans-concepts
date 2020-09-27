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

import java.beans.PropertyChangeEvent;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
abstract class AbstractConnectionTest<C extends Connection, L extends Connection.Listener> {

    C connection;
    PropertyChangeEvent lastEvent;
    L listener;

    void setupListeners() {
        connection.addPropertyChangeListener(event -> lastEvent = event);
        connection.addListener(listener);
        lastEvent = null;
    }

    abstract void testGetState();

    @Test
    void testGetName() {
        assertThat(connection.getName()).isNull();
        connection.setName("foo");
        assertThat(connection.getName()).isEqualTo("foo");
    }

    @Test
    void testSetName() {
        assertThat(connection.getName()).isNull();
        assertThat(lastEvent).isNull();
        assertThat((Object) connection.setName("foo")).isEqualTo(connection);
        assertThat(connection.getName()).isEqualTo("foo");
        assertThat(lastEvent.getSource()).isEqualTo(connection);
        assertThat(lastEvent.getOldValue()).isNull();
        assertThat(lastEvent.getNewValue()).isEqualTo("foo");
        assertThat(lastEvent.getPropertyName()).isEqualTo("name");
    }

    @Test
    void testAddListener() {
        connection.removeListener(listener);
        assertThat(connection.getListeners()).doesNotContain(listener);
        assertThat((Object) connection.addListener(listener)).isEqualTo(connection);
        assertThat(connection.getListeners()).contains(listener);
    }

    @Test
    void testRemoveListener() {
        assertThat(connection.getListeners()).contains(listener);
        assertThat((Object) connection.removeListener(listener)).isEqualTo(connection);
        assertThat(connection.getListeners()).doesNotContain(listener);
    }

    @Test
    void testGetListeners() {
        connection.removeListener(listener);
        assertThat(connection.getListeners()).isEmpty();
        connection.addListener(listener);
        assertThat(connection.getListeners()).contains(listener);
    }

    class AbstractConnectionImpl extends AbstractConnection {

        @Override
        public AbstractConnectionImpl getSelf() {
            return this;
        }

        @Override
        public void start() {
            setState(State.STARTING, null);
        }

        @Override
        public void stop() {
            setState(State.STOPPING, null);
        }

        // expand scope for testing purposes
        @Override
        public void setState(State newState, Throwable cause) {
            super.setState(newState, cause);
        }
    }

    class ConnectionListenerImpl implements Connection.Listener {

        public Connection source = null;
        public Connection.State state = Connection.State.STOPPED;
        public Throwable cause = null;

        @Override
        public void connectionStarting(Connection source) {
            this.source = source;
            this.state = Connection.State.STARTING;
        }

        @Override
        public void connectionStarted(Connection source) {
            this.source = source;
            this.state = Connection.State.STARTED;
        }

        @Override
        public void connectionFailure(Connection source, Throwable cause) {
            this.source = source;
            this.state = Connection.State.FAILED;
            this.cause = cause;
        }

        @Override
        public void connectionStopping(Connection source) {
            this.source = source;
            this.state = Connection.State.STOPPING;
        }

        @Override
        public void connectionStopped(Connection source) {
            this.source = source;
            this.state = Connection.State.STOPPED;
        }

    }
}
