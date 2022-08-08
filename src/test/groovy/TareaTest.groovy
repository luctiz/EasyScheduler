import Modelos.Equipo
import Modelos.Estado
import Modelos.Evento
import Modelos.Tarea
import Modelos.Usuario
import Repositorios.EventoRepository
import Repositorios.UsuarioRepository
import Servicios.EquipoService
import Servicios.EventoService
import Servicios.TareaService
import Servicios.UsuarioService
import groovy.test.GroovyAssert
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

import java.time.LocalDate
import java.time.LocalTime

class TareaTest {


    LocalTime horaInicio2
    LocalTime horaInicio = LocalTime.parse("21:20:12.1")
    LocalTime horaFin = LocalTime.parse("22:20:13.1")
    LocalTime horaFin2 = LocalTime.parse("23:20:13.1")
    Usuario usuario = new Usuario(
            "user1",
            "pass",
            ObjectId.get()
    )
    Usuario usuario2 = new Usuario(
            "user2",
            "pass",
            ObjectId.get()
    )
    Usuario usuario3 = new Usuario(
            "user3",
            "pass",
            ObjectId.get()
    )
    Usuario usuario4 = new Usuario(
            "user4",
            "pass",
            ObjectId.get()
    )
    Equipo equipo = new Equipo(
            "equipo",
            usuario.nombreUsuario,
            ObjectId.get()
    )
    Equipo equipo2 = new Equipo(
            "equipo2",
            usuario3.nombreUsuario,
            ObjectId.get()
    )
    Evento evento = new Evento(
            "evento1",
            LocalDate.now(),
            equipo.nombre,
            ObjectId.get()
    )
    Evento evento2 = new Evento(
            "evento2",
            LocalDate.now(),
            equipo.nombre,
            ObjectId.get()
    )
    Tarea tarea = new Tarea(
            "tarea1",
            "desc1",
            horaInicio,
            horaFin,
            usuario.nombreUsuario,
            ObjectId.get(),
            5
    )
    Tarea tarea2 = new Tarea(
            "tarea2",
            "desc1",
            horaInicio2,
            horaFin2,
            usuario2.nombreUsuario,
            ObjectId.get(),
            4
    )
    Tarea tarea3 = new Tarea(
            "tarea3",
            "desc1",
            horaInicio,
            horaFin,
            usuario3.nombreUsuario,
            ObjectId.get(),
            3
    )

    UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class)
    EventoRepository eventoRepository = Mockito.mock(EventoRepository.class)
    UsuarioService usuarioService = new UsuarioService(usuarioRepository: usuarioRepository)
    EquipoService equipoService = new EquipoService(usuarioRepository: usuarioRepository, usuarioService: usuarioService)
    EventoService eventoService = new EventoService(usuarioService: usuarioService, eventoRepository: eventoRepository, equipoService: equipoService)
    TareaService tareaService = new TareaService(usuarioService: usuarioService, eventoRepository: eventoRepository, equipoService: equipoService, eventoService: eventoService)


    @BeforeEach
    void setup() {
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario)
        Mockito.when(usuarioRepository.save(usuario2)).thenReturn(usuario2)
        Mockito.when(usuarioRepository.save(usuario3)).thenReturn(usuario3)
        usuario = usuarioService.crearUsuario(usuario)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario.nombreUsuario)).thenReturn(usuario)
        usuario2 = usuarioService.crearUsuario(usuario2)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario2.nombreUsuario)).thenReturn(usuario2)
        usuario3 = usuarioService.crearUsuario(usuario3)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario3.nombreUsuario)).thenReturn(usuario3)
        usuario4 = usuarioService.crearUsuario(usuario4)
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario4.nombreUsuario)).thenReturn(usuario4)
        def usuarios = [usuario2, usuario, usuario4]
        Mockito.when(usuarioRepository.saveAll(usuarios)).thenReturn(usuarios)
        equipo = equipoService.crearEquipo(equipo.nombre, equipo.lider, [usuario.nombreUsuario, usuario2.nombreUsuario, usuario4.nombreUsuario] as String[])
        usuario.equipos += equipo
        usuario2.equipos += equipo
        usuarios = [usuario3]
        Mockito.when(usuarioRepository.saveAll(usuarios)).thenReturn(usuarios)
        equipo2 = equipoService.crearEquipo(equipo2.nombre, equipo2.lider, [usuario3.nombreUsuario] as String[])
        Mockito.when(usuarioRepository.findByEquipos(equipo.nombre)).thenReturn([usuario, usuario2] as Usuario[])
        Mockito.when(usuarioRepository.findByEquipos(equipo2.nombre)).thenReturn([usuario3] as Usuario[])
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = eventoService.crearEvento(evento, usuario.nombreUsuario)
        Mockito.when(eventoRepository.findByNombreFecha(evento.nombreFecha)).thenReturn(evento)
    }


    @Test
    void CrearTarea() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = tareaService.agregarTarea(evento.nombreFecha, tarea)
        assert (evento.tareas.size() == 1)
        assert (evento.tareas.first().nombre == tarea.nombre)
    }

    @Test
    void CrearTareaHoraInvalida() {
        GroovyAssert.shouldFail {
            def _t = new Tarea(
                    "tarea",
                    "swax",
                    LocalTime.parse("22:20:13.1"),
                    LocalTime.parse("21:20:12.1"),
                    usuario.nombreUsuario,
                    ObjectId.get()
            )
            def _t2 = new Tarea(
                    "tarea",
                    "swax",
                    LocalTime.parse("22:20:13.1"),
                    LocalTime.parse("21:20:12.1"),
                    usuario.nombreUsuario,
                    ObjectId.get(),
                    -1
            )
        }
    }

    @Test
    void CompletarTareaAsignadaUsuario() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = tareaService.agregarTarea(evento.nombreFecha, tarea)
        evento = tareaService.modificarEstado(evento.nombreFecha, tarea.nombre, Estado.Completado, usuario.nombreUsuario)
        assert (tarea.estado == Estado.Completado)
    }

    @Test
    void CompletarTareaNoAsignadaEsInvalido() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = tareaService.agregarTarea(evento.nombreFecha, tarea)
        GroovyAssert.shouldFail {
            evento = tareaService.modificarEstado(evento.nombreFecha, tarea.nombre, Estado.Completado, usuario2.nombreUsuario)
        }
    }

    @Test
    void ModificarTarea() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = tareaService.agregarTarea(evento.nombreFecha, tarea)
        tarea.asignado = "user2"
        tarea.nombre = "nnn"
        tarea.horaFin = LocalTime.parse("22:20:12.1")
        tarea.horaInicio = LocalTime.parse("21:20:12.1")
        tarea.descripcion = "dsdsa"
        tarea.peso = 3
        evento = tareaService.modficarTareas(evento.nombreFecha, [tarea] as Tarea[])
        assert (evento.tareas.find { t -> t.nombre == tarea.nombre })
        GroovyAssert.shouldFail {
            tarea.asignado = "user3"
            evento = tareaService.modficarTareas(evento.nombreFecha, [tarea] as Tarea[])
            tarea.peso = -1
            evento = tareaService.modficarTareas(evento.nombreFecha, [tarea] as Tarea[])
            tarea.horaInicio = LocalTime.parse("23:20:12.1")
            evento = tareaService.modficarTareas(evento.nombreFecha, [tarea] as Tarea[])
        }

    }

    @Test
    void borrarTareas() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = tareaService.agregarTareas(evento.nombreFecha, [tarea, tarea2] as Tarea[])
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        assert(evento.tareas.size() == 2)
        evento = tareaService.borrarTareas(evento.nombreFecha, [tarea.nombre] as String[])
        assert(evento.tareas.size() == 1)
    }

    @Test
    void borrarTarea() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = tareaService.agregarTareas(evento.nombreFecha, [tarea, tarea2] as Tarea[])
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        assert(evento.tareas.size() == 2)
        evento = tareaService.borrarTarea(evento.nombreFecha, tarea.nombre)
        assert(evento.tareas.size() == 1)
    }

    @Test
    void borrarTareaCuandoNoEnEventoShouldFail() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        evento = tareaService.agregarTareas(evento.nombreFecha, [tarea, tarea2] as Tarea[])
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        assert(evento.tareas.size() == 2)
        GroovyAssert.shouldFail {
            evento = tareaService.borrarTarea(evento.nombreFecha, tarea3.nombre)
        }
    }

    @Test
    void getTareasByNombre() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        Mockito.when(eventoRepository.save(evento2)).thenReturn(evento2)
        Mockito.when(eventoRepository.findByNombreFecha(evento2.nombreFecha)).thenReturn(evento2)
        evento = tareaService.agregarTareas(evento.nombreFecha, [tarea, tarea2] as Tarea[])
        evento2 = tareaService.agregarTareas(evento2.nombreFecha, [tarea, tarea2] as Tarea[])
        Mockito.when(eventoRepository.findByNombreLike("evento")).thenReturn([evento, evento2] as Evento[])
        def e = tareaService.getTareasByNombre(tarea.nombre, "evento")
        assert (e.size() == 2)
    }

    @Test
    void getTareasByAsignado() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        Mockito.when(eventoRepository.save(evento2)).thenReturn(evento2)
        Mockito.when(eventoRepository.findByNombreFecha(evento2.nombreFecha)).thenReturn(evento2)
        tarea2.asignado = usuario.nombreUsuario
        evento = tareaService.agregarTareas(evento.nombreFecha, [tarea, tarea2] as Tarea[])
        evento2 = tareaService.agregarTareas(evento2.nombreFecha, [tarea, tarea2] as Tarea[])
        Mockito.when(eventoRepository.findByNombreLike("evento")).thenReturn([evento, evento2] as Evento[])
        def e = tareaService.getTareasByAsignado(tarea.asignado, "evento")
        assert (e.size() == 2)
    }

    @Test
    void repartirTareas() {
        Mockito.when(eventoRepository.save(evento)).thenReturn(evento)
        Tarea tareaRepartir = new Tarea(
                "tarea1",
                "desc1",
                LocalTime.parse("00:01:01"),
                LocalTime.parse("01:01:01"),
                usuario.nombreUsuario,
                ObjectId.get(),
                1
        )
        Tarea tareaRepartir2 = new Tarea(
                "tarea2",
                "desc1",
                LocalTime.parse("01:01:01"),
                LocalTime.parse("02:01:01"),
                usuario2.nombreUsuario,
                ObjectId.get(),
                4
        )
        Tarea tareaRepartir3 = new Tarea(
                "tarea3",
                "desc1",
                LocalTime.parse("02:01:01"),
                LocalTime.parse("03:01:01"),
                usuario4.nombreUsuario,
                ObjectId.get(),
                1
        )
        Tarea tareaRepartir4 = new Tarea(
                "tarea4",
                "desc1",
                LocalTime.parse("01:01:01"),
                LocalTime.parse("03:01:01"),
                usuario4.nombreUsuario,
                ObjectId.get(),
                1
        )
        Tarea tareaRepartir5 = new Tarea(
                "tarea5",
                "desc1",
                LocalTime.parse("00:01:01"),
                LocalTime.parse("04:01:01"),
                usuario2.nombreUsuario,
                ObjectId.get(),
                1
        )
        Tarea tareaRepartir6 = new Tarea(
                "tarea6",
                "desc1",
                LocalTime.parse("10:01:01"),
                LocalTime.parse("11:01:01"),
                usuario4.nombreUsuario,
                ObjectId.get(),
                3
        )
        Tarea tareaRepartir7 = new Tarea(
                "tarea7",
                "desc1",
                LocalTime.parse("02:01:01"),
                LocalTime.parse("05:01:01"),
                usuario.nombreUsuario,
                ObjectId.get(),
                5
        )
        Tarea tareaRepartir8 = new Tarea(
                "tarea8",
                "desc1",
                LocalTime.parse("02:01:01"),
                LocalTime.parse("07:01:01"),
                usuario2.nombreUsuario,
                ObjectId.get(),
                2
        )
        Tarea tareaRepartir9 = new Tarea(
                "tarea9",
                "desc1",
                LocalTime.parse("20:01:01"),
                LocalTime.parse("21:01:01"),
                usuario4.nombreUsuario,
                ObjectId.get(),
                2
        )

        def tareas = [tareaRepartir, tareaRepartir2, tareaRepartir3, tareaRepartir4, tareaRepartir5, tareaRepartir6, tareaRepartir7, tareaRepartir8, tareaRepartir9 ]
        evento = tareaService.agregarTareas(evento.nombreFecha, tareas as Tarea[])
        def users =  [usuario.nombreUsuario, usuario2.nombreUsuario, usuario4.nombreUsuario] as String[]
        evento = tareaService.repartirTareas(evento.nombreFecha, users)
        assert evento.tareas.size() == 9
    }
}
