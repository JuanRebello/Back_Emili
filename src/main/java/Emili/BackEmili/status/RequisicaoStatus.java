package Emili.BackEmili.status;

import Emili.BackEmili.requisicao.RequisicaoModel;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_requisicao_status")
@Getter
@Setter
public class RequisicaoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RequisicaoModel requisicao;

    @Enumerated(EnumType.STRING)
    private StatusTipo status;

    private LocalDateTime dataStatus;
}
