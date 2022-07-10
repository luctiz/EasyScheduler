package Repositorios

import Modelos.Evento
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDate

interface EventoRepository extends MongoRepository<Evento, String>{
    Evento findByNombreFecha(String nombre)
    Evento[] findByFechaBetween(LocalDate desde, LocalDate hasta)
    Evento[] findByNombreLike(String nombre)
    //@Query("\$and [{'Nombre': /.*?0.*/}, {'Fecha': {\$gte: ?1, \$lte: ?2}}]")
    Evento[] findByNombreAndFechaBetween(String nombre, LocalDate desde, LocalDate hasta)
    Evento[] findByEquipo(String equipo)
    Evento[] findByEquipoAndFechaBetween(String equipo, LocalDate desde, LocalDate hasta)

//    @Query("{'Tareas.Nombre': {\$in : [?0] }")
//    Evento findByTareas(String nombre)
}