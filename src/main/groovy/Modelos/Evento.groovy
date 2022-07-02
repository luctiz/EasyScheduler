package Modelos
import Excepciones.InvalidDateException
import Excepciones.TareaInvalidaException
import Excepciones.UsuarioNoEsLiderException
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDate
import java.time.LocalDateTime

@Document("eventos")
class Evento extends Entity {
    public String nombre
    public String nombreFecha
    public LocalDate fecha
    public String equipo
    public Tarea[] tareas = []

    Evento(nombre, fecha, equipo) {
        this.nombre = nombre
        if (fecha < LocalDate.now())
            throw new InvalidDateException()
        this.fecha = fecha
        nombreFecha = nombre + fecha.format("yyyyMMdd") + LocalDateTime.now().getTimeString()
        this.equipo = equipo
    }

    Tarea addTarea(id, nombreTarea, horaInicio, horaFin, asignado, String creador, peso = 1) {
        if (creador != this.equipo.lider) {
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