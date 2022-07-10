package Servicios

import Excepciones.UsuarioNoEsLiderException
import Modelos.Equipo
import Modelos.Usuario
import Repositorios.UsuarioRepository
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
        Usuario usuario = usuarioService.getUsuario(nuevo_miembro)
        equipo = getEquipo(equipo.nombre)
        if (usuario.equipos.each { e ->
            if (e.nombre == equipo.nombre) {
                logger.error("el usuario ${usuario.nombreUsuario} ya existe en ${equipo.nombre}")
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el usuario ${usuario.nombreUsuario} ya existe en ${equipo.nombre}")
            }
        })
        logger.info("Se agrego ${usuario.nombreUsuario} al equipo ${equipo.nombre}")
        usuario.equipos += equipo
        usuarioRepository.save(usuario)
        return equipo
    }

    Equipo crearEquipo(String nombre, String creador, Usuario[] miembros) {
        if (exsiteEquipo(nombre)) {
            logger.error("el equipo ${nombre} ya existe")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el equipo ${nombre} ya existe")
        }
        def userCreador = usuarioService.getUsuario(creador)
        miembros = usuarioService.getUsuarios(miembros)
        boolean contains = false
        miembros.each { m ->
            if (m.nombreUsuario == creador)
                contains = true
        }
        if (!contains)
            miembros += userCreador
        def equipo = new Equipo(nombre, userCreador.nombreUsuario)
        for (i in 0..<miembros.size()) {
            miembros[i] = usuarioService.getUsuario(miembros[i].nombreUsuario)
            miembros[i].equipos += equipo
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
            Equipo remover = new Equipo("", "")
            m.equipos.each { e ->
                if (e.nombre == eq.nombre) {
                    remover = e
                    return true
                }
            }
            if (remover.nombre != "") {
                m.equipos -= remover
                m.equipos += equipo
            }
        }
        usuarioRepository.saveAll(miembros.iterator() as Iterable<Usuario>)
        return equipo
    }

    Equipo removerEquipo(String equipo, String lider, String[] miembrosARemover) {
        def eq = getEquipo(equipo)
        if (eq.lider != lider)
            throw new UsuarioNoEsLiderException("usuario ${lider} no es lider de ${equipo}")
        def miembros = getMiembros(equipo)
        for (i in 0..<miembros.size()) {
            Equipo remover = new Equipo("", "")
            if (miembrosARemover.contains(miembros[i].nombreUsuario)) {
                miembros[i].equipos.each { e ->
                    if (e.nombre == eq.nombre) {
                        remover = e
                        return true
                    }
                }
                if (remover.nombre != "")
                    miembros[i].equipos -= remover
            }
        }
        // TODO no esta eliminando el miembro
        usuarioRepository.saveAll(miembros.iterator() as Iterable<Usuario>)
        return eq
    }

    void borrarEquipo(String nombre, String lider) {
        def eq = getEquipo(nombre)
        if (eq.lider != lider)
            throw new UsuarioNoEsLiderException("usuario ${lider} no es lider de ${nombre}")
        def miembros = getMiembros(nombre)
        for (i in 0..<miembros.size()) {
            Equipo remover = new Equipo("", "")
            miembros[i].equipos.each { e ->
                if (e.nombre == eq.nombre) {
                    remover = e
                    return true
                }
            }
            if (remover.nombre != "") {
                miembros[i].equipos -= remover
            }
        }
        // TODO no esta eliminando todos los miembros
        usuarioRepository.saveAll(miembros.iterator() as Iterable<Usuario>)
    }
}
