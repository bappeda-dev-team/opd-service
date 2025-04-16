package kk.kertaskerja.opdservice.domain;

public class OpdNotFoundException extends RuntimeException {
    public OpdNotFoundException(String kodeOpd) {
        super("OPD dengan kode " + kodeOpd + " tidak ditemukan");
    }
}
