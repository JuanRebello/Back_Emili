package Emili.BackEmili.estatistica;


import Emili.BackEmili.requisicao.RequisicaoModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_estatistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaService {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstatistica;

    @OneToOne
    @JoinColumn(name = "id_requisicao")
    private RequisicaoModel requisicao;

    private Long tempoProcessamento;


}
