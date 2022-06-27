package Servicios

import Excepciones.TareaNoAsignadaException
import Modelos.Equipo
import Modelos.Estado
import Modelos.Tarea
import Modelos.Usuario
import Repositorios.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    Usuario crearUsuario(Usuario usuario) {
        usuarioRepository.save(usuario)
        return usuario
    }

    Equipo crearNuevoEquipo(String nombre, Usuario usuario){
        def equipo = new Equipo(nombre, usuario)
        usuario.equipos += equipo
        return equipo
    }

    void completarTarea(Tarea tarea){
        if (tarea.getAsignado() != this){
            throw new TareaNoAsignadaException()
        }
        tarea.setEstado(Estado.Completado)
    }

}
