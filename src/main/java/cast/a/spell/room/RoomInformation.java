package cast.a.spell.room;

import java.util.ArrayList;
import java.util.List;

class RoomInformation {

    private List<String> clientsId;
    private String hostId;
    private Integer roomId;

    RoomInformation(Integer roomId, String hostId) {
        this.hostId = hostId;
        this.roomId = roomId;
        this.clientsId = new ArrayList<>();
    }

    void addClientId(String clientId) {
        this.clientsId.add(clientId);
    }

    void removeClientId(String clientId) {
        this.clientsId.remove(clientId);
    }

    List<String> getClientsId() {
        return clientsId;
    }

    Integer getRoomId() {
        return roomId;
    }

    String getHostId() {
        return hostId;
    }
}
