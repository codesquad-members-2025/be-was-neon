package webserver.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

public class SessionCleaner {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private static final Logger log = LoggerFactory.getLogger(SessionCleaner.class);

    public void start() {
        scheduler.scheduleAtFixedRate(this::cleanUp, 0, 5, TimeUnit.MINUTES);
    }

    public void cleanUp() {
        Map<String, Session> sessions = sessionManager.getSessions();
        Iterator<Map.Entry<String, Session>> it = sessions.entrySet().iterator();

        int removedSessions = 0;

        while (it.hasNext()) {
            Map.Entry<String, Session> entry = it.next();
            Session session = entry.getValue();

            if (session.isExpired()) {
                it.remove();
                removedSessions ++;
            }
        }
        //제거된 세션 수가 있으면 로그를 출력
        if (removedSessions > 0) {
            log.info("[SessionCleaner] Removed " + sessionManager + " expired sessions,");
        }
    }
}
