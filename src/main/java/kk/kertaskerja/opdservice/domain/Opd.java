package kk.kertaskerja.opdservice.domain;

import jakarta.annotation.Nullable;
import org.springframework.data.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;

public record Opd(
        @Id Long id,

        @NotBlank(message = "Kode OPD wajib terisi")
        @Pattern(
                regexp = "^\\d\\.\\d{2}\\.\\d\\.\\d{2}\\.\\d\\.\\d{2}\\.\\d{2}\\.\\d{4}$",
                message = "Format kode tidak valid"
        )
        String kodeOpd,

        @NotBlank(message = "Nama OPD wajib terisi")
        String namaOpd,

        @Nullable
        String kodeOpdParent,

        @Version
        int version,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        @CreatedBy
        String createdBy,

        @LastModifiedBy
        String lastModifiedBy
) {
    public static Opd of(String kodeOpd, String namaOpd, String kodeOpdParent) {
        return new Opd(null, kodeOpd, namaOpd, kodeOpdParent, 0, null, null, "", "");
    }

    public boolean isSubOpd() {
        return kodeOpdParent != null && !kodeOpdParent.isBlank();
    }

    public boolean isParentOpd() {
        return kodeOpdParent == null || kodeOpdParent.isEmpty();
    }
}
