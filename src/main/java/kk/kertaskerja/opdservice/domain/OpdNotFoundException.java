package kk.kertaskerja.opdservice.domain;

public class OpdNotFoundException extends RuntimeException {
    public OpdNotFoundException(String kodeOpd) {
        super("Kode OPD " + kodeOpd + " tidak ditemukan");
    }
}
