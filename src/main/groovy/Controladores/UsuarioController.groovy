package Controladores

import Modelos.Usuario
import Servicios.UsuarioService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class UsuarioController extends ApiControllerBase {

    @Autowired
    private UsuarioService service


    @GetMapping("/usuario/{nombreUsuario}")
    @ResponseStatus(HttpStatus.OK)
    Usuario getUsuario(@PathVariable String nombreUsuario) {
        if (nombreUsuario.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre de usuario no puede ser vacio")
        return service.getUsuario(nombreUsuario)
    }

    @PostMapping("/usuario/{nombreUsuario}&{contrasenia}")
    @ResponseStatus(HttpStatus.CREATED)
    Usuario crearUsuario(@PathVariable String nombreUsuario, @PathVariable String contrasenia) throws Throwable {
        if (nombreUsuario.isAllWhitespace() || contrasenia.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre de usuario/contraseña no puede ser vacio")
        return service.crearUsuario(new Usuario(nombreUsuario, contrasenia, ObjectId.get()))
    }

//    @GetMapping("/usuario")
//    @ResponseStatus(HttpStatus.OK)
//    Usuario[] getUsuarios(Usuario[] usuarios) {
//        if (!usuarios || usuarios.size())
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarios no pueden ser null o vacios")
//        return service.getUsuarios(usuarios)
//    }

    @PatchMapping("/usuario")
    @ResponseStatus(HttpStatus.OK)
    Usuario modificarUsuario(@RequestBody Usuario usuario) {
        return service.modificarUsuario(usuario)
    }

    @DeleteMapping("/usuario/{nombreUsuario}&{contrasenia}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void borrarUsuario(@PathVariable String nombreUsuario, @PathVariable String contrasenia) {
        service.borrarUsuario(nombreUsuario, contrasenia)
    }
}
