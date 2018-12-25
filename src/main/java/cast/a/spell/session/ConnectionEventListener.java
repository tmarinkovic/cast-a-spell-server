package cast.a.spell.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class ConnectionEventListener implements ApplicationListener {

    private final SessionTracker sessionTracker;

    @Autowired
    public ConnectionEventListener(SessionTracker sessionTracker) {
        this.sessionTracker = sessionTracker;
    }

    @EventListener(SessionConnectEvent.class)
    public void handleWebsocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        sessionTracker.addSession(sha);
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        sessionTracker.removeSession(sha.getSessionId());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
    }
}
