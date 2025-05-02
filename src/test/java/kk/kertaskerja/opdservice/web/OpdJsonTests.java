package kk.kertaskerja.opdservice.web;

import kk.kertaskerja.opdservice.domain.Opd;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class OpdJsonTests {
    @Autowired
    private JacksonTester<Opd> json;

    @Test
    void testSerialize() throws Exception {
        var now = Instant.now();
        var opd = new Opd(394L, "1.01.0.00.0.00.01.0000", "Test OPD", "", 3, now, now, "isabelle", "isabelle");
        var jsonContent = json.write(opd);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(opd.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.kodeOpd")
                .isEqualTo(opd.kodeOpd());
        assertThat(jsonContent).extractingJsonPathStringValue("@.namaOpd")
                .isEqualTo(opd.namaOpd());
        assertThat(jsonContent).extractingJsonPathStringValue("@.kodeOpdParent")
                .isEqualTo(opd.kodeOpdParent());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
                .isEqualTo(opd.version());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate")
                .isEqualTo(opd.createdDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate")
                .isEqualTo(opd.lastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathBooleanValue("@.parentOpd")
                .isTrue();
        assertThat(jsonContent).extractingJsonPathBooleanValue("@.subOpd")
                .isFalse();
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdBy")
                .isEqualTo(opd.createdBy());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedBy")
                .isEqualTo(opd.lastModifiedBy());
    }
}
