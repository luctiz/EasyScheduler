package Servicios

import Modelos.Evento
import Modelos.Tarea
import Modelos.Usuario

import java.time.LocalTime
import java.time.temporal.TemporalAmount

class TaskScheduler {

    private static Map<String, Integer> userTasks = new HashMap<String, Integer>();


    private static class LocalTimeRange {

        private final LocalTime from
        private final LocalTime to

        LocalTimeRange(LocalTime from, LocalTime to) {
            this.from = from
            this.to = to
        }

        boolean overlaps(LocalTimeRange other) {
            return isBetween(other.from, this.from, this.to)
                    || isBetween(other.to, this.from, this.to)
                    || isBetween(this.from, other.from, other.to)
                    || isBetween(this.to, other.from, other.to)
        }

        private static boolean isBetween(LocalTime t, LocalTime from, LocalTime to) {
            if (from.isBefore(to)) { // same day
                return from.isBefore(t) && t.isBefore(to)
            } else { // spans to the next day.
                return from.isBefore(t) || t.isBefore(to)
            }
        }
    }

    private static boolean hasConflict(Tarea tareaAAsignar, Tarea[] tareas, String user) {
        def tareasUser = tareas.findAll { t -> t.asignado == user }
        def range = new LocalTimeRange(tareaAAsignar.horaInicio, tareaAAsignar.horaFin)
        tareasUser.each {
            if (range.overlaps(new LocalTimeRange(it.horaInicio, it.horaFin)))
                return true
        }
        return false
    }


    private static String getUsertoAssignTask(int n, Tarea tareaAAsignar, Tarea[] tareas) {
        def iter = userTasks.iterator()
        def user = userTasks.keySet().first()
        if (hasConflict(tareaAAsignar, tareas, user) && userTasks.size() != 1) {
            user = iter.next().key
            while (iter.hasNext()) {
                if (hasConflict(tareaAAsignar, tareas, user))
                    user = iter.next().key
            }
        }
        userTasks[user] = userTasks.get(user) + n
        // ordeno de menor a mayor peso
        userTasks = userTasks.sort { it.value}
        return user
    }

    static Tarea[] scheduleTasks(Evento evento, Usuario[] usuarios) {
        evento.tareas.each {
            it.asignado = ""
        }
        def tareas = evento.tareas
        usuarios.each { u ->
            userTasks.put(u.nombreUsuario, 0)
        }
        while(tareas.size() > 0) {
            def i = tareas.max { it.peso}.peso
            Tarea[] tareas_weighti = []
            for (j in 0..<tareas.size()) {
                if (tareas[j].peso == i) {
                    tareas_weighti += tareas[j]
                }
            }
            for (j in 0..<tareas_weighti.size()) {
                def t = tareas.find { t -> t.nombre == tareas_weighti[j].nombre}
                tareas -= t
            }
            evento.tareas = assignTasks(evento.tareas, tareas_weighti, i)
        }
        return evento.tareas
    }

    private static Tarea[] assignTasks(Tarea[] tareas, Tarea[] tareas_weighti, int n) {
        for (i in 0..<tareas_weighti.size()) {
            def usuario = getUsertoAssignTask(n, tareas_weighti[i], tareas)
            tareas_weighti[i].asignado = usuario
            tareas.each { t ->
                if (t.nombre == tareas_weighti[i].nombre)
                    t.asignado = tareas_weighti[i].asignado
            }
        }
        return tareas
    }

}

