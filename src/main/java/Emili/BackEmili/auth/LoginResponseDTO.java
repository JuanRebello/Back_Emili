package Emili.BackEmili.auth;

import Emili.BackEmili.usuario.Role;

public class LoginResponseDTO {
    private String token;
    private Long userId;
    private Role role;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
