package kk.kertaskerja.opdservice.domain;

import kk.kertaskerja.opdservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class OpdJdbcTests {
    @Autowired
    private OpdRepository opdRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findAllOpds() {
        var opd1 = Opd.of("5.01.5.05.0.00.01.0000", "Test OPD", "");
        var opd2 = Opd.of("1.01.2.05.3.03.03.0000", "Test OPD", "");
        jdbcAggregateTemplate.insert(opd1);
        jdbcAggregateTemplate.insert(opd2);

        Iterable<Opd> opds = opdRepository.findAll();

        assertThat(StreamSupport.stream(opds.spliterator(), true)
                .filter(opd ->
                    opd.kodeOpd().equals(opd1.kodeOpd()) || opd.kodeOpd().equals(opd2.kodeOpd()))
                .collect(Collectors.toList())).hasSize(2);
    }
}
