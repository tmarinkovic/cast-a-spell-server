package cast.a.spell.room;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

class RoomsHandlerTest {

    @Test
    void shouldReturnNull_whenCreatingRoom_withNoSessionId() {
        RoomsHandler roomsHandler = new RoomsHandler();

        RoomInformation roomInformation = roomsHandler.createRoom(null);

        assertThat(roomInformation, nullValue());
    }

    @Test
    void shouldIncludeRoomId_whenRoomIsCreated() {
        RoomsHandler roomsHandler = new RoomsHandler();

        RoomInformation roomInformation = roomsHandler.createRoom("sessionId");

        assertThat(roomInformation.getRoomId(), is(0));
    }

    @Test
    void shouldAddRoomToActiveRooms_whenRoomIsCreated() {
        RoomsHandler roomsHandler = new RoomsHandler();

        RoomInformation roomInformation = roomsHandler.createRoom("sessionId");

        assertThat(roomsHandler.getRooms().get("sessionId"), is(roomInformation));
    }

    @Test
    void shouldIncreaseRoomId_whenMoreThanOneRoomIsCreated() {
        RoomsHandler roomsHandler = new RoomsHandler();

        roomsHandler.createRoom("sessionId-1");
        RoomInformation roomInformation = roomsHandler.createRoom("sessionId-2");

        assertThat(roomInformation.getRoomId(), is(1));
    }

    @Test
    void shouldReturnNull_whenGettingAvailableRoom_withNoClientId() {
        RoomsHandler roomsHandler = new RoomsHandler();

        RoomInformation roomInformation = roomsHandler.getNextAvailableRoomForPlayer(null);

        assertThat(roomInformation, nullValue());
    }

    @Test
    void shouldReturnRoomNotFound_whenNoRoomIsAvailable() {
        RoomsHandler roomsHandler = new RoomsHandler();

        RoomInformation roomInformation = roomsHandler.getNextAvailableRoomForPlayer("clientId");

        assertThat(roomInformation.getRoomId(), nullValue());
        assertThat(roomInformation.getHostId(), nullValue());
    }

    @Test
    void shouldReturnRoomInformation_whenThereIsRoom() {
        RoomsHandler roomsHandler = new RoomsHandler();
        roomsHandler.createRoom("sessionId");

        RoomInformation roomInformation = roomsHandler.getNextAvailableRoomForPlayer("clientId");

        assertThat(roomInformation.getRoomId(), is(0));
        assertThat(roomInformation.getHostId(), is("sessionId"));
    }

    @Test
    void shouldAddClientToRoom_whenThereIsRoom() {
        RoomsHandler roomsHandler = new RoomsHandler();
        roomsHandler.createRoom("sessionId");

        roomsHandler.getNextAvailableRoomForPlayer("clientId");

        assertThat(roomsHandler.getRooms().get("sessionId").getClientsId().get(0), is("clientId"));
        assertThat(roomsHandler.getClientIdHostId().get("clientId"), is("sessionId"));
    }

    @Test
    void shouldReturnRoomNotFound_whenAllRoomsAreFull() {
        RoomsHandler roomsHandler = new RoomsHandler();
        roomsHandler.createRoom("sessionId");
        roomsHandler.getNextAvailableRoomForPlayer("clientId-1");
        roomsHandler.getNextAvailableRoomForPlayer("clientId-2");

        RoomInformation roomInformation = roomsHandler.getNextAvailableRoomForPlayer("clientId-3");

        assertThat(roomInformation.getRoomId(), nullValue());
        assertThat(roomInformation.getHostId(), nullValue());
    }

    @Test
    void shouldRemoveRoomFromRooms_whenSessionIsBroken() {
        RoomsHandler roomsHandler = new RoomsHandler();

        roomsHandler.createRoom("sessionId");
        roomsHandler.destroyRoom("sessionId");

        assertThat(roomsHandler.getRooms().size(), is(0));
    }

    @Test
    void shouldRemoveClientFromRoom_whenSessionIsBroken() {
        RoomsHandler roomsHandler = new RoomsHandler();

        roomsHandler.createRoom("sessionId");
        roomsHandler.getNextAvailableRoomForPlayer("clientId");
        roomsHandler.removeClientFromRoom("clientId");

        assertThat(roomsHandler.getClientIdHostId().size(), is(0));
    }
}