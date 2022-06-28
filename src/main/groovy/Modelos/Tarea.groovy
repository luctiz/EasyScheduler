package Modelos

import Excepciones.InvalidDateException
import Excepciones.InvalidPesoException
import Excepciones.UsuarioAsignadoNoEsMiembroDelEquipoException
import Excepciones.UsuarioNoEsLiderException

import java.time.LocalTime

class Tarea {
    int Id
    String Nombre
    LocalTime HoraInicio
    LocalTime HoraFin
    private Usuario Asignado
    Estado estado
    int Peso = 1
    Evento Evento

    Tarea( id, nombre, horaInicio, horaFin, asignado, evento, peso = 1) {
        if (horaFin < horaInicio) {
            //log
            throw new InvalidDateException()
        }
        if (peso < 0) {
            //log
            throw new InvalidPesoException()
        }
        Nombre = nombre
        HoraInicio = horaInicio
        HoraFin = horaFin
        Asignado = asignado
        Peso = peso
        Evento = evento
        estado = Estado.Pendiente
        Id = id
    }

    Usuario getAsignado(){
        return this.Asignado
    }
    void setAsignado(Usuario asignante, Usuario asignado){
        if (asignante != this.Evento.Equipo.getLider()){
            throw new UsuarioNoEsLiderException()
        }

        if (!this.Evento.Equipo.getMiembros().contains(asignado)){
            throw new UsuarioAsignadoNoEsMiembroDelEquipoException()
        }

        this.Asignado = asignado
    }
}
