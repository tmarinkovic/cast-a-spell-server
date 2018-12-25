package cast.a.spell.session;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ConnectionEventListenerTest {

    @Mock
    private SessionTracker sessionTracker;
    @Mock
    private SessionConnectEvent sessionConnectEvent;
    @Mock
    private SessionDisconnectEvent sessionDisconnectEvent;

    @Test
    void shouldAddSession_whenNewConnectionEventFired() {
        ConnectionEventListener connectionEventListener = new ConnectionEventListener(sessionTracker);

        connectionEventListener.handleWebsocketConnectListener(sessionConnectEvent);

        verify(sessionTracker).addSession(any(StompHeaderAccessor.class));
    }

    @Test
    void shouldRemoveSession_whenNewDisconnectedEventFired() {
        ConnectionEventListener connectionEventListener = new ConnectionEventListener(sessionTracker);

        connectionEventListener.handleWebsocketDisconnectListener(sessionDisconnectEvent);

        verify(sessionTracker).removeSession(any());
    }
}