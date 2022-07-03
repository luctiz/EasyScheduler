package Controladores

import Modelos.Equipo
import Modelos.Usuario
import Servicios.EquipoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping("/equipo/{nombre}")
    @ResponseStatus(HttpStatus.FOUND)
    Equipo getEquipo(@PathVariable String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre no puede ser vacio")
        return service.getEquipo(nombre)
    }

    @GetMapping("/equipo/miembros/{nombre}")
    @ResponseStatus(HttpStatus.FOUND)
    Usuario[] getMiembros(@PathVariable String nombre) {
        if (nombre.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre no puede ser vacio")
        return service.getMiembros(nombre)
    }

    @PostMapping("/equipo/{equipo}")
    @ResponseStatus(HttpStatus.CREATED)
    Equipo crearEquipo(@PathVariable String equipo, @RequestBody Usuario creador,@RequestBody Usuario[] miembros) {
        if (equipo.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre no puede ser vacio")
        if (!creador || creador.nombreUsuario.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre de usuario no puede ser vacio o null")
        if (!miembros || miembros.size() == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "miembros no puede ser vacio o null")
        return service.crearEquipo(equipo, creador, miembros)
    }

    @PutMapping("/equipo/agregarMiembro/{nombreUsuario}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Equipo agregarMiembro(@RequestBody Equipo equipo, @PathVariable String nombreUsuario) throws Throwable {
        if (!equipo || equipo.nombre.isAllWhitespace() || equipo.lider.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el nombre de equipo/lider no puede ser vacio, o null")
        return  service.agregarMiembro(equipo, nombreUsuario)
    }

    @PutMapping("equipo/modificarEquipo/{nombreUsuario}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Equipo modificarEquipo(@RequestBody Equipo equipo, @PathVariable String nombreUsuario) throws Throwable {
        if (!equipo || equipo.nombre.isAllWhitespace() || equipo.lider.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el nombre de equipo/lider no puede ser vacio, o null")
        return  service.modficarEquipo(equipo, nombreUsuario)
    }

    @PutMapping("equipo/removerMiembro/{nombreUsuario}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Equipo removerMiembro(@RequestBody Equipo equipo, @PathVariable String nombreUsuario, @RequestBody String[] miembrosARemover) throws Throwable {
        if (!equipo || equipo.nombre.isAllWhitespace() || equipo.lider.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el nombre de equipo/lider no puede ser vacio, o null")
        return  service.removerEquipo(equipo, nombreUsuario, miembrosARemover)
    }

    @DeleteMapping("equipo/{equipo}&{nombreUsuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void borrarEquipo(@PathVariable String equipo, @PathVariable String nombreUsuario) {
        if (equipo.isAllWhitespace() || nombreUsuario.isAllWhitespace())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "el nombre de equipo/lider no puede ser vacio, o null")
        service.borrarEquipo(equipo, nombreUsuario)
    }

}
