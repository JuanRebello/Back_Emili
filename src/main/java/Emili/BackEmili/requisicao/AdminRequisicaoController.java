package Emili.BackEmili.requisicao;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/requisicoes")
public class AdminRequisicaoController {

    private final RequisicaoService requisicaoService;
    private final RequisicaoRepository requisicaoRepository;
    private final Emili.BackEmili.auth.SessionService sessionService;

    public AdminRequisicaoController(RequisicaoService requisicaoService,
                                     RequisicaoRepository requisicaoRepository,
                                     Emili.BackEmili.auth.SessionService sessionService) {
        this.requisicaoService = requisicaoService;
        this.requisicaoRepository = requisicaoRepository;
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestHeader("X-Session-Id") String token) {
        try {
            sessionService.requireAdmin(token);
            return ResponseEntity.ok(requisicaoService.listarRequisicoes());
        } catch (SecurityException | IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterPorId(@RequestHeader("X-Session-Id") String token,
                                        @PathVariable Long id) {
        try {
            sessionService.requireAdmin(token);
            RequisicaoResponseDTO dto = requisicaoService.listarRequisicaoPorId(id);
            return ResponseEntity.ok(dto);
        } catch (SecurityException | IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@RequestHeader("X-Session-Id") String token,
                                             @PathVariable Long id,
                                             @RequestBody @Valid RequisicaoUpdateDTO dto) {
        try {
            sessionService.requireAdmin(token);
            RequisicaoResponseDTO atualizado = requisicaoService.atualizarRequisicaoPorId(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (SecurityException | IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    @PostMapping("/{id}/decidir-modal")
    public ResponseEntity<?> decidirModal(@RequestHeader("X-Session-Id") String token,
                                          @PathVariable Long id,
                                          @RequestBody @Valid AdminDecideModalDTO dto) {
        try {
            sessionService.requireAdmin(token);
            Long requisicaoId = java.util.Objects.requireNonNull(id, "ID da requisição é obrigatório");
            RequisicaoModel requisicao = requisicaoRepository.findById(requisicaoId)
                    .orElseThrow(() -> new IllegalArgumentException("Requisição não encontrada: " + requisicaoId));

            Modal escolhido = dto.getModal();
            if (requisicao.getModais() == null || !requisicao.getModais().contains(escolhido)) {
                return ResponseEntity.badRequest().body("Modal escolhido não está entre os sugeridos pelo usuário.");
            }

            // Observação: Persistência da decisão do modal pode exigir um novo campo na entidade
            // (por exemplo, 'modalAprovado'). Mantemos aqui apenas a validação e confirmação.
            return ResponseEntity.ok("Modal aprovado: " + escolhido.name());
        } catch (SecurityException | IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }
}
