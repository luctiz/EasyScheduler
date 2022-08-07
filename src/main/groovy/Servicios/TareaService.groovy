package Servicios

import Excepciones.InvalidDateException
import Excepciones.InvalidPesoException
import Excepciones.TareaInvalidaException
import Excepciones.UsuarioNoAsignadoATareaException
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
        def eq = equipoService.getEquipo(evento.equipo)
        if (!usuarioService.getUsuario(tarea.asignado).equipos.find{ e -> e.nombre == eq.nombre}) {
            logger.error("el asignado ${tarea.asignado} no pertenece al equipo ${evento.equipo} del evento ${evento.nombre}")
            throw new TareaInvalidaException("el asignado ${tarea.asignado} no pertenece al equipo ${evento.equipo} del evento ${evento.nombre}")
        }
        evento.tareas += tarea
        eventoRepository.save(evento)
        return evento
    }

    Evento agregarTareas(String nombreFecha, Tarea[] tareas) {
        def evento = eventoService.getEvento(nombreFecha)
        def eq = equipoService.getEquipo(evento.equipo)
        evento.tareas.each { t ->
            def remover = tareas.find { t2 -> t2.nombre == t.nombre}
            tareas -= remover
        }
        tareas.each { t ->
            if (!usuarioService.getUsuario(t.asignado).equipos.find{ e -> e.nombre == eq.nombre}) {
                logger.error("el asignado ${t.asignado} no pertenece al equipo ${evento.equipo} del evento ${evento.nombre}")
                throw new TareaInvalidaException("el asignado ${t.asignado} no pertenece al equipo ${evento.equipo} del evento ${evento.nombre}")
            }
            evento.tareas += t
        }
        eventoRepository.save(evento)
        return evento
    }

    Evento borrarTarea(String nombreFechaEvento, String tarea) {
        def evento = eventoService.getEvento(nombreFechaEvento)
        def t = evento.tareas.find{t -> t.nombre == tarea}
        if (!t) {
            logger.error("no existe tarea ${tarea} en el evento")
            throw new TareaInvalidaException("no existe tarea ${tarea} en el evento")
        }
        evento.tareas -= t
        eventoRepository.save(evento)
        return evento
    }

    Evento borrarTareas(String nombreFechaEvento, String[] tareas) {
        def evento = eventoService.getEvento(nombreFechaEvento)
        tareas.each { t ->
            def remover = evento.tareas.find { t2 -> t2.nombre == t }
            evento.tareas -= remover
        }
        eventoRepository.save(evento)
        return evento
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
            def tarea = null
            for (j in 0..<tareas.size()) {
                if (tareas[j]._id.timestamp == evento.tareas[i]._id.timestamp)
                    tarea = tareas[j]
            }
            if (!tarea || !checkTarea(tarea, evento))
                throw new TareaInvalidaException("${tarea.nombre} es invalida")
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

    Evento modificarEstado(String nombreFecha, String tarea, Estado estado, String nombreUsuario) {
        usuarioService.getUsuario(nombreUsuario)
        def evento = eventoService.getEvento(nombreFecha)
        if (!evento.tareas.find {t -> t.asignado == nombreUsuario && t.nombre == tarea})
            throw new UsuarioNoAsignadoATareaException()
        for (i in 0..<evento.tareas.size()) {
            if (evento.tareas[i].nombre == tarea)
                evento.tareas[i].estado = estado
        }
        eventoRepository.save(evento)
        return evento
    }

    private boolean checkTarea(Tarea tarea, Evento evento) {
        def usuario = usuarioService.getUsuario(tarea.asignado)
        if (!usuario.equipos.find {e -> e.nombre == evento.equipo})
            return false
        if (tarea.horaFin < tarea.horaInicio)
            throw new InvalidDateException()
        if (tarea.peso < 0)
            throw new InvalidPesoException()
        return true
    }

    Evento modificarAsignado(String nombreFechaEvento, String nombre, String asignar) {
        def evento = eventoService.getEvento(nombreFechaEvento)
        for (i in 0..<evento.tareas.size()) {
            if (evento.tareas[i].nombre == nombre) {
                evento.tareas[i].asignado = asignar
                if (!checkTarea(evento.tareas[i], evento))
                    throw new TareaInvalidaException("${nombre} es invalida")
            }
        }
        eventoRepository.save(evento)
        return evento
    }
}
