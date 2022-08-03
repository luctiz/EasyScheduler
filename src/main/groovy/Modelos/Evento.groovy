package Modelos
import Excepciones.InvalidDateException
import Excepciones.TareaInvalidaException
import Excepciones.UsuarioNoEsLiderException
import org.bson.types.ObjectId
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

    Evento(String nombre, LocalDate fecha, String equipo, ObjectId _id) {
        this.nombre = nombre
        if (fecha < LocalDate.now())
            throw new InvalidDateException()
        this.fecha = fecha
        this.nombreFecha = nombre + fecha.format("yyyyMMdd") + LocalDateTime.now().getTimeString()
        this.equipo = equipo
        super._id = _id
    }

    Evento() {

    }
}