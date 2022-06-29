package Modelos

import Excepciones.InvalidDateException
import Excepciones.InvalidPesoException
import Excepciones.UsuarioAsignadoNoEsMiembroDelEquipoException
import Excepciones.UsuarioNoEsLiderException

import java.time.LocalTime

class Tarea extends Entity {
    String Nombre
    LocalTime HoraInicio
    LocalTime HoraFin
    String Asignado
    Estado estado
    int Peso = 1

    Tarea( nombre, horaInicio, horaFin, asignado, peso = 1) {
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
        //Evento = evento
        estado = Estado.Pendiente
    }

    String getAsignado(){
        return this.Asignado
    }
    void setAsignado(String asignante, String asignado){
//        if (asignante != this.Evento.Equipo.getLider()){
//            throw new UsuarioNoEsLiderException()
//        }

//        if (!Evento.Equipo.getMiembros().contains(asignado)){
//            throw new UsuarioAsignadoNoEsMiembroDelEquipoException()
//        }

        this.Asignado = asignado
    }
}
