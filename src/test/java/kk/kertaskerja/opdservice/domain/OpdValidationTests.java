package kk.kertaskerja.opdservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OpdValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSuccess() {
        var opd = Opd.of("5.01.5.05.0.00.02.0000", "OPD TEST", "");
        Set<ConstraintViolation<Opd>> violations = validator.validate(opd);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenKodeOpdDefinedButNotCorrectThenValidationFailure() {
        var opd = Opd.of("5", "OPD TEST", "");
        Set<ConstraintViolation<Opd>> violations = validator.validate(opd);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Format kode tidak valid");
    }

    @Test
    void whenKodeOpdNotDefinedThenValidationFailure() {
        var opd = Opd.of("", "Test OPD", "");
        Set<ConstraintViolation<Opd>> violations = validator.validate(opd);
        assertThat(violations).hasSize(2);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        assertThat(constraintViolationMessages)
                .contains("Format kode tidak valid")
                .contains("Kode OPD wajib terisi");
    }

    @Test
    void whenNamaOpdNotDefinedThenValidationFailure() {
        var opd = Opd.of("5.01.5.05.0.00.02.0000", "", "");
        Set<ConstraintViolation<Opd>> violations = validator.validate(opd);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Nama OPD wajib terisi");
    }
}
