package Emili.BackEmili.auth;

import Emili.BackEmili.usuario.Role;
import Emili.BackEmili.usuario.UsuarioModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();
    private static final long DEFAULT_TTL_MINUTES = 120; // 2h

    public String createSession(UsuarioModel user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp = now.plusMinutes(DEFAULT_TTL_MINUTES);
        SessionInfo info = new SessionInfo(user.getId(), user.getRole(), now, exp);
        String token = UUID.randomUUID().toString();
        sessions.put(token, info);
        return token;
    }

    public SessionInfo getSessionOrThrow(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token de sessão ausente");
        }
        SessionInfo info = sessions.get(token);
        if (info == null) {
            throw new IllegalArgumentException("Sessão inválida");
        }
        if (info.getExpiresAt().isBefore(LocalDateTime.now())) {
            sessions.remove(token);
            throw new IllegalArgumentException("Sessão expirada");
        }
        return info;
    }

    public SessionInfo requireUser(String token) {
        return getSessionOrThrow(token);
    }

    public SessionInfo requireAdmin(String token) {
        SessionInfo info = getSessionOrThrow(token);
        if (info.getRole() != Role.ADMIN) {
            throw new SecurityException("Acesso restrito a administradores");
        }
        return info;
    }

    public void logout(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}
