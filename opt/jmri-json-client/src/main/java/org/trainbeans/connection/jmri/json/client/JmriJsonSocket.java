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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 *
 * @author rhwood
 */
@WebSocket
class JmriJsonSocket {

    /**
     * JSON object factory.
     */
    private final ObjectMapper mapper = new ObjectMapper();
    /**
     * A ping frame for the heartbeat.
     */
    private final String ping = mapper.createObjectNode()
            .put("type", "ping")
            .toString();
    /**
     * The connected session.
     */
    private Session session;
    /**
     * JMRI JSON servers require a heartbeat.
     */
    private ScheduledFuture<?> heartbeat;
    /**
     * All turnouts on this connection.
     */
    private final Map<String, JmriJsonTurnout> turnouts = new HashMap<>();

    @OnWebSocketClose
    public void onClose(final int statusCode, final String reason) {
        heartbeat.cancel(true);
    }

    @OnWebSocketConnect
    public void onConnect(final Session aSession) {
        session = aSession;
    }

    @OnWebSocketMessage
    public void onMessage(final String msg) {
        try {
            JsonNode node = mapper.readTree(msg);
            if (node.isArray()) {
                node.elements().forEachRemaining(this::onMessage);
            } else {
                onMessage(node);
            }
        } catch (JsonProcessingException ex) {
            onError(ex);
        }
    }

    private void onMessage(final JsonNode node) {
        switch (node.path("type").asText()) {
            case "hello":
                long interval = node.path("data").path("heartbeat").asLong();
                heartbeat = Executors.newSingleThreadScheduledExecutor()
                        .scheduleAtFixedRate(() -> session.getRemote()
                        .sendStringByFuture(ping),
                                interval,
                                interval,
                                TimeUnit.MILLISECONDS);
                session.getRemote().sendStringByFuture(mapper.createObjectNode()
                        .put("type", "list")
                        .put("list", "turnout")
                        .toString());
                break;
            case "turnout":
                // if turnout is unknown, add it, else update it
                String name = node.path("data").path("name").asText();
                turnouts.getOrDefault(name, new JmriJsonTurnout(name,
                        session,
                        mapper))
                        .setState(node.path("data").path("state").asInt(0));
                break;
            case "pong":
                // ignore it
                break;
            default:
            // log unknown message type
            }
    }

    @OnWebSocketError
    public void onError(final Throwable cause) {
        // handle appropriately
    }

}
