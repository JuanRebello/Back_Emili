package Emili.BackEmili.usuario;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;


import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {


    private UsuarioService usuarioService;
    @Value("${setup.token:}")
    private String setupToken;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping("/boasVindas")
    public String boasVindas(){
        return "Essa é minha primeira mensagem nessa rota";
    }

    @PostMapping("/criar")
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioCreateDTO usuario){
        UsuarioResponseDTO novoUsuario = usuarioService.criarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuario(){
        List<UsuarioResponseDTO> listaUsuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(listaUsuarios);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarUsuarioPorId(@PathVariable Long id){
        UsuarioResponseDTO usuarioPorId = usuarioService.listarUsuarioPorId(id);
        if(usuarioPorId != null){
            return ResponseEntity.ok(usuarioPorId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Usuário não encontrado");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarUsuarioPorId(@PathVariable Long id){
        if (usuarioService.listarUsuarioPorId(id) != null){
            usuarioService.deletarUsuarioPorId(id);
            return ResponseEntity.ok("Usuário deletado com ID " + id + " com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Usuário com ID " + id + " não encontrado");

        }
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<?> alterarUsuarioPorId(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO usuarioAtualizado){
        UsuarioResponseDTO usuario = usuarioService.atualizarUsuarioPorId(id, usuarioAtualizado);
        if (usuario != null){
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Usuário com ID " + id + " não encontrado");
        }
    }

    // Endpoint de promoção para ADMIN usando token de setup (apenas para bootstrap via Postman)
    @PutMapping("/promover/{id}")
    public ResponseEntity<?> promoverUsuarioParaAdmin(@PathVariable Long id,
                                                      @RequestHeader(value = "X-Setup-Token", required = false) String token){
        if (setupToken == null || setupToken.isBlank()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Setup token não configurado no backend");
        }
        if (token == null || !token.equals(setupToken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Token de setup inválido");
        }
        UsuarioResponseDTO promovido = usuarioService.promoverParaAdmin(id);
        if (promovido == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário com ID " + id + " não encontrado");
        }
        return ResponseEntity.ok(promovido);
    }

}