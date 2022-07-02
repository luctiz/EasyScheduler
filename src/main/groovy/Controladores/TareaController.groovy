package Controladores

import Modelos.Equipo
import Servicios.EquipoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class TareaController extends ApiControllerBase {

    @Autowired
    private EquipoService service

//    @PostMapping("/equipo")
//    @ResponseStatus(HttpStatus.CREATED)
//    Equipo crearEquipo(@RequestBody Equipo equipo) throws Throwable {
//        def equipo_creado = service.crearEquipo(equipo)
//        return created(equipo_creado)
//    }

//    @PutMapping("/equipo/agregarMiembro")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    Equipo agregarMiembro(@RequestBody Equipo equipo, String NombreUsuario) throws Throwable {
//        def equipo_actualizado = service.agregarMiembro(equipo, NombreUsuario)
//        return updated(equipo_actualizado)
//
//    }


}
