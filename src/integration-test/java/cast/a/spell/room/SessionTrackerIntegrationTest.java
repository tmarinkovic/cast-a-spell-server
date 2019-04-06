package cast.a.spell.room;

import cast.a.spell.session.SessionTracker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest()
public class SessionTrackerIntegrationTest {

    @Autowired
    private SessionTracker sessionTracker;

    @Test
    void shouldRemoveClientFromRoom() {
        sessionTracker.getRoomsHandler().createRoom("host-session-id");
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        stompHeaderAccessor.setNativeHeader("type", "client");
        stompHeaderAccessor.setNativeHeader("clientId", "clientId");
        stompHeaderAccessor.setSessionId("client-sessionId");
        sessionTracker.addSession(stompHeaderAccessor);
        sessionTracker.getRoomsHandler().getNextAvailableRoomForPlayer("clientId");

        sessionTracker.removeSession("client-sessionId");

        RoomInformation roomInformation = sessionTracker.getRoomsHandler().getRooms().get("host-session-id");
        assertThat(roomInformation.getClientsId().size(), is(0));

    }

}
