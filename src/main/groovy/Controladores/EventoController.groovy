package Controladores

import Modelos.Evento
import Modelos.Tarea
import Servicios.EventoService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

import java.time.LocalDate

@RestController
class EventoController extends ApiControllerBase {

    @Autowired
    private EventoService service

    @PostMapping("/evento/{usuario}")
    @ResponseStatus(HttpStatus.CREATED)
    Evento crearEvento(@RequestBody Evento evento, @PathVariable String usuario) {
        if (usuario.isAllWhitespace()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        }
        return service.crearEvento(evento, usuario)
    }

    @PutMapping("/evento/{usuario}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Evento editarEvento(@RequestBody Evento evento,@PathVariable String usuario) {
        if (usuario.isAllWhitespace()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        }
        return service.editarEvento(evento, usuario)
    }

    @DeleteMapping("/evento/{evento}&{nombreFecha}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void borrarEvento(@PathVariable ObjectId evento, @PathVariable String nombreFecha) {
        if (!nombreFecha && !evento) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        }
        if (nombreFecha.isAllWhitespace() && evento)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        service.borrarEvento(evento, nombreFecha)
    }


    @PutMapping("/evento/tarea")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Evento agregarTarea(@RequestBody Evento evento, @RequestBody Tarea tarea) {
        return service.agregarTarea(evento, tarea)
    }

    @PutMapping("/evento/borrarTarea")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Evento borrarTarea(@RequestBody Evento evento, @RequestBody Tarea tarea) {
        return service.borrarTarea(evento, tarea)
    }

    @GetMapping("/evento/nombre/{nombre}")
    @ResponseStatus(HttpStatus.FOUND)
    Evento getEvento(@PathVariable String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEvento(nombre)
    }

    @GetMapping("/evento/btfechas/{desde}&{hasta}")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByFechas(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta) {
        return service.getEventosByFechas(desde, hasta)
    }

    @GetMapping("/evento/btfechasyNombre/{desde}&{hasta}&{nombre}")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByFechasAndNombre(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta,@PathVariable String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEventosByFechasAndNombre(desde, hasta, nombre)
    }

    @GetMapping("/evento/btfechasyEquipo/{desde}&{hasta}&{equipo}")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByFechasAndEquipo(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desde, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate hasta,@PathVariable String equipo) {
        if (equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEventosByFechasAndEquipo(desde, hasta, equipo)
    }

    @GetMapping("/evento/equipo/{equipo}")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByEquipo(@PathVariable String equipo) {
        if (equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEventosByEquipo(equipo)
    }

    @GetMapping("/evento/{nombre}")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByNombre(@PathVariable String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEventosByNombre(nombre)
    }

}
