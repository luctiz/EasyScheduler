import Controladores.EquipoController
import Controladores.UsuarioController
import Modelos.Evento
import Modelos.Usuario
import Repositorios.EquipoRepository
import Repositorios.UsuarioRepository
import Servicios.EquipoService
import Servicios.UsuarioService
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.test.GroovyAssert
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc

import java.time.LocalDate
import java.time.LocalTime


@ContextConfiguration(classes = [UsuarioService.class, EquipoService.class])
@WebAppConfiguration
@WebMvcTest(EquipoController.class)
class EquipoTest {



    @Autowired
    MockMvc mockMvc
    @Autowired
    ObjectMapper mapper
    @MockBean
    EquipoRepository equipoRepository
    @MockBean
    UsuarioRepository usuarioRepository
    @Autowired
    static EquipoService equipoService
    @Autowired
    static UsuarioService usuarioService

    static def usuario = new Usuario(NombreUsuario:  "user1", Contraseña:  "pass")
    static def usuario2 = new Usuario(NombreUsuario: "usuario2", Contraseña: "123")
    static def usuario3 = new Usuario(NombreUsuario: "usuario3", Contraseña: "123")


    @BeforeAll
    static void setUp() {
//        usuario = usuarioService.crearUsuario()
//        usuario2 = usuarioService.crearUsuario()
//        usuario3 = usuarioService.crearUsuario()
    }


    @Test
    void CrearEquipoValido() {
        Mockito.when(usuarioRepository.findByNombreUsuario())
        def equipoNuevo = usuarioService.crearNuevoEquipo("trabajo", usuario)

        assert(equipoNuevo.getNombre() == "trabajo")
        assert(equipoNuevo.getMiembros().size() == 1)
        assert(equipoNuevo.getLider() == usuario)
    }

    @Test
    void AgregarMiembroAEquipo() {

        def equipo = usuarioService.crearNuevoEquipo("trabajo", usuario)
        equipoService.agregarMiembro(equipo, usuario2)

        assert(equipo.getMiembros().size() == 2)
        assert(equipo.getMiembros().contains(usuario))
        assert(equipo.getMiembros().contains(usuario2))

    }

    @Test
    void AgregarMiembroYaExistenteFalla() {

        def equipo = usuarioService.crearNuevoEquipo("trabajo", usuario)
        GroovyAssert.shouldFail {
            equipoService.agregarMiembro(equipo, usuario)
        }
        equipoService.agregarMiembro(equipo, usuario2)
        GroovyAssert.shouldFail {
            equipoService.agregarMiembro(equipo, usuario2)
        }
        assert(equipo.getMiembros().size() == 2)
        assert(equipo.getMiembros().contains(usuario))
        assert(equipo.getMiembros().contains(usuario2))
    }

    @Test
    void AgregarEventoAEquipoSiendoLiderEsValido(){
        def equipo = usuarioService.crearNuevoEquipo("trabajo", usuario)
        equipoService.agregarMiembro(equipo, usuario2)
        def fecha = LocalDate.parse("2022-07-01")
        def evento = new Evento("eventoequipo",fecha,equipo,usuario)

        evento.addTarea(
                1,
                "tarea1",
                LocalTime.parse('01:01:01.01'),
                LocalTime.parse('02:01:01.01'),
                usuario,
                usuario
        )

        assert(evento.getTareas().size() == 1)


    }

    @Test
    void AgregarEventoAEquipoSinSerLiderEsInvalido(){
        def equipo = usuarioService.crearNuevoEquipo("trabajo", usuario)
        equipoService.agregarMiembro(equipo, usuario2)
        def fecha = LocalDate.parse("2022-07-01")
        GroovyAssert.shouldFail {
            def _evento = new Evento("eventoequipo", fecha, equipo, usuario2)
        }
    }

    @Test
    void AgregarTareaAEventoSinSerLiderEsInvalido(){
        def equipo = usuario.crearNuevoEquipo("trabajo")
        equipo.agregarMiembro(usuario2)
        def fecha = LocalDate.parse("2022-07-01")
        def evento = new Evento("eventoequipo", fecha, equipo, usuario)


        GroovyAssert.shouldFail {
            evento.addTarea(
                    1,
                    "tarea1",
                    LocalTime.parse('01:01:01.01'),
                    LocalTime.parse('02:01:01.01'),
                    usuario2,
                    usuario2
            )
        }
    }

    @Test
    void AsignarTareaAMiembroSinSerLiderEsInvalido(){
        def equipo = usuario.crearNuevoEquipo("trabajo")
        equipo.agregarMiembro(usuario2)
        def fecha = LocalDate.parse("2022-07-01")
        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
        def tarea = evento.addTarea(
                1,
                "tarea1",
                LocalTime.parse('01:01:01.01'),
                LocalTime.parse('02:01:01.01'),
                usuario,
                usuario
        )

        GroovyAssert.shouldFail {
            tarea.setAsignado(usuario2,usuario2)
        }
    }

    @Test
    void AsignarTareaANoMiembroDeEquipoSiendoLiderEsInvalido(){
        def equipo = usuario.crearNuevoEquipo("trabajo")
        equipo.agregarMiembro(usuario2)
        def fecha = LocalDate.parse("2022-07-01")
        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
        def tarea = evento.addTarea(
                1,
                "tarea1",
                LocalTime.parse('01:01:01.01'),
                LocalTime.parse('02:01:01.01'),
                usuario,
                usuario
        )

        GroovyAssert.shouldFail {
            tarea.setAsignado(usuario,usuario3)
        }
    }

    @Test
    void AsignarTareaAMiembroDeEquipoSiendoLiderEsValido(){
        def equipo = usuario.crearNuevoEquipo("trabajo")
        equipo.agregarMiembro(usuario2)
        def fecha = LocalDate.parse("2022-07-01")
        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
        def tarea = evento.addTarea(
                1,
                "tarea1",
                LocalTime.parse('01:01:01.01'),
                LocalTime.parse('02:01:01.01'),
                usuario,
                usuario
        )

        tarea.setAsignado(usuario, usuario2)
        assert(tarea.getAsignado() == usuario2)
    }

}
