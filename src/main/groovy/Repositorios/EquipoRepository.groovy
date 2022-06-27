package Repositorios

import Modelos.Equipo
import org.springframework.data.mongodb.repository.MongoRepository

interface EquipoRepository extends MongoRepository<Equipo, String> {

}
