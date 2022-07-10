import Modelos.Equipo
import Modelos.Usuario
import Repositorios.UsuarioRepository
import Servicios.EquipoService
import Servicios.UsuarioService
import groovy.test.GroovyAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class EquipoTest {

    private static UsuarioRepository usuarioRepository
    private static EquipoService equipoService
    private static UsuarioService usuarioService

    def usuario = new Usuario( "user12",  "pass")
    def usuario2 = new Usuario("usuario2", "123")
    def usuario3 = new Usuario("usuario3", "123")
    Usuario[] usuarios = [usuario]
    def equipo = new Equipo("equipo", usuario.nombreUsuario)

    @BeforeEach
    void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class)
        usuarioService = new UsuarioService(usuarioRepository:  usuarioRepository)
        equipoService = new EquipoService(usuarioRepository: usuarioRepository, usuarioService: usuarioService)
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario)
        Mockito.when(usuarioRepository.save(usuario2)).thenReturn(usuario2)
        Mockito.when(usuarioRepository.save(usuario3)).thenReturn(usuario3)
        usuario = usuarioService.crearUsuario(usuario)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario.nombreUsuario)).thenReturn(usuario)
        usuario2 = usuarioService.crearUsuario(usuario2)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario2.nombreUsuario)).thenReturn(usuario2)
        usuario3 = usuarioService.crearUsuario(usuario3)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario3.nombreUsuario)).thenReturn(usuario3)
        Mockito.when(usuarioRepository.saveAll(usuarios.iterator() as Iterable<Usuario>)).thenReturn(usuarios as List<Usuario>)
        equipo = equipoService.crearEquipo("equipo",usuario.nombreUsuario, usuarios)
    }


    @Test
    void CrearEquipoValido() {
        assert(equipo.nombre == "equipo")
        assert(equipo.lider == usuario.nombreUsuario)
    }

    @Test
    void AgregarMiembroAEquipo() {
        usuario2.equipos = []
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        equipo = equipoService.agregarMiembro(equipo, usuario2.nombreUsuario)
        usuarios += usuario2
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        def miembros = equipoService.getMiembros(equipo.nombre)
        assert(miembros.size() == 2)
        assert(miembros.contains(usuario))
        assert(miembros.contains(usuario2))

    }

    @Test
    void AgregarMiembroYaExistenteFalla() {
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        GroovyAssert.shouldFail {
            equipoService.agregarMiembro(equipo, usuario.nombreUsuario)
        }
        equipoService.agregarMiembro(equipo, usuario2.nombreUsuario)
        usuarios += usuario2
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        GroovyAssert.shouldFail {
            equipoService.agregarMiembro(equipo, usuario2.nombreUsuario)
        }
        usuarios = equipoService.getMiembros(equipo.nombre)
        assert(usuarios.size() == 2)
        assert(usuarios.contains(usuario))
        assert(usuarios.contains(usuario2))
    }

//    @Test
//    void AgregarEventoAEquipoSiendoLiderEsValido(){
//        Usuario[] usuarios = [usuario]
//        Mockito.when(usuarioRepository.saveAll(usuarios.iterator() as Iterable<Usuario>)).thenReturn(usuarios as List<Usuario>)
//        def equipo = new Equipo("equipo", usuario.NombreUsuario)
//        equipo = equipoService.crearEquipo("equipo",usuario, usuarios)
//        Mockito.when(usuarioRepository.findByEquipos(equipo.Nombre)).thenReturn(usuarios)
//        equipoService.agregarMiembro(equipo, usuario2.NombreUsuario)
//        def fecha = LocalDate.parse("2022-07-01")
//        def evento = new Evento("eventoequipo",fecha,equipo,usuario)
//
//        evento.addTarea(
//                1,
//                "tarea1",
//                LocalTime.parse('01:01:01.01'),
//                LocalTime.parse('02:01:01.01'),
//                usuario,
//                usuario
//        )
//
//        assert(evento.getTareas().size() == 1)
//
//
//    }

//    @Test
//    void AgregarEventoAEquipoSinSerLiderEsInvalido(){
//        def equipo = usuarioService.crearNuevoEquipo("trabajo", usuario)
//        equipoService.agregarMiembro(equipo, usuario2)
//        def fecha = LocalDate.parse("2022-07-01")
//        GroovyAssert.shouldFail {
//            def _evento = new Evento("eventoequipo", fecha, equipo, usuario2)
//        }
//    }
//
//    @Test
//    void AgregarTareaAEventoSinSerLiderEsInvalido(){
//        def equipo = usuario.crearNuevoEquipo("trabajo")
//        equipo.agregarMiembro(usuario2)
//        def fecha = LocalDate.parse("2022-07-01")
//        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
//
//
//        GroovyAssert.shouldFail {
//            evento.addTarea(
//                    1,
//                    "tarea1",
//                    LocalTime.parse('01:01:01.01'),
//                    LocalTime.parse('02:01:01.01'),
//                    usuario2,
//                    usuario2
//            )
//        }
//    }
//
//    @Test
//    void AsignarTareaAMiembroSinSerLiderEsInvalido(){
//        def equipo = usuario.crearNuevoEquipo("trabajo")
//        equipo.agregarMiembro(usuario2)
//        def fecha = LocalDate.parse("2022-07-01")
//        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
//        def tarea = evento.addTarea(
//                1,
//                "tarea1",
//                LocalTime.parse('01:01:01.01'),
//                LocalTime.parse('02:01:01.01'),
//                usuario,
//                usuario
//        )
//
//        GroovyAssert.shouldFail {
//            tarea.setAsignado(usuario2,usuario2)
//        }
//    }
//
//    @Test
//    void AsignarTareaANoMiembroDeEquipoSiendoLiderEsInvalido(){
//        def equipo = usuario.crearNuevoEquipo("trabajo")
//        equipo.agregarMiembro(usuario2)
//        def fecha = LocalDate.parse("2022-07-01")
//        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
//        def tarea = evento.addTarea(
//                1,
//                "tarea1",
//                LocalTime.parse('01:01:01.01'),
//                LocalTime.parse('02:01:01.01'),
//                usuario,
//                usuario
//        )
//
//        GroovyAssert.shouldFail {
//            tarea.setAsignado(usuario,usuario3)
//        }
//    }
//
//    @Test
//    void AsignarTareaAMiembroDeEquipoSiendoLiderEsValido(){
//        def equipo = usuario.crearNuevoEquipo("trabajo")
//        equipo.agregarMiembro(usuario2)
//        def fecha = LocalDate.parse("2022-07-01")
//        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
//        def tarea = evento.addTarea(
//                1,
//                "tarea1",
//                LocalTime.parse('01:01:01.01'),
//                LocalTime.parse('02:01:01.01'),
//                usuario,
//                usuario
//        )
//
//        tarea.setAsignado(usuario, usuario2)
//        assert(tarea.getAsignado() == usuario2)
//    }

}
