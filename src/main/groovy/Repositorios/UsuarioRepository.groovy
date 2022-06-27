package Repositorios

import Modelos.Usuario
import org.springframework.data.mongodb.repository.MongoRepository

interface UsuarioRepository extends MongoRepository<Usuario, String>{
    Usuario findByNombreUsuario(String nombreUsuario)
}