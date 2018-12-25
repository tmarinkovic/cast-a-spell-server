package cast.a.spell.session;

import cast.a.spell.room.RoomsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class SessionTracker {

    private final RoomsHandler roomsHandler;
    private HashMap<String, String> sessionIdType;
    private HashMap<String, String> sessionIdClientId;

    @Autowired
    public SessionTracker(RoomsHandler roomsHandler) {
        sessionIdType = new HashMap<>();
        sessionIdClientId = new HashMap<>();
        this.roomsHandler = roomsHandler;
    }

    void addSession(StompHeaderAccessor stompHeaderAccessor) {
        List<String> type = stompHeaderAccessor.getNativeHeader("type");
        if (type != null && type.size() > 0) {
            sessionIdType.put(stompHeaderAccessor.getSessionId(), type.get(0));
            if (type.get(0).equals("client")) {
                List<String> clientId = stompHeaderAccessor.getNativeHeader("clientId");
                assert clientId != null;
                sessionIdClientId.put(stompHeaderAccessor.getSessionId(), clientId.get(0));
            }
        }
    }

    void removeSession(String sessionId) {
        String type = sessionIdType.get(sessionId);
        if (type.equals("host")) {
            roomsHandler.destroyRoom(sessionId);
        } else {
            String clientId = sessionIdClientId.get(sessionId);
            roomsHandler.removeClientFromRoom(clientId, sessionId);
            sessionIdClientId.remove(sessionId);
        }
        sessionIdType.remove(sessionId);
    }

    HashMap<String, String> getSessionIdType() {
        return sessionIdType;
    }

    HashMap<String, String> getSessionIdClientId() {
        return sessionIdClientId;
    }
}
