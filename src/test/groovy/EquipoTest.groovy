import Modelos.Equipo
import Modelos.Usuario
import Repositorios.UsuarioRepository
import Servicios.EquipoService
import Servicios.UsuarioService
import groovy.test.GroovyAssert
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class EquipoTest {

    private static UsuarioRepository usuarioRepository
    private static EquipoService equipoService
    private static UsuarioService usuarioService

    private static usuario = new Usuario( "user12",  "pass", ObjectId.get())
    private static usuario2 = new Usuario("usuario2", "123", ObjectId.get())
    private static usuario3 = new Usuario("usuario3", "123", ObjectId.get())
    private static Usuario[] usuarios = [usuario]
    private static String[] users = [usuario.nombreUsuario]
    private static equipo = new Equipo("equipo", usuario.nombreUsuario, ObjectId.get())

    @BeforeAll
    static void setUp() {
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
        Mockito.when(usuarioRepository.saveAll(usuarios.toList())).thenReturn(usuarios.toList())
        equipo = equipoService.crearEquipo("equipo",usuario.nombreUsuario, users)
    }


    @Test
    void crearEquipoValido() {
        assert(equipo.nombre == "equipo")
        assert(equipo.lider == usuario.nombreUsuario)
    }

    @Test
    void agregarMiembroAEquipo() {
        usuario2.equipos = []
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        equipo = equipoService.agregarMiembro(equipo, usuario2.nombreUsuario)
        usuarios += usuario2
        users += usuario2.nombreUsuario
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        def miembros = equipoService.getMiembros(equipo.nombre)
        assert(miembros.size() == 2)
        assert(miembros.contains(usuario))
        assert(miembros.contains(usuario2))

    }

    @Test
    void agregarMiembroYaExistenteFalla() {
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        GroovyAssert.shouldFail {
            equipoService.agregarMiembro(equipo, usuario.nombreUsuario)
        }
        usuarios = equipoService.getMiembros(equipo.nombre)
        assert(usuarios.size() == 2)
        assert(usuarios.contains(usuario))
        assert(usuarios.contains(usuario2))
    }

    @Test
    void borrarMiembros() {
        equipoService.agregarMiembro(equipo, usuario3.nombreUsuario)
        usuarios += usuario3
        users += usuario3.nombreUsuario
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        def usuarios =
        Mockito.when(usuarioRepository.saveAll())
        equipoService.removerEquipo(equipo.nombre, usuario.nombreUsuario, [usuario2.nombreUsuario, usuario3.nombreUsuario] as String[])
        GroovyAssert.shouldFail {
            equipoService.removerEquipo(equipo.nombre, usuario.nombreUsuario, [usuario2.nombreUsuario] as String[])
        }
    }


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
