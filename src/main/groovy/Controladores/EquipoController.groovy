package Controladores

import Modelos.Equipo
import Modelos.Usuario
import Servicios.EquipoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class EquipoController extends ApiControllerBase {

    @Autowired
    private EquipoService service

    @GetMapping("/equipo/nombre")
    @ResponseStatus(HttpStatus.FOUND)
    Equipo getEquipo(@RequestBody String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre no puede ser vacio")
        return service.getEquipo(nombre)
    }

    @GetMapping("/equipo/miembros")
    @ResponseStatus(HttpStatus.FOUND)
    Usuario[] getMiembros(@RequestBody String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre no puede ser vacio")
        return service.getMiembros(nombre)
    }

    @PostMapping("/equipo")
    @ResponseStatus(HttpStatus.CREATED)
    Equipo crearEquipo(@RequestBody String equipo, Usuario creador, Usuario[] miembros) throws Throwable {
        if (equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre no puede ser vacio")
        if (!creador || creador.nombreUsuario.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre de usuario no puede ser vacio o null")
        if (!miembros || miembros.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "miembros no puede ser vacio o null")
        return service.crearEquipo(equipo, creador, miembros)
    }

    @PutMapping("/equipo/agregarMiembro")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Equipo agregarMiembro(@RequestBody Equipo equipo, String NombreUsuario) throws Throwable {
        if (!equipo || equipo.nombre.isAllWhitespace() || equipo.lider.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el nombre de equipo/lider no puede ser vacio, o null")
        return  service.agregarMiembro(equipo, NombreUsuario)
    }
}
