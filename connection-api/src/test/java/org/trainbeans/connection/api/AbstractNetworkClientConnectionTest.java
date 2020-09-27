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
import java.net.URISyntaxException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
class AbstractNetworkClientConnectionTest extends AbstractNetworkClientConnectionTestBase<AbstractNetworkClientConnectionTest.AbstractNetworkClientConnectionImpl, AbstractNetworkClientConnectionTestBase.ConnectionListenerImpl> {

    @BeforeEach
    void setUp() {
        connection = new AbstractNetworkClientConnectionImpl();
        listener = new ConnectionListenerImpl();
        setupListeners();
    }

    @Test
    @Override
    void testGetState() {
        assertThat(connection.getState()).isEqualTo(Connection.State.STOPPED);
    }

    @Test
    @Override
    void testGetURI() throws URISyntaxException {
        testGetURI(new URI("http://localhost:8080"));
    }

    @Test
    @Override
    void testSetURI() throws URISyntaxException {
        testSetURI(new URI("http://localhost:8080"));
    }

    public class AbstractNetworkClientConnectionImpl extends AbstractNetworkClientConnection {

        @Override
        public AbstractNetworkClientConnectionImpl getSelf() {
            return this;
        }

        @Override
        public void start() {
            setState(Connection.State.STARTING, null);
        }

        @Override
        public void stop() {
            setState(Connection.State.STOPPING, null);
        }
    }

}
