package cast.a.spell.room;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertThat;


@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomsHandler roomsHandler;

    @Test
    void shouldReturnBadRequestCreatingRoom_whenNoSessionIdIsSent() {
        when(roomsHandler.createRoom(null)).thenReturn(null);
        RoomController roomController = new RoomController(roomsHandler);

        ResponseEntity response = roomController.createRoom(null);

        assertThat(response.getStatusCodeValue(), is(400));
    }

    @Test
    void shouldReturnRoomInformation_whenCreatingRoom() {
        RoomInformation expectedRoomInformation = new RoomInformation(0, "hostId");
        when(roomsHandler.createRoom("hostId")).thenReturn(expectedRoomInformation);
        RoomController roomController = new RoomController(roomsHandler);

        ResponseEntity response = roomController.createRoom("hostId");

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(expectedRoomInformation));
    }

    @Test
    void shouldReturnBadRequestGettingNextAvailableRoom_whenNoSessionIdIsSent() {
        when(roomsHandler.getNextAvailableRoomForPlayer(null)).thenReturn(null);
        RoomController roomController = new RoomController(roomsHandler);

        ResponseEntity response = roomController.getRoom(null);

        assertThat(response.getStatusCodeValue(), is(400));
    }

    @Test
    void shouldReturnRoomInformation_whenGettingNextAvailableRoom() {
        RoomInformation expectedRoomInformation = new RoomInformation(0, "hostId");
        when(roomsHandler.getNextAvailableRoomForPlayer("clientId")).thenReturn(expectedRoomInformation);
        RoomController roomController = new RoomController(roomsHandler);

        ResponseEntity response = roomController.getRoom("clientId");

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(expectedRoomInformation));
    }
}