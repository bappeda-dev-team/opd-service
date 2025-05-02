package kk.kertaskerja.opdservice.domain;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OpdService {
    private final OpdRepository opdRepository;

    public OpdService(OpdRepository opdRepository) {
        this.opdRepository = opdRepository;
    }

    public Iterable<Opd> viewOpdList() {
        return opdRepository.findAllByOrderByNamaOpdAsc();
    }

    public Opd viewOpdDetail(String kodeOpd) {
        return opdRepository.findByKodeOpd(kodeOpd).orElseThrow(() -> new OpdNotFoundException(kodeOpd));
    }

    public Opd addOpd(Opd opd) {
        if (opdRepository.existsByKodeOpd(opd.kodeOpd())) {
            throw new OpdAlreadyExistsException(opd.kodeOpd());
        }

        // Normalize kodeOpdParent to null if blank
        String normalizedParent = Optional.ofNullable(opd.kodeOpdParent())
                .filter(s -> !s.isBlank()).orElse(null);

        // Check parent exists if kodeOpdParent is not null
        if (normalizedParent != null && !opdRepository.existsByKodeOpd(normalizedParent)) {
            throw new OpdParentNotFoundException(normalizedParent);
        }

        // Create a new Opd with kodeOpdParent set to null if blank
        Opd normalizedOpd = Opd.of(opd.kodeOpd(), opd.namaOpd(), normalizedParent);

        return opdRepository.save(normalizedOpd);
    }

    public void removeOpd(String kodeOpd) {
        opdRepository.deleteByKodeOpd(kodeOpd);
    }

    public Opd editOpdDetails(String kodeOpd, Opd opd) {
        return opdRepository.findByKodeOpd(kodeOpd).map(existingOpd -> {
            var opdToUpdate = new Opd(
                    existingOpd.id(),
                    opd.kodeOpd(),
                    opd.namaOpd(),
                    opd.kodeOpdParent(), existingOpd.version(),
                    existingOpd.createdDate(), existingOpd.lastModifiedDate(),
                    existingOpd.createdBy(), existingOpd.lastModifiedBy());
            return opdRepository.save(opdToUpdate);
        }).orElseGet(() -> addOpd(opd));
    }
}
