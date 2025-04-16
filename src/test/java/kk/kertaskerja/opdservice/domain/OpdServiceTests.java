package kk.kertaskerja.opdservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class OpdServiceTests {
    @Mock
    private OpdRepository opdRepository;

    @InjectMocks
    private OpdService opdService;

    @Test
    void whenOpdToCreateAlreadyExistsThenThrowsException() {
        var kodeOpd = "5.01.5.05.0.00.02.0000";
        var opdToCreate = Opd.of(kodeOpd, "Test OPD", "");
        when(opdRepository.existsByKodeOpd(kodeOpd)).thenReturn(true);
        assertThatThrownBy(() -> opdService.addOpd(opdToCreate))
                .isInstanceOf(OpdAlreadyExistsException.class)
                .hasMessage("OPD dengan kode " + kodeOpd + " sudah ada");
    }

    @Test
    void whenKodeOpdToReadDoesNotExistsThenThrowsException() {
        var kodeOpd = "5.01.5.05.0.00.02.0000";
        when(opdRepository.findByKodeOpd(kodeOpd)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> opdService.viewOpdDetail(kodeOpd))
                .isInstanceOf(OpdNotFoundException.class)
                .hasMessage("Kode OPD " + kodeOpd + " tidak ditemukan");
    }

    @Test
    void whenKodeOpdParentExistsButNotFoundThenThrowsException() {
        var kodeOpdParent = "1.01.0.00.0.00.01.0000";
        var kodeOpdSub = "1.01.0.00.0.00.01.0001";
        var subOpdToCreate = Opd.of(kodeOpdSub, "Test OPD", kodeOpdParent);

        // When check if opd is duplicated (exists)
        // return false, we want new opd to be created
        when(opdRepository.existsByKodeOpd(kodeOpdSub)).thenReturn(false);

        assertThatThrownBy(() -> opdService.addOpd(subOpdToCreate))
                .isInstanceOf(OpdParentNotFoundException.class)
                .hasMessage("Parent OPD dengan kode " + kodeOpdParent + " tidak ditemukan");
    }

    @Test
    void whenKodeOpdParentBlankOpdIsCreatedAndIsParentAndNotSubOpd() {
        // Given
        var kodeOpd = "5.01.5.05.0.00.02.0000";
        var opdToCreate = Opd.of(kodeOpd, "Test OPD", "");

        // When check if opd is duplicated (exists)
        // return false, we want new opd to be created
        when(opdRepository.existsByKodeOpd(kodeOpd)).thenReturn(false);

        // When saving, just return the same object back
        when(opdRepository.save(any(Opd.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Call
        Opd saveOpd = opdService.addOpd(opdToCreate);

        // Then
        assertThat(saveOpd.kodeOpd()).isEqualTo("5.01.5.05.0.00.02.0000");
        assertThat(saveOpd.isParentOpd()).isTrue();
        assertThat(saveOpd.isSubOpd()).isFalse();
    }
}
