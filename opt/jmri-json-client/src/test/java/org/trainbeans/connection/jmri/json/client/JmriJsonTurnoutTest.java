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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.FutureTask;
import static org.assertj.core.api.Assertions.assertThat;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import org.trainbeans.connection.jmri.json.Constant;
import org.trainbeans.model.api.Turnout;

/**
 *
 * @author rhwood
 */
class JmriJsonTurnoutTest {

    private JmriJsonTurnout delegate;
    private String setStateMessage;

    @BeforeEach
    void setUp() {
        RemoteEndpoint re = Mockito.mock(RemoteEndpoint.class);
        Session session = Mockito.mock(Session.class);
        Mockito.when(session.getRemote()).thenReturn(re);
        Mockito.when(re.sendStringByFuture(anyString())).then((invocation) -> {
            setStateMessage = invocation.getArgument(0);
            return new FutureTask<>(() -> null);
        });
        delegate = new JmriJsonTurnout("IT1", session, new ObjectMapper());
        setStateMessage = null;
    }

    @Test
    void testSetState_int() {
        assertThat(delegate.setState(Constant.UNKNOWN)).isEqualTo(delegate);
        assertThat(delegate.getState()).isEqualTo(Turnout.State.UNKNOWN);
        assertThat(delegate.setState(Constant.OFF)).isEqualTo(delegate);
        assertThat(delegate.getState()).isEqualTo(Turnout.State.CLOSED);
        assertThat(delegate.setState(Constant.ON)).isEqualTo(delegate);
        assertThat(delegate.getState()).isEqualTo(Turnout.State.THROWN);
        assertThat(delegate.setState(Constant.CONFLICTED)).isEqualTo(delegate);
        assertThat(delegate.getState()).isEqualTo(Turnout.State.CONFLICTED);
    }

    @Test
    void testSetState_TurnoutState() {
        delegate.setState(Turnout.State.CLOSED);
        assertThat(setStateMessage).isEqualTo("{\"type\":\"turnout\",\"data\":{\"state\":2,\"name\":\"IT1\"}}");
        delegate.setState(Turnout.State.THROWN);
        assertThat(setStateMessage).isEqualTo("{\"type\":\"turnout\",\"data\":{\"state\":4,\"name\":\"IT1\"}}");
        delegate.setState(Turnout.State.CONFLICTED);
        assertThat(setStateMessage).isEqualTo("{\"type\":\"turnout\",\"data\":{\"state\":0,\"name\":\"IT1\"}}");
        setStateMessage = null; // reset
        delegate.setState(Turnout.State.UNKNOWN);
        assertThat(setStateMessage).isEqualTo("{\"type\":\"turnout\",\"data\":{\"state\":0,\"name\":\"IT1\"}}");
    }

    @Test
    void testGetRequestedState() {
        assertThat(delegate.getRequestedState()).isEqualTo(Turnout.State.UNKNOWN);
        delegate.setState(Turnout.State.CLOSED);
        assertThat(delegate.getState()).isEqualTo(Turnout.State.UNKNOWN);
        assertThat(delegate.getRequestedState()).isEqualTo(Turnout.State.CLOSED);
    }

    @Test
    void testIsValidName() {
        assertThat(delegate.isValidName("ITfoo")).isTrue();
        assertThat(delegate.isValidName("I2Tfoo")).isTrue();
        assertThat(delegate.isValidName("IT1")).isTrue();
        assertThat(delegate.isValidName("I22T1")).isTrue();
        assertThat(delegate.isValidName("foo")).isFalse();
        assertThat(delegate.isValidName("IT")).isFalse();
        assertThat(delegate.isValidName("I2T")).isFalse();
    }

    @Test
    void testGetSelf() {
        assertThat(delegate.getSelf()).isEqualTo(delegate);
    }

}
