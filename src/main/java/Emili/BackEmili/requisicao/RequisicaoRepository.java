package Emili.BackEmili.requisicao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RequisicaoRepository extends JpaRepository<RequisicaoModel, Long> {
	@Query("SELECT COUNT(DISTINCT r) FROM RequisicaoModel r JOIN r.modais m WHERE m = :modal")
	long countByModal(@Param("modal") Modal modal);
}
