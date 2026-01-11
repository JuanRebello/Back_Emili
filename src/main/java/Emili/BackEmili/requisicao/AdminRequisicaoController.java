package Emili.BackEmili.requisicao;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/requisicoes")
public class AdminRequisicaoController {

    private final RequisicaoService requisicaoService;
    private final RequisicaoRepository requisicaoRepository;

    public AdminRequisicaoController(RequisicaoService requisicaoService,
                                     RequisicaoRepository requisicaoRepository) {
        this.requisicaoService = requisicaoService;
        this.requisicaoRepository = requisicaoRepository;
    }

    @GetMapping
    public List<RequisicaoResponseDTO> listar() {
        return requisicaoService.listarRequisicoes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequisicaoResponseDTO> obterPorId(@PathVariable Long id) {
        RequisicaoResponseDTO dto = requisicaoService.listarRequisicaoPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RequisicaoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                                 @RequestBody @Valid RequisicaoUpdateDTO dto) {
        RequisicaoResponseDTO atualizado = requisicaoService.atualizarRequisicaoPorId(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PostMapping("/{id}/decidir-modal")
    public ResponseEntity<?> decidirModal(@PathVariable Long id,
                                          @RequestBody @Valid AdminDecideModalDTO dto) {
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
    }
}
