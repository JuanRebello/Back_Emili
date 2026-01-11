package Emili.BackEmili.requisicao;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

import jakarta.validation.Valid;
 


@RestController
@RequestMapping("/requisicoes")
public class RequisicaoController {


    private final RequisicaoService requisicaoService;
    private final Emili.BackEmili.auth.SessionService sessionService;

    public RequisicaoController(RequisicaoService requisicaoService,
                                Emili.BackEmili.auth.SessionService sessionService) {
        this.requisicaoService = requisicaoService;
        this.sessionService = sessionService;
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarRequisicao(@RequestHeader("X-Session-Id") String token,
                                             @Valid @RequestBody RequisicaoCreateDTO requisicaoCreateDTO) {
        try {
            var session = sessionService.requireUser(token);
            // Se não for admin, só permite criar para si mesmo
            if (session.getRole() != Emili.BackEmili.usuario.Role.ADMIN &&
                    !java.util.Objects.equals(session.getUserId(), requisicaoCreateDTO.getUsuarioId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Usuário não autorizado a criar requisição para outro usuário");
            }
            RequisicaoResponseDTO novaRequisicao = requisicaoService.criarRequisicao(requisicaoCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaRequisicao);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<RequisicaoResponseDTO>> listarRequisicoes(@RequestHeader("X-Session-Id") String token) {
        var session = sessionService.requireUser(token);
        // Admin vê todas; usuários veem apenas as próprias
        if (session.getRole() == Emili.BackEmili.usuario.Role.ADMIN) {
            return ResponseEntity.ok(requisicaoService.listarRequisicoes());
        } else {
            return ResponseEntity.ok(requisicaoService.listarRequisicoesDoUsuario(session.getUserId()));
        }
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<?> listarRequisicaoPorId(@RequestHeader("X-Session-Id") String token,
                                                   @PathVariable Long id) {
        var session = sessionService.requireUser(token);
        RequisicaoResponseDTO requisicaoPorId = requisicaoService.listarRequisicaoPorId(id);
        if (requisicaoPorId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requisição não encontrada");
        }
        // Se não for admin, só retorna se pertencer ao usuário
        if (session.getRole() != Emili.BackEmili.usuario.Role.ADMIN &&
                !java.util.Objects.equals(session.getUserId(), requisicaoPorId.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado a esta requisição");
        }
        return ResponseEntity.ok(requisicaoPorId);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarRequisicaoPorId(@RequestHeader("X-Session-Id") String token,
                                                         @PathVariable Long id){
        var session = sessionService.requireUser(token);
        try {
            // Se não for admin, valida propriedade da requisição
            RequisicaoResponseDTO req = requisicaoService.listarRequisicaoPorId(id);
            if (req == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Requisição com ID " + id + " não encontrada");
            }
            if (session.getRole() != Emili.BackEmili.usuario.Role.ADMIN &&
                    !java.util.Objects.equals(session.getUserId(), req.getIdUsuario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Usuário não autorizado a deletar esta requisição");
            }
            requisicaoService.deletarRequisicaoPorId(id);
            return ResponseEntity.ok("Requisição deletada com ID " + id + " com sucesso");
        } catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Requisição com ID " + id + " não encontrada");
        }
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<?> atualizarStatus(@RequestHeader("X-Session-Id") String token,
                                             @PathVariable Long id, @Valid @RequestBody RequisicaoUpdateDTO dto){
        var session = sessionService.requireUser(token);
        // Apenas admin pode atualizar status via rota pública; os demais devem usar rota admin
        if (session.getRole() != Emili.BackEmili.usuario.Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Apenas administradores podem alterar status nesta rota");
        }
        try {
            RequisicaoResponseDTO resposta = requisicaoService.atualizarRequisicaoPorId(id, dto);
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Requisição com ID " + id + " não encontrada");
        } catch (IllegalStateException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
        }
    }
}
