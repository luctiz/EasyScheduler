import Modelos.Equipo
import Modelos.Evento
import Modelos.Usuario
import Repositorios.EventoRepository
import Repositorios.UsuarioRepository
import Servicios.EquipoService
import Servicios.EventoService
import Servicios.UsuarioService
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import groovy.test.GroovyAssert
import org.mockito.Mockito

import java.time.LocalDate

class EventoTest {
    private static Equipo equipo
    private static Usuario usuario
    private static Evento evento
    private static LocalDate fecha
    private static EventoRepository eventoRepository
    private static UsuarioRepository usuarioRepository
    private static EquipoService equipoService
    private static EventoService eventoService
    private static UsuarioService usuarioService

    @BeforeAll
    static void setUp() {
        eventoRepository = Mockito.mock(EventoRepository.class)
        usuarioRepository = Mockito.mock(UsuarioRepository.class)
        usuarioService = new UsuarioService(usuarioRepository: usuarioRepository)
        equipoService = new EquipoService(usuarioRepository: usuarioRepository, usuarioService: usuarioService)
        eventoService = new EventoService(eventoRepository:  eventoRepository, usuarioService: usuarioService, equipoService: equipoService)
        Mockito.when(usuarioRepository.findByNombreUsuario("user1")).thenReturn(null)
        usuario = usuarioService.crearUsuario(new Usuario("user1", "pass", ObjectId.get()))
        equipo = usuario.equipos.first()
        Mockito.when(usuarioRepository.findByNombreUsuario("user1")).thenReturn(usuario)
        Mockito.when(usuarioRepository.findByEquipos("${usuario.nombreUsuario}_Privado")).thenReturn([usuario] as Usuario[])
        fecha = LocalDate.now()
        evento = new Evento("evento", fecha, equipo.nombre, ObjectId.get())
    }


    @Test
    void CrearEvento() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = eventoService.crearEvento(evento, usuario.nombreUsuario)
        Mockito.when(eventoRepository.findByNombreFecha(evento.nombreFecha)).thenReturn(evento)
        def e = eventoService.getEvento(evento.nombreFecha)
        assert evento.nombreFecha.contains("${e.nombre}${fecha.format("yyyyMMdd")}")
        assert e.tareas.size() == 0
    }

    @Test
    void CrearEventoInvalido() {
        GroovyAssert.shouldFail {
            evento = new Evento("evento", LocalDate.parse("2021-07-01"), equipo, ObjectId.get())
        }
    }

    @Test
    void AgregarEventoAEquipoSiendoLiderEsValido(){
        def usuario2 = usuarioService.crearUsuario(new Usuario("user2", "sasd", ObjectId.get()))
        Usuario[] usuarios = [usuario, usuario2]
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario2.nombreUsuario)).thenReturn(usuario2)
        Mockito.when(usuarioRepository.saveAll(usuarios.toList())).thenReturn(usuarios.toList())
        equipoService.agregarMiembro(equipo, usuario2.nombreUsuario)
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        def evento2 = new Evento("eventoequipo",LocalDate.now(), equipo.nombre, ObjectId.get())
        Mockito.when(eventoRepository.save(evento2)).thenReturn(evento2)
        def evento_creado = eventoService.crearEvento(evento2, usuario.nombreUsuario)
        assert(evento_creado.nombre == evento2.nombre)


    }

    @Test
    void AgregarEventoAEquipoSinSerLiderEsInvalido(){
        def usuario2 = usuarioService.crearUsuario(new Usuario("user22", "sasd", ObjectId.get()))
        Usuario[] usuarios = [usuario, usuario2]
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario2.nombreUsuario)).thenReturn(usuario2)
        Mockito.when(usuarioRepository.saveAll(usuarios.toList())).thenReturn(usuarios.toList())
        equipoService.agregarMiembro(equipo, usuario2.nombreUsuario)
        def fecha = LocalDate.now()
        GroovyAssert.shouldFail {
            def _evento = new Evento("eventoequipo", fecha, equipo, ObjectId.get())
            eventoService.crearEvento(_evento, usuario2.nombreUsuario)
        }
    }

//    @Test
//    void AgregarTareaAEvento() {
//        evento.addTarea(
//                1,
//                "tarea1",
//                new LocalTime(1,1,1,1),
//                new LocalTime(2,1,1,1),
//                new String(nombreUsuario: "user1",contraseña: "pass"),
//                usuario
//        )
//        evento.tareas.size() == 1
//    }
//
//    @Test
//    void AgregarTareaInvalidaAEvento() {
//        evento.addTarea(
//                1,
//                "tarea1",
//                new LocalTime(1,1,1,1),
//                new LocalTime(2,1,1,1),
//                new String(nombreUsuario: "user1",contraseña: "pass"),
//                usuario
//        )
//        GroovyAssert.shouldFail {
//            evento.addTarea(
//                    1,
//                    "tarea1",
//                    new LocalTime(1,1,1,1),
//                    new LocalTime(2,1,1,1),
//                    new String(nombreUsuario: "user1",contraseña: "pass"),
//                    usuario
//            )
//        }
//    }
//
//    @Test
//    void duplicarEventos() {
//        evento.addTarea(
//                1,
//                "tarea1",
//                new LocalTime(1, 1, 1, 1),
//                new LocalTime(2, 1, 1, 1),
//                new String(nombreUsuario: "user1", contraseña: "pass"),
//                usuario
//        )
//        def user2 = new String(nombreUsuario: "user2", contraseña: "pass")
//        def duplicado = DuplicarEvento.DuplicarEventos(evento, user2, user2.getEquipoPrivado())
//        assert duplicado.tareas.size() == 1
//    }
}
