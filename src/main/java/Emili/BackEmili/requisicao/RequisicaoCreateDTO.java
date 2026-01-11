package Emili.BackEmili.requisicao;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequisicaoCreateDTO {

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotEmpty(message = "Deve haver pelo menos um modal")
    private Set<Modal> modais;

    
}
