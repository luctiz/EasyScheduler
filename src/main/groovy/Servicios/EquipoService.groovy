package Servicios

import Excepciones.MiembroYaExistenteException
import Modelos.Equipo
import Repositorios.EquipoRepository
import Repositorios.UsuarioRepository
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory


@Service
class EquipoService extends ServiceBase {

    Logger logger = LoggerFactory.getLogger(EquipoService.class)

    @Autowired
    private EquipoRepository equipoRepository

    @Autowired
    private UsuarioRepository usuarioRepository


    Equipo crearEquipo(Equipo equipo) {
        equipoRepository.save(equipo)
        return equipo
    }

    Equipo agregarMiembro(Equipo equipo, String nuevo_miembro) {
        def usuario = usuarioRepository.findByNombreUsuario(nuevo_miembro)
        if (equipo.miembros.contains(usuario)) {
            logger.error("el usuario ${usuario.NombreUsuario} ya existe en ${equipo.nombre}")
            throw new MiembroYaExistenteException()
        }
        logger.info("Se agrego ${usuario.NombreUsuario} al equipo ${equipo.nombre}")
        equipo.miembros = equipo.miembros + usuario
        return equipo
    }
}
