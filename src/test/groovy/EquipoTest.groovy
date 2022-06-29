import Controladores.EquipoController
import Modelos.Equipo
import Modelos.Evento
import Modelos.Usuario
import Repositorios.EquipoRepository
import Repositorios.UsuarioRepository
import Servicios.EquipoService
import Servicios.UsuarioService
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.test.GroovyAssert
import org.junit.jupiter.api.BeforeAll
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


class EquipoTest {

    private static UsuarioRepository usuarioRepository
    private static EquipoService equipoService
    private static UsuarioService usuarioService

    static def usuario = new Usuario( "user12",  "pass")
    static def usuario2 = new Usuario("usuario2", "123")
    static def usuario3 = new Usuario("usuario3", "123")


    @BeforeAll
    static void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class)
        usuarioService = new UsuarioService(usuarioRepository:  usuarioRepository, equipoService: equipoService)
        equipoService = new EquipoService(usuarioRepository: usuarioRepository, usuarioService: usuarioService)
        // de nuevo porque equipoService es null primero
        usuarioService = new UsuarioService(usuarioRepository:  usuarioRepository, equipoService: equipoService)
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario)
        Mockito.when(usuarioRepository.save(usuario2)).thenReturn(usuario2)
        Mockito.when(usuarioRepository.save(usuario3)).thenReturn(usuario3)
        usuario = usuarioService.crearUsuario(usuario)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario.NombreUsuario)).thenReturn(usuario)
        usuario2 = usuarioService.crearUsuario(usuario2)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario2.NombreUsuario)).thenReturn(usuario2)
        usuario3 = usuarioService.crearUsuario(usuario3)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario3.NombreUsuario)).thenReturn(usuario3)
    }


    @Test
    void CrearEquipoValido() {
        def equipo = new Equipo("equipo", usuario.NombreUsuario)
        Usuario[] usuarios = [usuario]
        String[] nombreUsuarios = [usuario.NombreUsuario]
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario.NombreUsuario)).thenReturn(usuario)
        Mockito.when(usuarioRepository.findAllByNombreUsuario(nombreUsuarios)).thenReturn(usuarios)
        def equipoNuevo = equipoService.crearEquipo(equipo, usuario, usuarios)
        assert(equipoNuevo.Nombre == "trabajo")
        assert(equipoNuevo.getLider() == usuario)
    }

    @Test
    void AgregarMiembroAEquipo() {
        Usuario[] usuarios = [usuario, usuario2, usuario3]
        String[] nombreUsuarios = [usuario.NombreUsuario, usuario2.NombreUsuario, usuario3.NombreUsuario]
        Mockito.when(usuarioRepository.findByEquipos(equipo)).thenReturn(usuarios)
        Mockito.when(usuarioRepository.findAllByNombreUsuario(nombreUsuarios)).thenReturn(usuarios)
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
