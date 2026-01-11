package Emili.BackEmili.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import Emili.BackEmili.requisicao.RequisicaoModel;
import java.util.List;

public interface RequisicaoStatusRepository extends JpaRepository<RequisicaoStatus, Long> {
    RequisicaoStatus findTopByRequisicaoOrderByDataStatusDesc(RequisicaoModel requisicao);

    @Query("SELECT rs FROM RequisicaoStatus rs WHERE rs.dataStatus = (SELECT MAX(r2.dataStatus) FROM RequisicaoStatus r2 WHERE r2.requisicao = rs.requisicao)")
    List<RequisicaoStatus> findAllLatestStatuses();
}
