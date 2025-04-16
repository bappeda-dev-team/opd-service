package kk.kertaskerja.opdservice;

import kk.kertaskerja.opdservice.domain.Opd;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
class OpdServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenPostRequestThenOpdCreated() {
        var expectedOpd = Opd.of("5.01.5.05.0.00.02.0000", "Test OPD", "");

        webTestClient
                .post()
                .uri("/opd")
                .bodyValue(expectedOpd)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Opd.class).value( actualOpd -> {
                    assertThat(actualOpd).isNotNull();
                    assertThat(actualOpd.kodeOpd())
                            .isEqualTo(expectedOpd.kodeOpd());
                    assertThat(actualOpd.isParentOpd()).isTrue();
                    assertThat(actualOpd.isSubOpd()).isFalse();
                });
    }

}
