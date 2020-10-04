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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.jetty.websocket.api.Session;
import org.trainbeans.model.api.AbstractDiscreteStateDelegate;
import org.trainbeans.model.api.Turnout;
import org.trainbeans.model.api.Turnout.State;
import org.trainbeans.model.api.TurnoutDelegate;

/**
 *
 * @author rhwood
 */
@SuppressWarnings("linelength") // generic definitions on single line
public final class JmriJsonTurnout extends AbstractDiscreteStateDelegate<State, Turnout> implements TurnoutDelegate {

    /**
     * The current requested state.
     */
    private State requestedState = State.UNKNOWN;
    /**
     * The connected session.
     */
    private final Session session;
    /**
     * The JSON object factory.
     */
    private final ObjectMapper mapper;

    JmriJsonTurnout(final String name, final Session aSession,
            final ObjectMapper aMapper) {
        setName(name);
        session = aSession;
        mapper = aMapper;
    }

    /**
     * Allow the connection to set the state.
     *
     * @param state the new state from the connection
     * @return this delegate
     */
    JmriJsonTurnout setState(final int state) {
        State newState;
        switch (state) {
            case Constant.OFF:
                newState = State.CLOSED;
                break;
            case Constant.ON:
                newState = State.THROWN;
                break;
            case Constant.CONFLICTED:
                newState = State.CONFLICTED;
                break;
            default:
                newState = State.UNKNOWN;
        }
        return super.setState(newState);
    }

    @Override
    public JmriJsonTurnout setState(final State state) {
        requestedState = state;
        int update;
        switch (state) {
            case THROWN:
                update = Constant.ON;
                break;
            case CLOSED:
                update = Constant.OFF;
                break;
            default:
                update = Constant.UNKNOWN;
        }
        ObjectNode data = mapper.createObjectNode()
                .put("state", update)
                .put("name", getName());
        ObjectNode root = mapper.createObjectNode()
                .put("type", "turnout")
                .set("data", data);
        session.getRemote().sendStringByFuture(root.toString());
        return super.setState(state);
    }

    @Override
    public State getRequestedState() {
        return requestedState;
    }

    @Override
    protected boolean isValidName(final String aName) {
        return true;
    }

    @Override
    public JmriJsonTurnout getSelf() {
        return this;
    }

}
