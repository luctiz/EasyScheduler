<<<<<<< Updated upstream:src/main/groovy/DuplicarEvento.groovy
=======
package Servicios

import Modelos.Equipo
import Modelos.Evento
import Modelos.Usuario

>>>>>>> Stashed changes:src/main/groovy/Servicios/DuplicarEvento.groovy
import java.time.LocalDate

class DuplicarEvento {
    static Evento DuplicarEventos(Evento evento, Usuario usuario, Equipo equipo, fecha = LocalDate.now()) {
        def duplicado = new Evento(evento.Nombre, fecha, equipo, usuario)
        evento.tareas.eachWithIndex {t, index -> duplicado.addTarea(index, t.Nombre, t.HoraInicio, t.HoraFin, usuario, usuario, t.Peso)}
        return duplicado
    }
}
