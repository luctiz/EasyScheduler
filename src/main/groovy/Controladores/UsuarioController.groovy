package Controladores

import Modelos.Equipo
import Modelos.Usuario
import Servicios.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class UsuarioController extends ApiControllerBase {
    @Autowired
    private UsuarioService service

    @PostMapping("/usuario")
    @ResponseStatus(HttpStatus.CREATED)
    Equipo crearEquipo(@RequestBody Usuario usuario) throws Throwable {
        return service.crearUsuario(usuario)
    }
}
