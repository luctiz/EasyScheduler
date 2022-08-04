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
import org.mockito.internal.configuration.injection.MockInjection

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
    void crearEvento() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = eventoService.crearEvento(evento, usuario.nombreUsuario)
        Mockito.when(eventoRepository.findByNombreFecha(evento.nombreFecha)).thenReturn(evento)
        def e = eventoService.getEvento(evento.nombreFecha)
        assert evento.nombreFecha.contains("${e.nombre}${fecha.format("yyyyMMdd")}")
        assert e.tareas.size() == 0
    }

    @Test
    void crearEventoInvalido() {
        GroovyAssert.shouldFail {
            evento = new Evento("evento", LocalDate.parse("2021-07-01"), equipo.nombre, ObjectId.get())
        }
    }

    @Test
    void agregarEventoAEquipoSiendoLiderEsValido(){
        def usuario2 = usuarioService.crearUsuario(new Usuario("user2", "sasd", ObjectId.get()))
        Usuario[] usuarios = [usuario, usuario2]
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario2.nombreUsuario)).thenReturn(usuario2)
        Mockito.when(usuarioRepository.saveAll(usuarios.toList())).thenReturn(usuarios.toList())
        equipoService.agregarMiembro(equipo.nombre, usuario2.nombreUsuario)
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn(usuarios)
        def evento2 = new Evento("eventoequipo",LocalDate.now(), equipo.nombre, ObjectId.get())
        Mockito.when(eventoRepository.save(evento2)).thenReturn(evento2)
        def evento_creado = eventoService.crearEvento(evento2, usuario.nombreUsuario)
        assert(evento_creado.nombre == evento2.nombre)
    }

    @Test
    void agregarEventoAEquipoSinSerLiderEsInvalido() {
        def usuario2 = usuarioService.crearUsuario(new Usuario("user22", "sasd", ObjectId.get()))
        Usuario[] usuarios = [usuario, usuario2]
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario2.nombreUsuario)).thenReturn(usuario2)
        Mockito.when(usuarioRepository.saveAll(usuarios.toList())).thenReturn(usuarios.toList())
        equipoService.agregarMiembro(equipo.nombre, usuario2.nombreUsuario)
        def fecha = LocalDate.now()
        GroovyAssert.shouldFail {
            def _evento = new Evento("eventoequipo", fecha, equipo.nombre, ObjectId.get())
            eventoService.crearEvento(_evento, usuario2.nombreUsuario)
        }
    }

    @Test
    void getEventosByFechaOrEquipo() {
        def evento2 = new Evento("evento2", LocalDate.now().atStartOfDay().plusDays(1).toLocalDate(), equipo.nombre, ObjectId.get())
        def evento3 = new Evento("evento3", LocalDate.now().atStartOfDay().plusDays(2).toLocalDate(), equipo.nombre, ObjectId.get())
        Mockito.when(eventoRepository.save(evento2)).thenReturn(evento2)
        Mockito.when(eventoRepository.save(evento3)).thenReturn(evento3)
        evento2 = eventoService.crearEvento(evento2, usuario.nombreUsuario)
        evento3 = eventoService.crearEvento(evento3, usuario.nombreUsuario)
        Mockito.when(eventoRepository.findByFechaBetween(evento.fecha, evento2.fecha)).thenReturn([evento, evento2] as Evento[])
        def eventos = eventoService.getEventosByFechas(evento.fecha, evento2.fecha)
        assert (eventos.contains(evento))
        assert (eventos.contains(evento2))
        assert (!eventos.contains(evento3))
        Mockito.when(eventoRepository.findByEquipo(equipo.nombre)).thenReturn([evento, evento2, evento3] as Evento[])
        def eventos2 = eventoService.getEventosByEquipo(evento.equipo)
        assert (eventos2.contains(evento))
        assert (eventos2.contains(evento2))
        assert (eventos2.contains(evento3))
    }

    @Test
    void getEventosByFechayEquipoOrNombre() {
        def usuario2 = usuarioService.crearUsuario(new Usuario("user222", "sasd", ObjectId.get()))
        def equipo2 = usuario2.equipos.first()
        Mockito.when(usuarioRepository.findByNombreUsuario("user222")).thenReturn(usuario2)
        Mockito.when(usuarioRepository.findByEquipos("${usuario2.nombreUsuario}_Privado")).thenReturn([usuario2] as Usuario[])
        def evento2 = new Evento("evento22", LocalDate.now().atStartOfDay().plusDays(1).toLocalDate(), equipo2.nombre, ObjectId.get())
        Mockito.when(eventoRepository.save(evento2)).thenReturn(evento2)
        Mockito.when(eventoRepository.findByEquipoAndFechaBetween(equipo2.nombre, evento.fecha, evento2.fecha)).thenReturn([evento2] as Evento[])
        def eventos = eventoService.getEventosByFechasAndEquipo(evento.fecha, evento2.fecha, equipo2.nombre)
        assert (!eventos.contains(evento))
        assert (eventos.contains(evento2))
        Mockito.when(eventoRepository.findByNombreLike(evento.nombre)).thenReturn([evento, evento2] as Evento[])
        def eventos2 = eventoService.getEventosByNombre(evento.nombre)
        assert (eventos2.contains(evento))
        assert (eventos2.contains(evento2))

    }

    @Test
    void borrarEvento() {
        def evento2 = new Evento("evento2", LocalDate.now().atStartOfDay().plusDays(1).toLocalDate(), equipo.nombre, ObjectId.get())
        def evento3 = new Evento("evento3", LocalDate.now().atStartOfDay().plusDays(2).toLocalDate(), equipo.nombre, ObjectId.get())
        Mockito.when(eventoRepository.save(evento2)).thenReturn(evento2)
        Mockito.when(eventoRepository.save(evento3)).thenReturn(evento3)
        Mockito.when(eventoRepository.findByNombreFecha(evento.nombreFecha)).thenReturn(evento)
        Mockito.when(eventoRepository.findByNombreFecha(evento2.nombreFecha)).thenReturn(evento2)
        Mockito.when(eventoRepository.findByNombreFecha(evento3.nombreFecha)).thenReturn(evento3)
        evento2 = eventoService.crearEvento(evento2, usuario.nombreUsuario)
        evento3 = eventoService.crearEvento(evento3, usuario.nombreUsuario)
        eventoService.borrarEvento(evento.nombreFecha, usuario.nombreUsuario)
        eventoService.borrarEvento(evento2.nombreFecha, usuario.nombreUsuario)
        eventoService.borrarEvento(evento3.nombreFecha, usuario.nombreUsuario)
    }

    @Test
    void borrarEventoConUsuarioNoLiderShouldFail() {
        def usuario2 = new Usuario("user3", "adsa", ObjectId.get())
        Mockito.when(usuarioRepository.save(usuario2)).thenReturn(usuario2)
        usuario2 = usuarioService.crearUsuario(usuario2)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario2.nombreUsuario)).thenReturn(usuario2)
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn([usuario] as Usuario[])
        Mockito.when(usuarioRepository.save(usuario2)).thenReturn(usuario2)
        equipoService.agregarMiembro(equipo.nombre, usuario2.nombreUsuario)
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn([usuario, usuario2] as Usuario[])
        def evento2 = new Evento("evento2", LocalDate.now().atStartOfDay().plusDays(1).toLocalDate(), equipo.nombre, ObjectId.get())
        def evento3 = new Evento("evento3", LocalDate.now().atStartOfDay().plusDays(2).toLocalDate(), equipo.nombre, ObjectId.get())
        Mockito.when(eventoRepository.save(evento2)).thenReturn(evento2)
        Mockito.when(eventoRepository.save(evento3)).thenReturn(evento3)
        Mockito.when(eventoRepository.findByNombreFecha(evento.nombreFecha)).thenReturn(evento)
        Mockito.when(eventoRepository.findByNombreFecha(evento2.nombreFecha)).thenReturn(evento2)
        Mockito.when(eventoRepository.findByNombreFecha(evento3.nombreFecha)).thenReturn(evento3)
        evento = eventoService.crearEvento(evento, usuario.nombreUsuario)
        evento2 = eventoService.crearEvento(evento2, usuario.nombreUsuario)
        evento3 = eventoService.crearEvento(evento3, usuario.nombreUsuario)
        GroovyAssert.shouldFail {
            eventoService.borrarEvento(evento.nombreFecha, usuario2.nombreUsuario)
            eventoService.borrarEvento(evento2.nombreFecha, usuario2.nombreUsuario)
            eventoService.borrarEvento(evento3.nombreFecha, usuario2.nombreUsuario)
        }
    }

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
