package Controladores

import Modelos.Equipo
import Modelos.Usuario
import Servicios.EquipoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@CrossOrigin(origins = "*")
@RestController
class EquipoController extends ApiControllerBase {

    @Autowired
    private EquipoService service

    @GetMapping("/equipo/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    Equipo getEquipo(@PathVariable String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre no puede ser vacio")
        return service.getEquipo(nombre)
    }

    @GetMapping("/equipo/miembros/{nombre}")
    @ResponseStatus(HttpStatus.OK)
    Usuario[] getMiembros(@PathVariable String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre no puede ser vacio")
        return service.getMiembros(nombre)
    }

    @PutMapping("/equipo/{equipo}&{creador}")
    @ResponseStatus(HttpStatus.OK)
    Equipo crearEquipo(@PathVariable String equipo, @PathVariable String creador, @RequestBody String[] miembros) {
        if (equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre no puede ser vacio")
        if (creador.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre de usuario no puede ser vacio o null")
        if (!miembros)
            miembros = []
        return service.crearEquipo(equipo, creador, miembros)
    }

    @PatchMapping("/equipo/agregarMiembro/{equipo}&{nombreUsuario}")
    @ResponseStatus(HttpStatus.OK)
    Usuario[] agregarMiembro(@PathVariable String equipo, @PathVariable String nombreUsuario) {
        if (equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el nombre de equipo no puede ser vacio, o null")
        return service.agregarMiembro(equipo, nombreUsuario)
    }

    @PatchMapping("/equipo/modificarEquipo/{nombreUsuario}")
    @ResponseStatus(HttpStatus.OK)
    Equipo modificarEquipo(@RequestBody Equipo equipo, @PathVariable String nombreUsuario) {
        if (!equipo || equipo.nombre.isAllWhitespace() || equipo.lider.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el nombre de equipo/lider no puede ser vacio, o null")
        return  service.modficarEquipo(equipo, nombreUsuario)
    }

    @PutMapping("/equipo/removerMiembro/{equipo}&{nombreUsuario}")
    @ResponseStatus(HttpStatus.OK)
    Usuario[] removerMiembro(@PathVariable String equipo, @PathVariable String nombreUsuario, @RequestBody String[] miembrosARemover) {
        if (!equipo || equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el nombre de equipo/lider no puede ser vacio, o null")
        return  service.removerEquipo(equipo, nombreUsuario, miembrosARemover)
    }

    @DeleteMapping("/equipo/{equipo}&{nombreUsuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void borrarEquipo(@PathVariable String equipo, @PathVariable String nombreUsuario) {
        if (equipo.isAllWhitespace() || nombreUsuario.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el nombre de equipo/lider no puede ser vacio, o null")
        service.borrarEquipo(equipo, nombreUsuario)
    }

}
