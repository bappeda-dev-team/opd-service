package kk.kertaskerja.opdservice;

import kk.kertaskerja.opdservice.config.KertasKerjaProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import kk.kertaskerja.opdservice.common.HealthStatus;

import java.time.Instant;

@RestController
public class HomeController {
    private final KertasKerjaProperties kertasKerjaProperties;

    public HomeController(KertasKerjaProperties kertasKerjaProperties) {
        this.kertasKerjaProperties = kertasKerjaProperties;
    }
    @GetMapping("/")
    public HealthStatus status() {
        return new HealthStatus(
                kertasKerjaProperties.getStatusMessage(),
                Instant.now()
        );
    }
}
