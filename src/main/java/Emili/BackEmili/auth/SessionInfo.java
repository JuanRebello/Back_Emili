package Emili.BackEmili.auth;

import Emili.BackEmili.usuario.Role;

import java.time.LocalDateTime;

public class SessionInfo {
    private final Long userId;
    private final Role role;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;

    public SessionInfo(Long userId, Role role, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.userId = userId;
        this.role = role;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public Long getUserId() { return userId; }
    public Role getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
}
