package Repositorios

import Modelos.Usuario
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UsuarioRepository extends MongoRepository<Usuario, ObjectId>{
    Usuario findByNombreUsuario(String nombreUsuario)
    @Query("{'Equipos.Nombre': {\$in : [?0] }")
    Usuario[] findByEquipos(String nombre)
}