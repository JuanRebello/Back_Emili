package Emili.BackEmili.requisicao;

import Emili.BackEmili.status.StatusTipo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequisicaoUpdateDTO {
    
    @NotNull(message = "status é obrigatório")
    private StatusTipo status;
    
    
}
