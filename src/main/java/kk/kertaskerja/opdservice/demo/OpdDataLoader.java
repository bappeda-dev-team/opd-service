package kk.kertaskerja.opdservice.demo;

import kk.kertaskerja.opdservice.domain.Opd;
import kk.kertaskerja.opdservice.domain.OpdRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("testdata")
public class OpdDataLoader {
    private final OpdRepository opdRepository;

    public OpdDataLoader(OpdRepository opdRepository) {
        this.opdRepository = opdRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadOpdTestData() {
        opdRepository.deleteAll();
        var opd1 = Opd.of("5.01.5.05.0.00.02.0000",
                "Badan Perencanaan, Penelitian dan Pengembangan Daerah",
                "");
        var opd2 = Opd.of("4.01.0.00.0.00.01.0000",
                "Sekretariat Daerah",
                "");
        var opd21 = Opd.of("4.01.0.00.0.00.01.0002",
                "Bagian Organisasi",
                "4.01.0.00.0.00.01.0000");
        opdRepository.saveAll(List.of(opd1, opd2, opd21));
    }
}
