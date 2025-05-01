package kk.kertaskerja.opdservice.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OpdRepository extends CrudRepository<Opd, Long> {
    Iterable<Opd> findAllByOrderByNamaOpdAsc();
    Optional<Opd> findByKodeOpd(String kodeOpd);
    boolean existsByKodeOpd(String kodeOpd);

    @Modifying
    @Transactional
    @Query("delete from opd where kode_opd = :kodeOpd")
    void deleteByKodeOpd(String kodeOpd);
}
