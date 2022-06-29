package Repositorios

import Modelos.Equipo
import Modelos.Usuario
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UsuarioRepository extends MongoRepository<Usuario, ObjectId>{
    Usuario findByNombreUsuario(String nombreUsuario)
    Usuario[] findAllByNombreUsuario(String[] nombreUsuario)
    Usuario[] findByEquipos(Equipo equipo)
}