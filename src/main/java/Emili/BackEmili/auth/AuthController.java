package Emili.BackEmili.auth;

import Emili.BackEmili.usuario.UsuarioModel;
import Emili.BackEmili.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          SessionService sessionService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        UsuarioModel user = usuarioRepository.findByEmail(dto.getEmail())
                .orElse(null);
        if (user == null || user.getPasswordHash() == null ||
                !passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }

        String token = sessionService.createSession(user);
        LoginResponseDTO resp = new LoginResponseDTO();
        resp.setToken(token);
        resp.setUserId(user.getId());
        resp.setRole(user.getRole());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "X-Session-Id", required = false) String token) {
        sessionService.logout(token);
        return ResponseEntity.ok("Logout efetuado");
    }
}
