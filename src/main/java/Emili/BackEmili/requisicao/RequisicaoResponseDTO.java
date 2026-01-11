package Emili.BackEmili.requisicao;

import java.time.LocalDateTime;
import java.util.Set;
import Emili.BackEmili.status.StatusTipo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequisicaoResponseDTO {

    private Long idRequisicao;
    private Long idUsuario;
    private LocalDateTime dataCriacao;
    private String descricao;
    private Set<Modal> modais;
    private StatusTipo status;
}
