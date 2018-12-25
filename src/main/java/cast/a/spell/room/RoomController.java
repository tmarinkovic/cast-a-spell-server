package cast.a.spell.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    private final RoomsHandler roomsHandler;

    @Autowired
    public RoomController(RoomsHandler roomsHandler) {
        this.roomsHandler = roomsHandler;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/host/room/{sessionId}")
    public ResponseEntity<RoomInformation> createRoom(@PathVariable String sessionId) {
        return response(roomsHandler.createRoom(sessionId));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping("/player/room/{sessionId}")
    public ResponseEntity getRoom(@PathVariable String sessionId) {
        return response(roomsHandler.getNextAvailableRoomForPlayer(sessionId));
    }

    private ResponseEntity<RoomInformation> response(RoomInformation roomInformation) {
        if (roomInformation != null) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(roomInformation);
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
