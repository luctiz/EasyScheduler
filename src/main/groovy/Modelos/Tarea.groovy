package Modelos

import Excepciones.InvalidDateException
import Excepciones.InvalidPesoException
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalTime

@Document("eventos")
class Tarea extends Entity {

    public String nombre
    public String descripcion
    public LocalTime horaInicio
    public LocalTime horaFin
    public String asignado
    public Estado estado
    public int peso = 1

    Tarea(String nombre, String descripcion, LocalTime horaInicio, LocalTime horaFin,  String asignado,  ObjectId _id, int peso = 1) {
        if (horaFin < horaInicio) {
            throw new InvalidDateException()
        }
        if (peso < 0) {
            throw new InvalidPesoException()
        }
        this.nombre = nombre
        this.horaInicio = horaInicio
        this.descripcion = descripcion
        this.horaFin = horaFin
        this.asignado = asignado
        this.peso = peso
        this.estado = estado.Pendiente
        super._id = _id
    }

    Tarea() {

    }
}
