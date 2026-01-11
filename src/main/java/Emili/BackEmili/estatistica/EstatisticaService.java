package Emili.BackEmili.estatistica;

import Emili.BackEmili.requisicao.Modal;
import Emili.BackEmili.requisicao.RequisicaoRepository;
import Emili.BackEmili.status.RequisicaoStatus;
import Emili.BackEmili.status.RequisicaoStatusRepository;
import Emili.BackEmili.status.StatusTipo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstatisticaService {

	private final RequisicaoRepository requisicaoRepository;
	private final RequisicaoStatusRepository statusRepository;

	public EstatisticaService(RequisicaoRepository requisicaoRepository,
							  RequisicaoStatusRepository statusRepository) {
		this.requisicaoRepository = requisicaoRepository;
		this.statusRepository = statusRepository;
	}

	public DashboardStatsDTO obterDashboard() {
		long total = requisicaoRepository.count();
		long aereo = requisicaoRepository.countByModal(Modal.AEREO);
		long maritimo = requisicaoRepository.countByModal(Modal.MARITIMO);
		long terrestre = requisicaoRepository.countByModal(Modal.TERRESTRE);

		List<RequisicaoStatus> latest = statusRepository.findAllLatestStatuses();

		long pendentes = latest.stream()
				.filter(rs -> rs.getStatus() == StatusTipo.ABERTA || rs.getStatus() == StatusTipo.EM_ANALISE)
				.count();
		long emTransito = latest.stream().filter(rs -> rs.getStatus() == StatusTipo.EM_TRANSITO).count();
		long finalizadas = latest.stream().filter(rs -> rs.getStatus() == StatusTipo.FINALIZADA).count();
		long canceladas = latest.stream().filter(rs -> rs.getStatus() == StatusTipo.CANCELADA).count();

		return new DashboardStatsDTO(total, aereo, maritimo, terrestre, pendentes, emTransito, finalizadas, canceladas);
	}
}
