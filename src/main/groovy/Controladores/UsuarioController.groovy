package Controladores

import Modelos.Equipo
import Modelos.Usuario
import Servicios.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class UsuarioController extends ApiControllerBase {
    @Autowired
    private UsuarioService service


    @GetMapping("/usuario/nombreUsuario")
    @ResponseStatus(HttpStatus.FOUND)
    Usuario getUsuario(String nombreUsuario) throws Throwable {
        return service.getUsuario(nombreUsuario)
    }

    @PostMapping("/usuario")
    @ResponseStatus(HttpStatus.CREATED)
    Usuario crearUsuario(@RequestBody Usuario usuario) throws Throwable {
        return service.crearUsuario(usuario)
    }

}
