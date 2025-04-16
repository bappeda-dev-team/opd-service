package kk.kertaskerja.opdservice.domain;

public class OpdAlreadyExistsException extends RuntimeException {
    public OpdAlreadyExistsException(String kodeOpd) {
        super("OPD dengan kode " + kodeOpd + " sudah ada");
    }
}
