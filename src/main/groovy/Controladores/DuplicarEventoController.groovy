package Controladores

import Modelos.Evento
import Servicios.TareaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

import java.time.LocalDate

@RestController
class DuplicarEventoController extends ApiControllerBase {

    @Autowired
    private TareaService service

    @PostMapping("/duplicarEvento/duplicar/{nombreFecha}&{asignado}&{equipo}&{fecha}")
    @ResponseStatus(HttpStatus.CREATED)
    Evento duplicarEvento(@PathVariable String nombreFecha, @PathVariable String asignado, @PathVariable String equipo, @PathVariable  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fecha) {
        if (nombreFecha.isAllWhitespace() || asignado.isAllWhitespace() || equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.duplicar(nombreFecha, asignado, equipo, fecha)
    }

}
