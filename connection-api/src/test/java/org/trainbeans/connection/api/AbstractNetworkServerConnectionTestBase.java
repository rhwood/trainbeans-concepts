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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.Test;
import static org.trainbeans.connection.api.AbstractNetworkServerConnection.MAX_PORT;

/**
 *
 * @author rhwood
 */
abstract class AbstractNetworkServerConnectionTestBase<C extends AbstractNetworkServerConnection, L extends Connection.Listener> extends AbstractConnectionTestBase<C, L> {

    @Test
    void testGetPort() throws IOException {
        assertThat(connection.getPort()).isZero();
        int port;
        try (ServerSocket ss = new ServerSocket(0)) {
            port = ss.getLocalPort();
        }
        connection.setPort(port);
        assertThat(connection.getPort()).isEqualTo(port);
    }

    @Test
    void testSetPort() throws IOException {
        assertThat(connection.isStopped()).isTrue();
        assertThat(connection.getPort()).isZero();
        int port1;
        try (ServerSocket ss = new ServerSocket(0)) {
            port1 = ss.getLocalPort();
        }
        assertThat(port1).isNotZero();
        connection.setPort(port1);
        assertThat(connection.getPort()).isEqualTo(port1);
        connection.setPort(0);
        assertThat(connection.getPort()).isZero();
        assertThatCode(() -> connection.setPort(-1))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        assertThatCode(() -> connection.setPort(MAX_PORT + 1))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        try (ServerSocket ss = new ServerSocket(0)) {
            int port2 = ss.getLocalPort();
            assertThatCode(() -> connection.setPort(port2))
                    .isExactlyInstanceOf(IllegalStateException.class);
        }
        // this should be last test, since API does not allow us to reliably set
        // the state back to stopped within a test
        connection.start();
        assertThatCode(() -> connection.setPort(0))
                .isExactlyInstanceOf(IllegalStateException.class);
    }

}
