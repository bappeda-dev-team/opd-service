package kk.kertaskerja.opdservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import kk.kertaskerja.opdservice.domain.Opd;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("integration")
@Testcontainers
class OpdServiceApplicationTests {
    private static KeycloakToken bjornToken;
    private static KeycloakToken isabelleToken;

    @Autowired
    private WebTestClient webTestClient;

    @Container
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:19.0")
            .withRealmImportFile("test-realm-config.json");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "/realms/Kertaskerja");
    }

    @BeforeAll
    static void generateAccessToken() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl() + "/realms/Kertaskerja/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        isabelleToken = authenticateWith("isabelle", "password", webClient);
        bjornToken = authenticateWith("bjorn", "password", webClient);
    }

    private static KeycloakToken authenticateWith(String username, String password, WebClient webClient) {
        return webClient.post().body(
                BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "kertaskerja-test")
                        .with("username", username)
                        .with("password", password))
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }

    private record KeycloakToken(String access_token) {
        @JsonCreator
        private KeycloakToken(@JsonProperty("access_token") final String access_token) {
            this.access_token = access_token;
        }
    }


    @Test
    void whenPostRequestThenOpdCreated() {
        var expectedOpd = Opd.of("5.01.5.05.0.00.02.0000", "Test OPD", "");

        webTestClient
                .post()
                .uri("/opds")
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

    @Test
    void whenGetOpdNotExistingThenReturnNotFound() {
        webTestClient
                .get()
                .uri("/opds/1234")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo("404")
                .jsonPath("$.message").isEqualTo("OPD dengan kode 1234 tidak ditemukan");
    }

    @Test
    void whenPostOpdAlreadyExistsThenReturnUnprocessableEntity() {
        var opdToCreate = Opd.of("5.01.5.05.0.00.01.0000", "Test OPD", "");

        webTestClient
                .post()
                .uri("/opds")
                .bodyValue(opdToCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .post()
                .uri("/opds")
                .bodyValue(opdToCreate)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("$.status").isEqualTo("422")
                .jsonPath("$.message").isEqualTo("OPD dengan kode " + opdToCreate.kodeOpd() + " sudah ada");
    }

}
