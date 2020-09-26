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

import java.util.HashSet;
import java.util.Set;
import org.trainbeans.beans.Bean;

/**
 *
 * @author rhwood
 */
public abstract class AbstractConnection extends Bean implements Connection {

    /**
     * The current state of this connection. Defaults to {@link State#STOPPED}.
     */
    private State state = State.STOPPED;
    /**
     * The name of the connection.
     */
    private String name;
    /**
     * Listeners listening to this connection.
     */
    private final Set<Listener> listeners = new HashSet<>();

    /**
     * Set the connection state and notify all listeners.
     *
     * @param newState the new state to set
     * @param cause the cause of a failed state
     */
    protected void setState(final State newState, final Throwable cause) {
        State oldState = state;
        state = newState;
        Set<Listener> toNotify = new HashSet<>(listeners);
        switch (state) {
            case STARTING:
                toNotify.forEach(listener -> listener.connectionStarting(this));
                break;
            case STARTED:
                toNotify.forEach(listener -> listener.connectionStarted(this));
                break;
            case FAILED:
                toNotify.forEach(listener
                        -> listener.connectionFailure(this, cause));
                break;
            case STOPPING:
                toNotify.forEach(listener -> listener.connectionStopping(this));
                break;
            case STOPPED:
                toNotify.forEach(listener -> listener.connectionStopped(this));
                break;
            default:
            // do nothing
        }
        firePropertyChange("state", oldState, newState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        return state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends Connection> C setName(final String newName) {
        String oldName = name;
        name = newName;
        firePropertyChange("name", oldName, newName);
        return getSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends Connection> C addListener(final Listener listener) {
        listeners.add(listener);
        return getSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <C extends Connection> C removeListener(final Listener listener) {
        listeners.remove(listener);
        return getSelf();
    }

}
