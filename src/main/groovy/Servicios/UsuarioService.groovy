package Servicios

import Excepciones.TareaNoAsignadaException
import Modelos.Equipo
import Modelos.Estado
import Modelos.Tarea
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
    @Autowired
    private EquipoService equipoService
    private Logger logger = LoggerFactory.getLogger(EquipoService.class)

    Usuario crearUsuario(Usuario usuario) {
        if (usuarioExsite(usuario.NombreUsuario))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuario ${usuario.NombreUsuario} ya existe")
        usuario.Equipos += crearNuevoEquipo("${usuario.NombreUsuario}_Privado", usuario)
        usuarioRepository.save(usuario)
        return usuario
    }

    private Equipo crearNuevoEquipo(String nombre, Usuario usuario) {
        def equipo = new Equipo(nombre, usuario.NombreUsuario)
        if (equipoService.exsiteEquipo(equipo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "equipo ${nombre} ya existe")
            logger.error("ya existe un equipo con nombre ${nombre}")
        }
        return equipo
    }

    private boolean usuarioExsite(String nombreUsuario) {
        def usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
        return usuario
    }
    void completarTarea(Tarea tarea){
        if (tarea.getAsignado() != this){
            throw new TareaNoAsignadaException()
        }
        tarea.setEstado(Estado.Completado)
    }

    Usuario getUsuario(String nombreUsuario) throws Throwable {
        def usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
        if (!usuario)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario con nombre de usuario ${nombreUsuario} no encontrado")
        return usuario
    }

    Usuario[] getUsuarios(Usuario[] usuarios) {
        String[] nombreUsuarios = usuarios.each { u -> u.NombreUsuario}
        // TODO falla
        def miembrosEncontrados = usuarioRepository.findAllByNombreUsuario(nombreUsuarios)
        String[] faltantes = []
        if (miembrosEncontrados.size() != usuarios.size()) {
            usuarios.each { m ->
                if (!miembrosEncontrados.contains(m))
                    faltantes += m.NombreUsuario
            }
            logger.error("no se encontro los siguiente usuarios ${faltantes}")
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no se encontro los siguiente usuarios ${faltantes}")
        }
        return miembrosEncontrados
    }


}
