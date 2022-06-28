package Modelos
import Excepciones.InvalidDateException
import Excepciones.TareaInvalidaException
import Excepciones.UsuarioNoEsLiderException

import java.time.LocalDate
import java.time.LocalDateTime

class Evento {
    String Nombre
    String NombreFecha
    LocalDate Fecha
    Equipo Equipo
    Tarea[] tareas = []

    Evento(nombre, fecha, equipo, Usuario usuario) {
        Nombre = nombre
        if (fecha < LocalDate.now()) {
            // log
            throw new InvalidDateException()
        }
        Fecha = fecha
        NombreFecha = nombre + fecha.format("yyyyMMdd") + LocalDateTime.now().getTimeString()
        Equipo = equipo
        if (equipo.lider != usuario) {
            // log
            throw new UsuarioNoEsLiderException()
        }
    }

    Tarea addTarea(id, nombreTarea, horaInicio, horaFin, asignado, Usuario creador, peso = 1) {
        if (creador != this.Equipo.lider) {
            // log
            throw new UsuarioNoEsLiderException()
        }
        def tarea = new Tarea(id, nombreTarea, horaInicio, horaFin, asignado, this, peso)
        if (tareas.any{x -> x.Id == tarea.Id}) {
            throw new TareaInvalidaException()
        }
        tareas += tarea
        return tarea
    }
}