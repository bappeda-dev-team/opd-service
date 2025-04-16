package kk.kertaskerja.opdservice;

import kk.kertaskerja.opdservice.config.KertasKerjaProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final KertasKerjaProperties kertasKerjaProperties;

    public HomeController(KertasKerjaProperties kertasKerjaProperties) {
        this.kertasKerjaProperties = kertasKerjaProperties;
    }
    @GetMapping("/")
    public String status() {
        return kertasKerjaProperties.getStatusMessage();
    }
}
