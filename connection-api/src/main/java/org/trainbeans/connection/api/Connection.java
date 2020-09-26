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

import java.util.EventListener;
import org.trainbeans.beans.PropertyChangeProvider;

/**
 *
 * @author rhwood
 */
// State handling in this class are modelled on the Jetty LifeCycle interface
public interface Connection extends PropertyChangeProvider {

    /**
     * The current state of the connection. Use {@link #getState()} to get the
     * state or use {@link #isFailed()}, {@link #isRunning()},
     * {@link #isStarted()}, {@link #isStarting()}, {@link #isStopped()},
     * {@link #isStopping()} to check for specific states.
     */
    enum State {
        /**
         * The connection is starting, but is not yet usable.
         */
        STARTING,
        /**
         * The connection has started correctly and is usable.
         */
        STARTED,
        /**
         * The connection has failed for some reason and is not usable.
         */
        FAILED,
        /**
         * The connection is in the process of stopping and is not usable.
         */
        STOPPING,
        /**
         * The connection is stopped and is not usable. This is the default
         * state of a new connection. Connections may reject setting a state
         * with an {@link IllegalStateException} if not in this state.
         */
        STOPPED
    }

    /**
     * Get the current state of the connection. This is a read-only property. It
     * is recommended that listeners implement {@link Listener} to listen for
     * changes in state. This method can be considered an implementation detail.
     *
     * @return the current state
     */
    State getState();

    /**
     * Start the connection. If this is a serial connection, this opens the
     * port; on network connections, this either connects to an external server
     * or starts an internal server that listens for incoming connections.
     *
     * Implementing methods may throw RuntimeExceptions, and may wrap other
     * exceptions inside RuntimeExceptions, but must document those exceptions.
     */
    void start();

    /**
     * Stop the connection. If this is a serial connection, this closes the
     * port; on network connections, this either disconnects from an external
     * server or stops an internal server.
     *
     * Implementing methods may throw RuntimeExceptions, and may wrap other
     * exceptions inside RuntimeExceptions, but must document those exceptions.
     */
    void stop();

    /**
     * Get the name of the connection.
     *
     * @return the name
     */
    String getName();

    /**
     * Set the name of the connection. Implementations of this must notify
     * {@link java.beans.VetoableChangeListener}s and
     * {@link java.beans.PropertyChangeListener}s.
     *
     * @param <C> the returned type
     * @param name the new name
     * @return this object
     * @throws IllegalArgumentException if the name is blank, or the name is
     * null and the Element does not have a {@link Delegate}
     * @throws IllegalStateException if another element in the same model has
     * the same name
     */
    <C extends Connection> C setName(String name);

    /**
     * Get this object. This is an exposed internal implementation detail that
     * enables a fluent interface and method chaining. It should <em>not</em>
     * be implemented by abstract classes.
     *
     * @param <C> the returned type
     * @return this object
     */
    <C extends Connection> C getSelf();

    /**
     * Check if the connection is running (either {@link State#STARTED} or
     * {@link State#STARTING}).
     *
     * @return true if {@link #isStarted()} or {@link #isStarting()} is true;
     * false otherwise
     */
    default boolean isRunning() {
        return isStarting() || isStarted();
    }

    /**
     * Check if the connection is starting.
     *
     * @return true if the connection is {@link State#STARTING}; false otherwise
     */
    default boolean isStarting() {
        return getState() == State.STARTING;
    }

    /**
     * Check if the connection is started.
     *
     * @return true if the connection is {@link State#STARTED}; false otherwise
     */
    default boolean isStarted() {
        return getState() == State.STARTED;
    }

    /**
     * Check if the connection is stopping.
     *
     * @return true if the connection is {@link State#STOPPING}; false otherwise
     */
    default boolean isStopping() {
        return getState() == State.STOPPING;
    }

    /**
     * Check if the connection is stopped.
     *
     * @return true if the connection is {@link State#STOPPED}; false otherwise
     */
    default boolean isStopped() {
        return getState() == State.STOPPED;
    }

    /**
     * Check if the connection is failed.
     *
     * @return true if the connection is {@link State#FAILED}; false otherwise
     */
    default boolean isFailed() {
        return getState() == State.FAILED;
    }

    /**
     * Listener that can listen to changes in connection state.
     */
    interface Listener extends EventListener {

        /**
         * Called when the connection state changes to {@link State#STARTING}.
         *
         * @param source the connection that changed state
         */
        void connectionStarting(Connection source);

        /**
         * Called when the connection state changes to {@link State#STARTED}.
         *
         * @param source the connection that changed state
         */
        void connectionStarted(Connection source);

        /**
         * Called when the connection state changes to {@link State#FAILED}.
         *
         * @param source the connection that changed state
         * @param cause an exception explaining the failure
         */
        void connectionFailure(Connection source, Throwable cause);

        /**
         * Called when the connection state changes to {@link State#STOPPING}.
         *
         * @param source the connection that changed state
         */
        void connectionStopping(Connection source);

        /**
         * Called when the connection state changes to {@link State#STOPPED}.
         *
         * @param source the connection that changed state
         */
        void connectionStopped(Connection source);
    }

    /**
     * Add a connection listener.
     *
     * @param <C> the returned type
     * @param listener the listener to add
     * @return this connection
     */
    <C extends Connection> C addListener(Listener listener);

    /**
     * Remove a connection listener.
     *
     * @param <C> the returned type
     * @param listener the listener to remove
     * @return this connection
     */
    <C extends Connection> C removeListener(Listener listener);
}
