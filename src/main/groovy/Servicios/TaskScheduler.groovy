package Servicios

import Modelos.Evento
import Modelos.Tarea
import Modelos.Usuario

import java.time.temporal.TemporalAmount

class TaskScheduler {

        // Find the latest job (in sorted array) that doesn't
        // conflict with the job[i]. If there is no compatible job,
        // then it returns -1.
        private static int latestNonConflict(Tarea[] arr, int i)
        {
            for (int j = i - 1; j >= 0; j--)
            {
                if (arr[j].horaFin <= arr[i - 1].horaInicio)
                    return j 
            }
            return -1 
        }

        // A recursive function that returns the maximum possible
        // profit from given array of jobs. The array of jobs must
        // be sorted according to horaFin time.
        private static int findMaxProfitRec(Tarea[] arr, int n)
        {
            // Base case
            if (n == 1) return arr[n-1].peso

            // Find profit when current job is included
            int inclProf = arr[n-1].peso 
            int i = latestNonConflict(arr, n) 
            if (i != -1)
                inclProf += findMaxProfitRec(arr, i+1) 

            // Find profit when current job is excluded
            int exclProf = findMaxProfitRec(arr, n-1) 

            return Math.max(inclProf, exclProf) 
        }

        // The main function that returns the maximum possible
        // profit from given array of jobs
        private static int findMaxProfit(Tarea[] arr, int n)
        {
            // Sort jobs according to finish time
            Arrays.sort(arr,new Comparator<Tarea>(){
                int compare(Tarea j1,Tarea j2)
                {
                    return (j1.horaFin.minus(j2.horaFin.toSecondOfDay())).toSecondOfDay()
                }
            }) 

            return findMaxProfitRec(arr, n) 
        }

    static Usuario[] getNUsuariosWithNTareasYPesoOrLess(Tarea[] tareas, Usuario[] usuarios, int n) {
        // get map usuario : peso de tareas asignadas
        // TODO encaralo por agrego una tarea por una osea el hashmap es statico, y agarro el que tiene menos peso le meto el n peso, y ordeno de nuevo, hasta que me quedo sin tareas
        Map<String, Integer> userTasks = new HashMap<String, Integer>();
        for (i in 0..<tareas.size()) {
            if (!userTasks.containsKey(tareas[i].asignado))
                userTasks.put(tareas[i].asignado, tareas[i].peso)
            else
                userTasks[tareas[i].asignado] = userTasks.get(tareas[i].asignado) + tareas[i].peso
        }
        // ordeno de menor a mayor peso
        userTasks = userTasks.sort { it.value}
        def retUsers = []
        int amountNeeded = n
        def userIterator = userTasks.iterator()
        def current = userIterator[0]
        int currentIndex = 0;
        while (userIterator.hasNext()) {
            if (current.value < userIterator[currentIndex + 1].value) {
                retUsers += usuarios.find { u -> u.nombreUsuario == current.key }
                amountNeeded -= n
            }
            current = userIterator.next()
            currentIndex += 1

        }
        return retUsers.subList(0, n)
    }

    static Tarea[] scheduleTasks(Evento evento, Usuario[] usuarios) {
        def tareas = evento.tareas
        while(tareas.size() > 0) {
            def i = findMaxProfit(tareas, tareas.size())
            def tareas_weighti = []
            for (j in 0..<tareas.size()) {
                if (j == i)
                    tareas_weighti += tareas[j]
            }
            if (usuarios.size() == evento.tareas.size()) {
                evento.tareas = assignTasksEqualSize(evento.tareas, tareas_weighti, usuarios)
            } else if (usuarios.size() < evento.tareas.size()) {
                evento.tareas = assignTasksLessUsers(evento.tareas, tareas_weighti, getNUsuariosWithNTareasYPesoOrLess(evento.tareas, usuarios, i))
            } else {
                evento.tareas = assignTasksMoreUsers(evento.tareas, tareas_weighti, getNUsuariosWithNTareasYPesoOrLess(evento.tareas, usuarios, i))
            }
        }
        return evento.tareas
    }

    static Tarea[] assignTasksEqualSize(Tarea[] tareas, List<Tarea> tareas_weighti, Usuario[] usuarios) {
        for (i in 0..<tareas_weighti.size()) {
            tareas_weighti[i].asignado = usuarios[i].nombreUsuario
        }
        for (i in 0..<tareas.size()) {
            def t = tareas_weighti.find { t-> t.nombre == tareas[i].nombre }
            tareas[i].asignado = t.asignado
        }
        return tareas
    }

    static Tarea[] assignTasksLessUsers(Tarea[] tareas, List<Tarea> tareas_weighti, Usuario[] usuarios) {
        HashMap<String, int>
        for (i in 0..<tareas.size()) {
            def t = tareas_weighti.find { t-> t.nombre == tareas[i].nombre }
            tareas[i].asignado = t.asignado
        }
        return tareas
    }

    static Tarea[] assignTasksMoreUsers(Tarea[] tareas, List<Tarea> tareas_weighti, Usuario[] usuarios) {
        for (i in 0..<tareas.size()) {
            def t = tareas_weighti.find { t-> t.nombre == tareas[i].nombre }
            tareas[i].asignado = t.asignado
        }
        return tareas
    }
}

