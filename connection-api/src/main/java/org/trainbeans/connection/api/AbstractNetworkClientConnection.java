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

/**
 *
 * @author rhwood
 */
public abstract class AbstractNetworkClientConnection extends AbstractConnection
        implements NetworkClientConnection {

    /**
     * The URI for the connection.
     */
    private URI uri;

    /**
     * Return the validated URI for the connection. If the URI cannot be
     * validated, implementations must throw an IllegalArgumentException. The
     * default implementation validates any URI with a defined host and port.
     *
     * @param aUri the URI to validate
     * @return the validated URI; this must be equal to the parameter if valid
     * @throws IllegalArgumentException if the URI cannot be validated
     */
    protected URI validatedURI(final URI aUri) {
        if (aUri.getPort() == -1 || aUri.getHost() == null) {
            throw new IllegalArgumentException();
        }
        return aUri;
    }

    /**
     * Normalize the URI for the connection. The default implementation returns
     * the URI unchanged.
     *
     * @param aUri the URI to normalize
     * @return the normalized URI
     */
    protected URI normalizedURI(final URI aUri) {
        return aUri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getURI() {
        return uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends NetworkClientConnection> C setURI(final URI newUri) {
        URI oldUri = uri;
        uri = normalizedURI(validatedURI(newUri));
        firePropertyChange("uri", oldUri, newUri);
        return getSelf();
    }

}
