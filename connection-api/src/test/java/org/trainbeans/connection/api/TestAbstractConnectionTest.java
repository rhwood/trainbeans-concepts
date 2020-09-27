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

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
class TestAbstractConnectionTest extends AbstractConnectionTest<AbstractConnectionTest.AbstractConnectionImpl, AbstractConnectionTest.ConnectionListenerImpl> {

    @BeforeEach
    void setUp() {
        connection = new AbstractConnectionImpl();
        listener = new ConnectionListenerImpl();
        setupListeners();
    }

    @Test
    void testSetState() {
        assertThat(connection.getState()).isEqualTo(Connection.State.STOPPED);
        assertThat(listener.source).isNull();
        assertThat(listener.state).isEqualTo(Connection.State.STOPPED);
        assertThat(listener.cause).isNull();
        assertThat(connection.isFailed()).isFalse();
        assertThat(connection.isRunning()).isFalse();
        assertThat(connection.isStarted()).isFalse();
        assertThat(connection.isStarting()).isFalse();
        assertThat(connection.isStopped()).isTrue();
        assertThat(connection.isStopping()).isFalse();
        connection.start();
        assertThat(connection.getState()).isEqualTo(Connection.State.STARTING);
        assertThat(listener.source).isEqualTo(connection);
        assertThat(listener.state).isEqualTo(Connection.State.STARTING);
        assertThat(listener.cause).isNull();
        assertThat(connection.isFailed()).isFalse();
        assertThat(connection.isRunning()).isTrue();
        assertThat(connection.isStarted()).isFalse();
        assertThat(connection.isStarting()).isTrue();
        assertThat(connection.isStopped()).isFalse();
        assertThat(connection.isStopping()).isFalse();
        connection.setState(Connection.State.STARTED, null);
        assertThat(listener.source).isEqualTo(connection);
        assertThat(listener.state).isEqualTo(Connection.State.STARTED);
        assertThat(listener.cause).isNull();
        assertThat(connection.isFailed()).isFalse();
        assertThat(connection.isRunning()).isTrue();
        assertThat(connection.isStarted()).isTrue();
        assertThat(connection.isStarting()).isFalse();
        assertThat(connection.isStopped()).isFalse();
        assertThat(connection.isStopping()).isFalse();
        connection.stop();
        assertThat(listener.source).isEqualTo(connection);
        assertThat(listener.state).isEqualTo(Connection.State.STOPPING);
        assertThat(listener.cause).isNull();
        assertThat(connection.isFailed()).isFalse();
        assertThat(connection.isRunning()).isFalse();
        assertThat(connection.isStarted()).isFalse();
        assertThat(connection.isStarting()).isFalse();
        assertThat(connection.isStopped()).isFalse();
        assertThat(connection.isStopping()).isTrue();
        connection.setState(Connection.State.STOPPED, null);
        assertThat(listener.source).isEqualTo(connection);
        assertThat(listener.state).isEqualTo(Connection.State.STOPPED);
        assertThat(listener.cause).isNull();
        assertThat(connection.isFailed()).isFalse();
        assertThat(connection.isRunning()).isFalse();
        assertThat(connection.isStarted()).isFalse();
        assertThat(connection.isStarting()).isFalse();
        assertThat(connection.isStopped()).isTrue();
        assertThat(connection.isStopping()).isFalse();
        Throwable throwable = new RuntimeException();
        connection.setState(Connection.State.FAILED, throwable);
        assertThat(listener.source).isEqualTo(connection);
        assertThat(listener.state).isEqualTo(Connection.State.FAILED);
        assertThat(listener.cause).isEqualTo(throwable);
        assertThat(connection.isFailed()).isTrue();
        assertThat(connection.isRunning()).isFalse();
        assertThat(connection.isStarted()).isFalse();
        assertThat(connection.isStarting()).isFalse();
        assertThat(connection.isStopped()).isFalse();
        assertThat(connection.isStopping()).isFalse();
    }

    @Override
    void testGetState() {
        assertThat(connection.getState()).isEqualTo(Connection.State.STOPPED);
    }

}
