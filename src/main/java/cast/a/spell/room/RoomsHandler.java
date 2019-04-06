package cast.a.spell.room;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RoomsHandler {

    private Integer currentRoomId;
    private HashMap<String, RoomInformation> rooms;
    private HashMap<String, String> clientIdHostId;

    public RoomsHandler() {
        currentRoomId = 0;
        rooms = new HashMap<>();
        clientIdHostId = new HashMap<>();
    }

    RoomInformation createRoom(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        RoomInformation roomInformation = new RoomInformation(currentRoomId, sessionId);
        rooms.put(sessionId, roomInformation);
        currentRoomId++;
        return roomInformation;
    }

    RoomInformation getNextAvailableRoomForPlayer(String clientId) {
        if (clientId == null) {
            return null;
        }
        for (String hostId : rooms.keySet()) {
            int playerCount = rooms.get(hostId).getClientsId().size();
            if (playerCount < 2) {
                rooms.get(hostId).addClientId(clientId);
                clientIdHostId.put(clientId, hostId);
                return rooms.get(hostId);
            }
        }
        return new RoomInformation(null, null);
    }

    public void destroyRoom(String sessionId) {
        rooms.remove(sessionId);
    }

    public void removeClientFromRoom(String clientId) {
        String hostId = clientIdHostId.get(clientId);
        clientIdHostId.remove(clientId);
        rooms.get(hostId).removeClientId(clientId);
    }

    HashMap<String, RoomInformation> getRooms() {
        return rooms;
    }

    HashMap<String, String> getClientIdHostId() {
        return clientIdHostId;
    }
}
