package kk.kertaskerja.opdservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import kk.kertaskerja.opdservice.config.SecurityConfig;
import kk.kertaskerja.opdservice.domain.Opd;
import kk.kertaskerja.opdservice.domain.OpdNotFoundException;
import kk.kertaskerja.opdservice.domain.OpdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OpdController.class)
@Import(SecurityConfig.class)
public class OpdControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    JwtDecoder jwtDecoder;

    @MockitoBean
    private OpdService opdService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenGetOpdNotExistingAndNotAuthenticatedThenReturnNotFound() throws Exception {
        String kodeOpd = "5.01.5.05.0.00.01.0000";
        given(opdService.viewOpdDetail(kodeOpd))
                .willThrow(OpdNotFoundException.class);

        mvc.perform(get("/opds/" + kodeOpd))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetOpdExistAndNotAuthenticatedThenShouldReturn200() throws Exception {
        var kodeOpd = "5.01.5.05.0.00.01.0000";
        var expectedOpd = Opd.of(kodeOpd, "Contoh OPD", "");
        given(opdService.viewOpdDetail(kodeOpd)).willReturn(expectedOpd);
        mvc.perform(get("/opds/" + kodeOpd))
                .andExpect(status().isOk())
        ;
    }

    @Test
    void whenDeleteOpdWithEmployeeRoleThenShouldReturn204() throws Exception {
        var kodeOpd = "5.01.5.05.0.00.01.0000";
        mvc.perform(MockMvcRequestBuilders.delete("/opds/" + kodeOpd)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteOpdWithCustomerRoleThenShouldReturn204() throws Exception {
        var kodeOpd = "5.01.5.05.0.00.01.0000";
        mvc.perform(MockMvcRequestBuilders.delete("/opds/" + kodeOpd)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteOpdNotAuthenticatedThenShouldReturn401() throws Exception {
        var kodeOpd = "5.01.5.05.0.00.01.0000";
        mvc.perform(MockMvcRequestBuilders.delete("/opds/" + kodeOpd))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenPostOpdWithEmployeeRoleThenShouldReturn201() throws Exception {
        var kodeOpd = "5.01.5.05.0.00.01.0000";
        var opdToCreate = Opd.of(kodeOpd, "Contoh OPD", "");
        mvc.perform(MockMvcRequestBuilders.post("/opds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(opdToCreate))
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(status().isCreated());
    }

    @Test
    void whenPostOpdWithCustomerRoleThenShouldReturn201() throws Exception {
            var kodeOpd = "5.01.5.05.0.00.01.0000";
            var opdToCreate = Opd.of(kodeOpd, "Contoh OPD", "");
            mvc.perform(MockMvcRequestBuilders.post("/opds")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(opdToCreate))
                            .with(SecurityMockMvcRequestPostProcessors.jwt()
                                    .authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                    .andExpect(status().isCreated());
    }

    @Test
    void whenPostOpdNotAuthenticatedThenShouldReturn401() throws Exception {
            var kodeOpd = "5.01.5.05.0.00.01.0000";
            var opdToCreate = Opd.of(kodeOpd, "Contoh OPD", "");
            mvc.perform(MockMvcRequestBuilders.post("/opds")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(opdToCreate)))
                    .andExpect(status().isUnauthorized());
    }

    @Test
    void whenPutOpdWithEmployeeRoleThenShouldReturn200() throws Exception {
        var kodeOpd = "5.01.5.05.0.00.01.0000";
        var opdToCreate = Opd.of(kodeOpd, "Contoh OPD", "");
        given(opdService.addOpd(opdToCreate)).willReturn(opdToCreate);

        mvc.perform(MockMvcRequestBuilders.put("/opds/" + kodeOpd)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(opdToCreate))
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutOpdWithCustomerRoleThenShouldReturn403() throws Exception {
        var kodeOpd = "5.01.5.05.0.00.01.0000";
        var opdToCreate = Opd.of(kodeOpd, "Contoh OPD", "");
        given(opdService.addOpd(opdToCreate)).willReturn(opdToCreate);

        mvc.perform(MockMvcRequestBuilders.put("/opds/" + kodeOpd)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opdToCreate))
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutOpdNotAuthenticatedThenShouldReturn401() throws Exception {
        var kodeOpd = "5.01.5.05.0.00.01.0000";
        var opdToCreate = Opd.of(kodeOpd, "Contoh OPD", "");
        given(opdService.addOpd(opdToCreate)).willReturn(opdToCreate);

        mvc.perform(MockMvcRequestBuilders.put("/opds/" + kodeOpd)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opdToCreate)))
                .andExpect(status().isUnauthorized());
    }
}
