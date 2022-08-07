package Controladores

import Excepciones.UsuarioAsignadoNoEsMiembroDelEquipoException
import Modelos.Estado
import Modelos.Evento
import Modelos.Tarea
import Servicios.TareaService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

import javax.servlet.http.HttpServletResponse
import java.time.LocalDate
import java.time.LocalTime

@RestController
class TareaController extends ApiControllerBase {

    @Autowired
    private TareaService service

    @PutMapping("/tarea/{nombreFechaEvento}&{nombre}&{descripcion}&{horaInicio}&{horaFin}&{asignado}&{peso}")
    @ResponseStatus(HttpStatus.OK)
    Evento crearTarea(@PathVariable String nombreFechaEvento, @PathVariable String nombre, @PathVariable String descripcion,
                      @PathVariable @DateTimeFormat(pattern = "HH:mm:ss") LocalTime horaInicio, @PathVariable @DateTimeFormat(pattern = "HH:mm:ss") LocalTime horaFin,
                      @PathVariable String asignado, @PathVariable Optional<Integer> peso) {
        if (nombreFechaEvento.isAllWhitespace() || nombre.isAllWhitespace() || descripcion.isAllWhitespace() || asignado.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.agregarTarea(nombreFechaEvento, new Tarea(nombre, descripcion, horaInicio, horaFin, asignado, ObjectId.get(), peso.orElse(1)))
    }


    @PutMapping("/tarea/modificarTarea/{nombreFechaEvento}")
    @ResponseStatus(HttpStatus.OK)
    Evento modficarTareas(@PathVariable String nombreFechaEvento, @RequestBody Tarea[] tareas) {
        if (nombreFechaEvento.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.modficarTareas(nombreFechaEvento, tareas)
    }

    @PutMapping("/tarea/modificarAsignado/{nombreFechaEvento}&{nombre}&{asignar}")
    @ResponseStatus(HttpStatus.OK)
    Evento modificarAsignado(@PathVariable String nombreFechaEvento, @PathVariable String nombre, @PathVariable String asignar) {
        if (nombreFechaEvento.isAllWhitespace() || nombre.isAllWhitespace() || asignar.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.modificarAsignado(nombreFechaEvento, nombre, asignar)
    }

    @PutMapping("/tarea/borrarTareas{nombreFechaEvento}")
    @ResponseStatus(HttpStatus.OK)
    Evento borrarTareas(@PathVariable String nombreFechaEvento, @RequestBody String[] tareas) {
        if (nombreFechaEvento.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.borrarTareas(nombreFechaEvento, tareas)
    }

    @PutMapping("/tarea/modificarEstado/{nombreFechaEvento}&{tarea}&{estado}&{usuario}")
    @ResponseStatus(HttpStatus.OK)
    Evento modificarEstado(@PathVariable String nombreFechaEvento, @PathVariable String tarea, @PathVariable int estado, @PathVariable String usuario) {
        if (nombreFechaEvento.isAllWhitespace() || tarea.isAllWhitespace() || usuario.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        if (Estado.values().size() < estado)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.modificarEstado(nombreFechaEvento, tarea, Estado.values()[estado], usuario)
    }

    @PutMapping("/tarea/borrarTarea/{nombreFechaEvento}&{nombre}")
    @ResponseStatus(HttpStatus.OK)
    Evento borrarTarea(@PathVariable String nombreFechaEvento, @PathVariable String nombre) {
        if (nombreFechaEvento.isAllWhitespace() || nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.borrarTarea(nombreFechaEvento, nombre)
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

    @PutMapping("/tarea/repartirTareas/{nombreFechaEvento}")
    @ResponseStatus(HttpStatus.OK)
    Evento repartirTareas(@PathVariable String nombreFechaEvento, @RequestBody String[] asignarA) {
        if (asignarA.size() == 0 || nombreFechaEvento.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        return service.repartirTareas(nombreFechaEvento, asignarA)
    }

}
