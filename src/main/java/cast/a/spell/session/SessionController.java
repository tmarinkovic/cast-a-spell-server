package cast.a.spell.session;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SessionController {
    @MessageMapping("/room/*")
    @SendTo("/topic/room/*")
    public String traffic(String input) {
        return "abc";
    }

}