package kk.kertaskerja.opdservice.web;

import kk.kertaskerja.opdservice.domain.OpdNotFoundException;
import kk.kertaskerja.opdservice.domain.OpdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OpdController.class)
public class OpdControllerTests {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private OpdService opdService;

    @Test
    void whenGetOpdNotExistingThenReturnNotFound() throws Exception {
        String kodeOpd = "5.01.5.05.0.00.01.0000";
        given(opdService.viewOpdDetail(kodeOpd))
                .willThrow(OpdNotFoundException.class);

        mvc.perform(get("/opd" + kodeOpd))
                .andExpect(status().isNotFound());
    }
}
