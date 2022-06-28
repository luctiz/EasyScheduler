package Servicios

import Excepciones.TareaNoAsignadaException
import Modelos.Equipo
import Modelos.Estado
import Modelos.Tarea
import Modelos.Usuario
import Repositorios.EquipoRepository
import Repositorios.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ResponseStatusException


@Service
class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository
    @Autowired
    private EquipoService equipoService

    Usuario crearUsuario(Usuario usuario) {
        usuario.EquipoPrivado = crearNuevoEquipo("${usuario.NombreUsuario}_Privado", usuario)
        usuarioRepository.save(usuario)
        return usuario
    }

    private Equipo crearNuevoEquipo(String nombre, Usuario usuario) {
        def equipo = equipoService.crearEquipo(new Equipo(nombre, usuario))
        usuario.equipos += equipo
        return equipo
    }

    void completarTarea(Tarea tarea){
        if (tarea.getAsignado() != this){
            throw new TareaNoAsignadaException()
        }
        tarea.setEstado(Estado.Completado)
    }

    Usuario getUsuario(String nombreUsuario) throws Throwable {
        def usuario = UsuarioRepository.findByNombreUsuario(nombreUsuario)
        if (!usuario)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario con nombre de usuario ${nombreUsuario} no encontrado")
        return usuario
    }
}
