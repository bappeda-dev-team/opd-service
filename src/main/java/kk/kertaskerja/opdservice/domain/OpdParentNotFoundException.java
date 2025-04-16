package kk.kertaskerja.opdservice.domain;

public class OpdParentNotFoundException extends RuntimeException {
    public OpdParentNotFoundException(String kodeOpdParent) {
        super("Parent OPD dengan kode " + kodeOpdParent + " tidak ditemukan");
    }
}
