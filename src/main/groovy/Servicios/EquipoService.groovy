package Servicios

import Excepciones.UsuarioNoEsLiderException
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


    Equipo agregarMiembro(Equipo equipo, String nuevo_miembro) {
        Usuario usuario = usuarioService.getUsuario(nuevo_miembro)
        equipo = getEquipo(equipo.nombre)
        if (usuario.equipos.find { e -> e.nombre == equipo.nombre}) {
            logger.error("el usuario ${usuario.nombreUsuario} ya existe en ${equipo.nombre}")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el usuario ${usuario.nombreUsuario} ya existe en ${equipo.nombre}")
        }
        logger.info("Se agrego ${usuario.nombreUsuario} al equipo ${equipo.nombre}")
        usuario.equipos += equipo
        usuarioRepository.save(usuario)
        return equipo
    }

    Equipo crearEquipo(String nombre, String creador, String[] miembros) {
        if (exsiteEquipo(nombre)) {
            logger.error("el equipo ${nombre} ya existe")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el equipo ${nombre} ya existe")
        }
        def userCreador = usuarioService.getUsuario(creador)
        Usuario[] miembrosAagregar = usuarioService.getUsuarios(miembros)
        if (!miembrosAagregar)
            miembrosAagregar = []
        if (!miembrosAagregar.find { u -> u.nombreUsuario == creador})
            miembrosAagregar += userCreador
        def equipo = new Equipo(nombre, userCreador.nombreUsuario, ObjectId.get())
        for (i in 0..<miembrosAagregar.size()) {
            miembrosAagregar[i] = usuarioService.getUsuario(miembrosAagregar[i].nombreUsuario)
            miembrosAagregar[i].equipos += equipo
        }
        usuarioRepository.saveAll(miembrosAagregar.toList())
        return equipo
    }

    Equipo getEquipo(String nombre) {
        Usuario[] usuarios = usuarioRepository.findByEquipos(nombre)
        if (!usuarios)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no existe equipo ${nombre}")
        Equipo ret = new Equipo(nombre, "", ObjectId.get())
        usuarios.each {  user ->
            if (user.equipos.find {e -> e.nombre == nombre}.lider == user.nombreUsuario)
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

    private boolean exsiteEquipo(String nombre) {
        return usuarioRepository.findByEquipos(nombre)
    }

    Equipo modficarEquipo(Equipo equipo, String lider) {
        def nuevo_lider = usuarioService.getUsuario(lider)
        def eq = getEquipo(equipo.nombre)
        if (eq.lider != nuevo_lider.nombreUsuario)
            throw new UsuarioNoEsLiderException("usuario ${lider} no es lider de ${equipo.nombre}")
        def miembros = getMiembros(eq.nombre)
        miembros.each { m ->
            Equipo remover = m.equipos.find { e -> e.nombre == eq.nombre}
            if (remover) {
                m.equipos -= remover
                m.equipos += equipo
            }
        }
        usuarioRepository.saveAll(miembros.toList())
        return equipo
    }

    Equipo removerEquipo(String equipo, String lider, String[] miembrosARemover) {
        def eq = getEquipo(equipo)
        if (eq.lider != lider)
            throw new UsuarioNoEsLiderException("usuario ${lider} no es lider de ${equipo}")
        def miembros = getMiembros(equipo)
        for (i in 0..< miembros.size()) {
            if (miembrosARemover.contains(miembros[i].nombreUsuario)) {
                Equipo remover = miembros[i].equipos.find { e -> e.nombre == eq.nombre}
                if (remover.nombre != "")
                    miembros[i].equipos -= remover
            }
        }
        usuarioRepository.saveAll(miembros.toList())
        return eq
    }

    void borrarEquipo(String nombre, String lider) {
        def eq = getEquipo(nombre)
        if (eq.lider != lider)
            throw new UsuarioNoEsLiderException("usuario ${lider} no es lider de ${nombre}")
        def miembros = getMiembros(nombre)
        for (i in 0..< miembros.size()) {
            Equipo remover = miembros[i].equipos.find { e -> e.nombre == eq.nombre }
            if (remover.nombre != "") {
                miembros[i].equipos -= remover
            }
        }
        usuarioRepository.saveAll(miembros.toList())
    }
}
