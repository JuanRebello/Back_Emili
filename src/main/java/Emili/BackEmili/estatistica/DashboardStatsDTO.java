package Emili.BackEmili.estatistica;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private long totalSolicitacoes;
    private long totalAereo;
    private long totalMaritimo;
    private long totalTerrestre;

    private long pendentes;     // Assumimos pendentes = ABERTA + EM_ANALISE
    private long emTransito;
    private long finalizadas;
    private long canceladas;
}
