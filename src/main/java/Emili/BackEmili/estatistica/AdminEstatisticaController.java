package Emili.BackEmili.estatistica;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/estatisticas")
public class AdminEstatisticaController {

    private final EstatisticaService estatisticaService;
    private final Emili.BackEmili.auth.SessionService sessionService;

    public AdminEstatisticaController(EstatisticaService estatisticaService,
                                      Emili.BackEmili.auth.SessionService sessionService) {
        this.estatisticaService = estatisticaService;
        this.sessionService = sessionService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(@RequestHeader("X-Session-Id") String token) {
        try {
            sessionService.requireAdmin(token);
            DashboardStatsDTO dto = estatisticaService.obterDashboard();
            return ResponseEntity.ok(dto);
        } catch (SecurityException | IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    // Endpoint de relatório simples (pode ser expandido futuramente com filtros de período)
    @GetMapping("/relatorio")
    public ResponseEntity<?> relatorio(@RequestHeader("X-Session-Id") String token) {
        try {
            sessionService.requireAdmin(token);
            DashboardStatsDTO dto = estatisticaService.obterDashboard();
            return ResponseEntity.ok(dto);
        } catch (SecurityException | IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }
}
