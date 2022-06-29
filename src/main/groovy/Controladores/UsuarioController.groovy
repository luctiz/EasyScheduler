package Controladores

import Modelos.Usuario
import Servicios.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class UsuarioController extends ApiControllerBase {
    @Autowired
    private UsuarioService service


    @GetMapping("/usuario/nombreUsuario")
    @ResponseStatus(HttpStatus.FOUND)
    String getUsuario(String nombreUsuario) throws Throwable {
        return service.getUsuario(nombreUsuario)
    }

    @PostMapping("/usuario")
    @ResponseStatus(HttpStatus.CREATED)
    String crearUsuario(@RequestBody Usuario usuario) throws Throwable {
        return service.crearUsuario(usuario)
    }

}
