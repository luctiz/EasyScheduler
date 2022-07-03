package Servicios

import Excepciones.EventoNoExisteException
import Excepciones.FechaInvalidException
import Excepciones.TareaInvalidaException
import Excepciones.UsuarioNoEsLiderException
import Modelos.Evento
import Modelos.Tarea
import Repositorios.EventoRepository
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.time.LocalDate

@Service
class EventoService extends ServiceBase {

    private static Logger logger = LoggerFactory.getLogger(EventoService.class)

    @Autowired
    private EventoRepository eventoRepository

    @Autowired
    private UsuarioService usuarioService

    @Autowired
    private EquipoService equipoService

    private verificarEvento(Evento evento, String usuario) {
        def team = equipoService.getEquipo(evento.equipo)
        def user = usuarioService.getUsuario(usuario)
        if (team.lider != user.nombreUsuario) {
            logger.error("el usuario ${usuario} no es lider del equipo ${evento.equipo}")
            throw new UsuarioNoEsLiderException("el usuario ${usuario} no es lider del equipo ${evento.equipo}")
        }
        return evento
    }

    Evento crearEvento(Evento evento, String usuario) {
        def ret = verificarEvento(evento, usuario)
        eventoRepository.save(ret)
        return ret
    }

    Evento editarEvento(Evento evento, String usuario) {
        def ret = verificarEvento(evento, usuario)
        eventoRepository.save(ret)
        return ret
    }

    void borrarEvento(ObjectId eventoId = null, String nombreFecha = null) {
        def evento
        if (evento)
            evento = getEvento(nombreFecha)
        else
            evento = getEvento(eventoId)
        if (!existeEvento(evento.nombreFecha)) {
            logger.error("evento ${evento.nombreFecha} no existe")
            throw new EventoNoExisteException("evento ${evento.nombreFecha} no existe")
        }
        eventoRepository.delete(evento)
    }

    Evento agregarTarea(Evento evento, Tarea tarea) {
        if (evento.tareas.contains(tarea)) {
            logger.error("ya existe tarea ${tarea.Nombre} en el evento")
            throw new TareaInvalidaException("ya existe tarea ${tarea.Nombre} en el evento")
        }
        if (!usuarioService.getUsuario(tarea.Asignado).equipos.contains(equipoService.getEquipo(evento.nombre))) {
            logger.error("el asignado ${tarea.Asignado} no pertenece al equipo ${evento.equipo} del evento ${evento.nombre}")
            throw new TareaInvalidaException("el asignado ${tarea.Asignado} no pertenece al equipo ${evento.equipo} del evento ${evento.nombre}")
        }
        evento.tareas += tarea
        eventoRepository.save(evento)
        return evento
    }

    void borrarTarea(Evento evento, Tarea tarea) {
        if (!evento.tareas.contains(tarea)) {
            logger.error("no existe tarea ${tarea.Nombre} en el evento")
            throw new TareaInvalidaException("no existe tarea ${tarea.Nombre} en el evento")
        }
        evento.tareas -= tarea
        eventoRepository.save(evento)
    }

    Evento getEvento(ObjectId id) {
        def evento = eventoRepository.findById(id)
        if (!evento.get()) {
            logger.error("evento ${id} no existe")
            throw new EventoNoExisteException("evento ${id} no existe")
        }
        return evento.get()
    }

    Evento getEvento(String nombreFecha) {
        def evento = eventoRepository.findByNombreFecha(nombreFecha)
        if (!evento) {
            logger.error("evento ${nombreFecha} no existe")
            throw new EventoNoExisteException("evento ${nombreFecha} no existe")
        }
        return evento
    }

    private static checkFecha(LocalDate desde, LocalDate hasta) {
        if (desde > hasta) {
            logger.error("fecha ${desde}, debe ser menor a fecha ${hasta}")
            throw new FechaInvalidException("fecha ${desde}, debe ser menor a fecha ${hasta}")
        }
    }

    Evento[] getEventosByFechas(LocalDate desde, LocalDate hasta) {
        checkFecha(desde, hasta)
        return eventoRepository.findByFechaBetween(desde, hasta)
    }

    Evento[] getEventosByFechasAndNombre(LocalDate desde, LocalDate hasta, String nombre) {
        checkFecha(desde, hasta)
        return eventoRepository.findByNombreAndFechaBetween(nombre, desde, hasta)
    }

    Evento[] getEventosByFechasAndEquipo(LocalDate desde, LocalDate hasta, String equipo) {
        equipoService.getEquipo(equipo)
        checkFecha(desde, hasta)
        return eventoRepository.findByEquipoAndFechaBetween(equipo, desde, hasta)
    }

    Evento[] getEventosByEquipo(String equipo) {
        equipoService.getEquipo(equipo)
        return eventoRepository.findByEquipo(equipo)
    }

    Evento[] getEventosByNombre(String nombre) {
        return eventoRepository.findByNombreLike(nombre)
    }

    private boolean existeEvento(String nombreFecha) {
        return eventoRepository.findByNombreFecha(nombreFecha)
    }
}
