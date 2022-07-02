package Servicios

import Modelos.Equipo
import Modelos.Usuario
import Repositorios.UsuarioRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TareaService extends ServiceBase {

    private Logger logger = LoggerFactory.getLogger(TareaService.class)

    @Autowired
    private UsuarioRepository usuarioRepository

    @Autowired
    private UsuarioService usuarioService


    Equipo agregarMiembro(Equipo equipo, String nuevo_miembro) throws Throwable {
        Usuario usuario = usuarioService.getUsuario(nuevo_miembro)
        if (!usuario) {
            logger.error("el usuario ${nuevo_miembro} no existe")
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, nuevo_miembro)
        }
        if (usuario.equipos.contains(equipo)) {
            logger.error("el usuario ${usuario.nombreUsuario} ya existe en ${equipo.nombre}")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el usuario ${usuario.nombreUsuario} ya existe en ${equipo.nombre}")
        }
        logger.info("Se agrego ${usuario.nombreUsuario} al equipo ${equipo.nombre}")
        usuario.equipos += equipo
        usuarioRepository.save(usuario)
        return equipo
    }

    Equipo crearEquipo(String nombre, Usuario creador, Usuario[] miembros) {
        if (exsiteEquipo(nombre)) {
            logger.error("el equipo ${nombre} ya existe")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el equipo ${nombre} ya existe")
        }
        miembros = usuarioService.getUsuarios(miembros)
        if (!miembros.contains(creador)) {
            creador = usuarioService.getUsuario(creador.nombreUsuario)
            miembros += creador
        }
        def equipo = new Equipo(nombre, creador.nombreUsuario)
        miembros.each {m ->
            m.equipos += equipo
        }
        usuarioRepository.saveAll(miembros.iterator() as Iterable<Usuario>)
        return equipo
    }

    Equipo getEquipo(String nombre) {
        Usuario[] usuarios = usuarioRepository.findByEquipos(nombre)
        if (!usuarios)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no existe equipo ${nombre}")
        Equipo ret = new Equipo(nombre, "")
        usuarios.each {  user ->
            if (user.equipos.first().lider == user.nombreUsuario)
                ret.lider = user.nombreUsuario
        }
        return ret
    }

    Usuario[] getMiembros(String nombre) {
        Usuario[] usuarios = usuarioRepository.findByEquipos(nombre)
        if (!usuarios)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no existe equipo ${nombre}")
        return usuarios
    }

    boolean exsiteEquipo(String nombre) {
        return usuarioRepository.findByEquipos(nombre)
    }
}
