package Servicios


import Modelos.Equipo
import Modelos.Usuario
import Repositorios.UsuarioRepository
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.web.server.ResponseStatusException


@Service
class EquipoService extends ServiceBase {

    private Logger logger = LoggerFactory.getLogger(EquipoService.class)

    @Autowired
    private UsuarioRepository usuarioRepository

    @Autowired
    private UsuarioService usuarioService


    Equipo agregarMiembro(Equipo equipo, String nuevo_miembro) throws Throwable {
        Usuario[] usuario = usuarioRepository.findByNombreUsuario(nuevo_miembro)
        if (!usuario) {
            logger.error("el usuario ${nuevo_miembro} no existe")
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, nuevo_miembro)
        }
        if (usuario.equipos.contains(equipo)) {
            logger.error("el usuario ${usuario.NombreUsuario} ya existe en ${equipo.nombre}")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el usuario ${usuario.NombreUsuario} ya existe en ${equipo.nombre}")
        }
        logger.info("Se agrego ${usuario.NombreUsuario} al equipo ${equipo.nombre}")
        usuario.Equipos += equipo
        usuarioRepository.save(usuario)
        return equipo
    }

    Equipo crearEquipo(Equipo equipo, Usuario creador, Usuario[] miembros) {
        if (exsiteEquipo(equipo)) {
            logger.error("el equipo ${equipo.Nombre} ya existe")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el equipo ${equipo.Nombre} ya existe")
        }
        miembros = usuarioService.getUsuarios(miembros)
//        if (!miembrosEncontrados.contains(creador)) {
//            logger.error("no se pudo crear equipo ${equipo.Nombre} no se encontro el creador ${creador.NombreUsuario} en la lista de miembros")
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no se encontro el creador ${creador.NombreUsuario} en la lista de miembros")
//        }
        if (!miembros.contains(creador)) {
            creador = usuarioService.getUsuario(creador.NombreUsuario)
            miembros += creador
        }
        equipo.Lider = creador.NombreUsuario
        miembros.each {m ->
            m.Equipos += equipo
        }
        usuarioRepository.save(miembros)
        return equipo
    }

    Equipo getEquipo(String nombre) {
        Usuario[] usuarios = usuarioRepository.findByEquipos(new Equipo(nombre, null))
        if (!usuarios)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no existe equipo ${nombre}")
        Equipo ret = new Equipo(nombre, "")
        usuarios.each {  user ->
            if (user.Equipos.first().Lider == user.NombreUsuario)
                ret.Lider = user.NombreUsuario
        }
        return ret
    }

    Usuario[] getMiembros(String nombre) {
        Usuario[] usuarios = usuarioRepository.findByEquipos(new Equipo(nombre, null))
        if (!usuarios)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no existe equipo ${nombre}")
        return usuarios
    }

    boolean exsiteEquipo(Equipo equipo) {
        return usuarioRepository.findByEquipos(equipo)
    }
}
