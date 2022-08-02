package Controladores

import Modelos.Estado
import Modelos.Evento
import Modelos.Tarea
import Servicios.TareaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

import javax.servlet.http.HttpServletResponse
import java.time.LocalDate

@RestController
class TareaController extends ApiControllerBase {
    @Autowired
    private TareaService service

    @PutMapping("/tarea/{nombreFechaEvento}&{nombre}&{descripcion}&{horaInicio}&{horaFin}&{asignado}&{peso}")
    @ResponseStatus(HttpStatus.OK)
    Evento crearTarea(@PathVariable String nombreFechaEvento, @PathVariable String nombre, @PathVariable String descripcion,
                        @PathVariable LocalDate horaInicio, @PathVariable LocalDate horaFin,
                        @PathVariable String asignado, @PathVariable Optional<Integer> peso) {
        if (nombreFechaEvento.isAllWhitespace() || nombre.isAllWhitespace() || descripcion.isAllWhitespace() || asignado.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.agregarTarea(nombreFechaEvento, new Tarea(nombre, descripcion, horaInicio, horaFin, asignado, peso.orElse(1)))
    }

    @PutMapping("/tarea/modificarTarea/{nombreFechaEvento}")
    @ResponseStatus(HttpStatus.OK)
    Evento modficarTareas(@PathVariable String nombreFechaEvento, @RequestBody Tarea[] tareas) {
        if (nombreFechaEvento.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.modficarTareas(nombreFechaEvento, tareas)
    }

    @PutMapping("/tarea/modificarEstado/{nombreFechaEvento}&{tarea}&{estado}")
    @ResponseStatus(HttpStatus.OK)
    Evento modificarEstado(@PathVariable String nombreFechaEvento, @PathVariable String tarea, @PathVariable int estado) {
        if (nombreFechaEvento.isAllWhitespace() || tarea.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        if (Estado.values().size() < estado)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.modificarEstado(nombreFechaEvento, tarea, Estado.values()[estado])
    }

    @PutMapping("/tarea/borrarTarea/{nombreFechaEvento}")
    @ResponseStatus(HttpStatus.OK)
    Evento borrarTarea(@PathVariable String nombreFechaEvento, @RequestBody Tarea tarea) {
        if (nombreFechaEvento.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.borrarTarea(nombreFechaEvento, tarea)
    }

    @GetMapping("/tarea/getTareasByNombre/{nombre}&{nombreFechaEvento}")
    @ResponseStatus(HttpStatus.OK)
    Evento[] getTareasByNombre(@PathVariable String nombre, @PathVariable String nombreFechaEvento) {
        if (nombre.isAllWhitespace() || nombreFechaEvento.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getTareasByNombre(nombre, nombreFechaEvento)
    }

    @GetMapping("/tarea/getTareasByAsignado/{asignado}&{nombreFechaEvento}")
    @ResponseStatus(HttpStatus.OK)
    Evento[] getTareasByAsignado(@PathVariable String asignado, @PathVariable String nombreFechaEvento) {
        if (asignado.isAllWhitespace() || nombreFechaEvento.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.getTareasByAsignado(asignado, nombreFechaEvento)
    }

}
