package Controladores

import Modelos.Evento
import Modelos.Tarea
import Servicios.EventoService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

import java.time.LocalDate

@RestController
class EventoController extends ApiControllerBase {

    @Autowired
    private EventoService service

    @PostMapping("/crearEvento")
    @ResponseStatus(HttpStatus.CREATED)
    Evento crearEvento(@RequestBody String nombre, LocalDate fecha, String equipo, String usuario) {
        if (nombre.isAllWhitespace() || equipo.isAllWhitespace() || usuario.isAllWhitespace()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        }
        return service.crearEvento(nombre, fecha, equipo, usuario)
    }

    @PutMapping("/actualizarEvento")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Evento editarEvento(@RequestBody String nombre, LocalDate fecha, String equipo, String usuario) {
        if (nombre.isAllWhitespace() || equipo.isAllWhitespace() || usuario.isAllWhitespace()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        }
        return service.editarEvento(nombre, fecha, equipo, usuario)
    }

    @DeleteMapping("/borrarEvento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void borrarEvento(@RequestBody ObjectId evento, String nombreFecha) {
        if (nombreFecha.isAllWhitespace() && !evento) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        }
        service.borrarEvento(evento, nombreFecha)
    }


    @PutMapping("/evento/tarea")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Evento agregarTarea(Evento evento, Tarea tarea) {
        return service.agregarTarea(evento, tarea)
    }

    @PutMapping("/evento/borrarTarea")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Evento borrarTarea(Evento evento, Tarea tarea) {
        return service.borrarTarea(evento, tarea)
    }

    @GetMapping("/evento")
    @ResponseStatus(HttpStatus.FOUND)
    Evento getEvento(String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEvento(nombre)
    }

    @GetMapping("/evento/btfechas")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByFechas(LocalDate desde, LocalDate hasta) {
        return service.getEventosByFechas(desde, hasta)
    }

    @GetMapping("/evento/btfechasyNombre")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByFechasAndNombre(LocalDate desde, LocalDate hasta, String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEventosByFechasAndNombre(desde, hasta, nombre)
    }

    @GetMapping("/evento/btfechasyEquipo")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByFechasAndEquipo(LocalDate desde, LocalDate hasta, String equipo) {
        if (equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEventosByFechasAndEquipo(desde, hasta, equipo)
    }

    @GetMapping("/evento/equipo")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByEquipo(String equipo) {
        if (equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEventosByEquipo(equipo)
    }

    @GetMapping("/evento/nombre")
    @ResponseStatus(HttpStatus.FOUND)
    Evento[] getEventosByNombre(String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getEventosByNombre(nombre)
    }

}
