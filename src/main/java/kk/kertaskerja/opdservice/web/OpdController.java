package kk.kertaskerja.opdservice.web;

import jakarta.validation.Valid;
import kk.kertaskerja.opdservice.domain.Opd;
import kk.kertaskerja.opdservice.domain.OpdService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("opd")
public class OpdController {
    private final OpdService opdService;

    public OpdController(OpdService opdService) {
        this.opdService = opdService;
    }

    @GetMapping
    public Iterable<Opd> get() {
        return opdService.viewOpdList();
    }

    @GetMapping("{kodeOpd}")
    public Opd getByKodeOpd(@PathVariable String kodeOpd) {
        return opdService.viewOpdDetail(kodeOpd);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Opd create(@Valid @RequestBody Opd opd) {
        return opdService.addOpd(opd);
    }

    @PutMapping("{kodeOpd}")
    public Opd update(@PathVariable String kodeOpd, @Valid @RequestBody Opd opd) {
        return opdService.editOpdDetails(kodeOpd, opd);
    }

    @DeleteMapping("{kodeOpd}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String kodeOpd) {
        opdService.removeOpd(kodeOpd);
    }

}
