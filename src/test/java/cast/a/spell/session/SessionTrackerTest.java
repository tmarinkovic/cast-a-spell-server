package cast.a.spell.session;

import cast.a.spell.room.RoomsHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SessionTrackerTest {

    @Mock
    private RoomsHandler roomsHandler;

    @Test
    void shouldAddHostSessionToMapOfActiveSessions() {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        stompHeaderAccessor.setNativeHeader("type", "host");
        stompHeaderAccessor.setSessionId("sessionId");
        SessionTracker sessionTracker = new SessionTracker(roomsHandler);

        sessionTracker.addSession(stompHeaderAccessor);

        assertThat(sessionTracker.getSessionIdType().get("sessionId"), is("host"));
    }

    @Test
    void shouldAddClientSessionToMapOfActiveSessions() {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        stompHeaderAccessor.setNativeHeader("type", "client");
        stompHeaderAccessor.setNativeHeader("clientId", "clientId");
        stompHeaderAccessor.setSessionId("sessionId");
        SessionTracker sessionTracker = new SessionTracker(roomsHandler);

        sessionTracker.addSession(stompHeaderAccessor);

        assertThat(sessionTracker.getSessionIdClientId().get("sessionId"), is("clientId"));
    }

    @Test
    void shouldRemoveHostSessionFromMapOfActiveSessions() {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
        stompHeaderAccessor.setNativeHeader("type", "host");
        stompHeaderAccessor.setSessionId("sessionId");
        SessionTracker sessionTracker = new SessionTracker(roomsHandler);

        sessionTracker.addSession(stompHeaderAccessor);
        sessionTracker.removeSession("sessionId");

        verify(roomsHandler).destroyRoom("sessionId");
        assertThat(sessionTracker.getSessionIdType().size(), is(0));

    }

    @Test
    void shouldRemoveClientSessionFromMapOfActiveSessions() {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        stompHeaderAccessor.setNativeHeader("type", "client");
        stompHeaderAccessor.setNativeHeader("clientId", "clientId");
        stompHeaderAccessor.setSessionId("sessionId");
        SessionTracker sessionTracker = new SessionTracker(roomsHandler);

        sessionTracker.addSession(stompHeaderAccessor);
        sessionTracker.removeSession("sessionId");

        verify(roomsHandler, never()).destroyRoom("sessionId");
        verify(roomsHandler).removeClientFromRoom(any(String.class), any());
        assertThat(sessionTracker.getSessionIdType().size(), is(0));
        assertThat(sessionTracker.getSessionIdClientId().size(), is(0));
    }
}