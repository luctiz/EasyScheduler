package Controladores

import Modelos.Equipo
import Modelos.Usuario
import Servicios.EquipoService
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class EquipoController extends ApiControllerBase {

    @Autowired
    private EquipoService service

    @PostMapping("/equipo")
    @ResponseStatus(HttpStatus.CREATED)
    Equipo crearEquipo(@RequestBody Equipo equipo) throws Throwable {
        def equipo_creado = service.crearEquipo(equipo)
        return created(equipo_creado)
    }

    @PatchMapping("/equipo/agregarMiembro")
    @ResponseStatus(HttpStatus.ACCEPTED)
    Equipo agregarMiembro(@RequestBody Equipo equipo, String nombreUsuario) throws Throwable {
        def equipo_actualizado = service.agregarMiembro(equipo, nombreUsuario)
        return updated(equipo_actualizado)

    }


}
