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
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.Test;

/**
 *
 * @author rhwood
 */
abstract class AbstractNetworkClientConnectionTestBase<C extends AbstractNetworkClientConnection, L extends Connection.Listener> extends AbstractConnectionTestBase<C, L> {

    @Test
    void testValidatedURI() throws URISyntaxException {
        URI uri1 = new URI(null, null, "localhost", 8080, null, null, null);
        assertThat(connection.validatedURI(uri1)).isEqualTo(uri1);
        URI uri2 = new URI(null, null, "localhost", -1, null, null, null);
        assertThatCode(() -> connection.validatedURI(uri2))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        URI uri3 = new URI(null, null, null, 8080, null, null, null);
        assertThatCode(() -> connection.validatedURI(uri3))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        URI uri4 = new URI(null, null, null, -1, null, null, null);
        assertThatCode(() -> connection.validatedURI(uri4))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testNormalizedURI() throws URISyntaxException {
        URI uri = new URI("http://localhost:8080");
        assertThat(connection.normalizedURI(uri)).isEqualTo(uri);
    }

    @Test
    void testGetURI() throws URISyntaxException {
        testGetURI(new URI("http://localhost:8080"));
    }

    @Test
    void testSetURI() throws URISyntaxException {
        testSetURI(new URI("http://localhost:8080"));
    }

    void testGetURI(URI uri) {
        assertThat(connection.getURI()).isNull();
        connection.setURI(uri);
        assertThat(connection.getURI()).isEqualTo(uri);
    }

    void testSetURI(URI uri) {
        assertThat(connection.getURI()).isNull();
        assertThat((Object) connection.setURI(uri)).isEqualTo(connection);
        assertThat(connection.getURI()).isEqualTo(uri);
    }

}