package Servicios

import Excepciones.UsuarioNoExisteException
import Modelos.Equipo
import Modelos.Usuario
import Repositorios.UsuarioRepository
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository
    private Logger logger = LoggerFactory.getLogger(UsuarioService.class)

    Usuario crearUsuario(Usuario usuario) {
        if (usuarioExsite(usuario.nombreUsuario))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuario ${usuario.nombreUsuario} ya existe")
        usuario.equipos += crearNuevoEquipo("${usuario.nombreUsuario}_Privado", usuario)
        usuarioRepository.save(usuario)
        return usuario
    }

    private Equipo crearNuevoEquipo(String nombre, Usuario usuario) {
        def equipo = new Equipo(nombre, usuario.nombreUsuario, ObjectId.get())
        if (usuarioRepository.findByEquipos(nombre)) {
            logger.error("ya existe un equipo con nombre ${nombre}")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "equipo ${nombre} ya existe")
        }
        return equipo
    }

    boolean usuarioExsite(String nombreUsuario) {
        def usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
        return usuario
    }
//    void completarTarea(Tarea tarea){
//        if (tarea.getAsignado() != this){
//            throw new TareaNoAsignadaException()
//        }
//        tarea.setEstado(Estado.Completado)
//    }

    Usuario getUsuario(String nombreUsuario) throws Throwable {
        def usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
        if (!usuario)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario con nombre de usuario ${nombreUsuario} no encontrado")
        return usuario
    }

    Usuario[] getUsuarios(Usuario[] usuarios) {
        def miembrosEncontrados = []
        usuarios.each {us ->
            miembrosEncontrados += usuarioRepository.findByNombreUsuario(us.nombreUsuario)
        }
        String[] users = []
        usuarios.each {u -> users += u.nombreUsuario}
        return getUsers(miembrosEncontrados as Usuario[], users)
    }

    Usuario[] getUsuarios(String[] usuarios) {
        def miembrosEncontrados = []
        usuarios.each {us ->
            miembrosEncontrados += usuarioRepository.findByNombreUsuario(us)
        }
        return getUsers(miembrosEncontrados as Usuario[], usuarios)
    }

    private Usuario[] getUsers(Usuario[] miembrosEncontrados, String[] usuarios) {
        String[] faltantes = []
        if (miembrosEncontrados.size() != usuarios.size()) {
            miembrosEncontrados.each { m ->
                if (!usuarios.contains(m.nombreUsuario))
                    faltantes += m.nombreUsuario
            }
            logger.error("no se encontro los siguiente usuarios ${faltantes}")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no se encontro los siguiente usuarios ${faltantes}")
        }
        return miembrosEncontrados
    }


    Usuario modificarUsuario(Usuario usuario) {
        if (!usuarioExsite(usuario.nombreUsuario)) {
            throw new UsuarioNoExisteException("no existe el usuario ${usuario.nombreUsuario}")
        }
        def usuario_edit = usuarioRepository.findByNombreUsuario(usuario.nombreUsuario)
        usuario_edit.contrasenia = usuario.contrasenia
        usuarioRepository.save(usuario_edit)
        return usuario_edit
    }

    void borrarUsuario(String nombreUsuario, String contrasenia) {
        def user = getUsuario(nombreUsuario)
        if (user.contrasenia != contrasenia) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuario o contraseña invalida")
        }
        usuarioRepository.delete(user)
    }
}
