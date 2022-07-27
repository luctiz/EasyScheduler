package Servicios

import Excepciones.TareaInvalidaException
import Modelos.Estado
import Modelos.Evento
import Modelos.Tarea
import Repositorios.EventoRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class TareaService extends ServiceBase {

    private static Logger logger = LoggerFactory.getLogger(TareaService.class)

    @Autowired
    private EventoRepository eventoRepository

    @Autowired
    private UsuarioService usuarioService

    @Autowired
    private EquipoService equipoService

    @Autowired
    private EventoService eventoService

    Evento agregarTarea(String nombreFecha, Tarea tarea) {
        def evento = eventoService.getEvento(nombreFecha)
        if (evento.tareas.contains(tarea)) {
            logger.error("ya existe tarea ${tarea.nombre} en el evento")
            throw new TareaInvalidaException("ya existe tarea ${tarea.nombre} en el evento")
        }
        if (!usuarioService.getUsuario(tarea.asignado).equipos.contains(equipoService.getEquipo(evento.nombre))) {
            logger.error("el asignado ${tarea.asignado} no pertenece al equipo ${evento.equipo} del evento ${evento.nombre}")
            throw new TareaInvalidaException("el asignado ${tarea.asignado} no pertenece al equipo ${evento.equipo} del evento ${evento.nombre}")
        }
        evento.tareas += tarea
        eventoRepository.save(evento)
        return evento
    }

    void borrarTarea(Evento evento, Tarea tarea) {
        if (!evento.tareas.contains(tarea)) {
            logger.error("no existe tarea ${tarea.nombre} en el evento")
            throw new TareaInvalidaException("no existe tarea ${tarea.nombre} en el evento")
        }
        evento.tareas -= tarea
        eventoRepository.save(evento)
    }

    Evento[] getTareasByNombre(String nombre, String evento) {
        def eventosContarea = []
        def eventos = eventoService.getEventosByNombre(evento)
        eventos.each {e ->
            if (e.tareas.find { t -> t.nombre == nombre })
                eventosContarea += e
        }
        return eventosContarea
    }

    Evento[] getTareasByAsignado(String usuario, String evento) {
        def eventosYTareasConAsignado = []
        def eventos = eventoService.getEventosByNombre(evento)
        for (i in 0..<eventos.size()) {
            def tareas = eventos[i].tareas.findAll { t ->
                t.asignado == usuario
            }
            if (tareas.size() > 0) {
                eventos[i].tareas = tareas
                eventosYTareasConAsignado += eventos[i]
            }
        }
        return eventosYTareasConAsignado
    }

    Evento modficarTareas(String nombreFecha, Tarea[] tareas) {
        def evento = eventoService.getEvento(nombreFecha)
        for (i in 0..<evento.tareas.size()) {
            def tarea = tareas.find {t -> t._id == evento.tareas[i]._id}
            evento.tareas[i].nombre = tarea.nombre
            evento.tareas[i].asignado = tarea.asignado
            evento.tareas[i].horaFin = tarea.horaFin
            evento.tareas[i].horaInicio = tarea.horaInicio
            evento.tareas[i].descripcion = tarea.descripcion
            evento.tareas[i].peso = tarea.peso
        }
        eventoRepository.save(evento)
        return evento
    }

    Evento modificarEstado(String nombreFecha, String tarea, Estado estado) {
        def evento = eventoService.getEvento(nombreFecha)
        for (i in 0..<evento.tareas.size()) {
            if (evento.tareas[i].nombre == tarea)
                evento.tareas[i].estado = estado
        }
        eventoRepository.save(evento)
        return evento
    }
}
