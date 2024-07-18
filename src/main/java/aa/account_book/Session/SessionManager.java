package aa.account_book.Session;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final Map<String, HttpSession> sessionStore = new ConcurrentHashMap<>();

    public void addSession(String userId, HttpSession session) {
        sessionStore.put(userId, session);
    }

    public void removeSession(String userId) {
        sessionStore.remove(userId);
    }

    public List<String> getLoginUserIdList() {
        return sessionStore.keySet().stream().toList();
    }
}
