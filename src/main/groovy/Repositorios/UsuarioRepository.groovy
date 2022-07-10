package Repositorios

import Modelos.Usuario
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UsuarioRepository extends MongoRepository<Usuario, String>{
    Usuario findByNombreUsuario(String nombreUsuario)
    @Query("{'equipos.nombre': {\$in : [?0] }}")
    Usuario[] findByEquipos(String nombre)
}