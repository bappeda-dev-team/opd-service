package kk.kertaskerja.opdservice.common;

import java.time.Instant;

public record HealthStatus(
        String status,
        Instant timestamp
) {}
