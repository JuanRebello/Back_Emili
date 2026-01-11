package Emili.BackEmili.status;

import org.springframework.data.jpa.repository.JpaRepository;
import Emili.BackEmili.requisicao.RequisicaoModel;

public interface RequisicaoStatusRepository extends JpaRepository<RequisicaoStatus, Long> {
    RequisicaoStatus findTopByRequisicaoOrderByDataStatusDesc(RequisicaoModel requisicao);
}
