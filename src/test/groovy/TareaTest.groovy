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
import org.mockito.Mockito

import java.time.LocalDate
import java.time.LocalTime

class TareaTest {


    LocalTime horaInicio2
    LocalTime horaInicio = LocalTime.parse("21:20:12.1")
    LocalTime horaFin = LocalTime.parse("22:20:13.1")
    LocalTime horaFin2 = LocalTime.parse("23:20:13.1")
    LocalDate fecha = LocalDate.now()
    LocalDate fecha2 = LocalDate.now()
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
    Usuario usuario3  = new Usuario(
        "user3",
        "pass",
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
    Tarea tarea = new Tarea(
        "tarea1",
            "desc1",
            horaInicio,
            horaFin,
            usuario.nombreUsuario,
            ObjectId.get()
    )
    Tarea tarea2 = new Tarea(
            "tarea2",
            "desc1",
            horaInicio2,
            horaFin2,
            usuario2.nombreUsuario,
            ObjectId.get()
    )
    Tarea tarea3 = new Tarea(
            "tarea3",
            "desc1",
            horaInicio,
            horaFin,
            usuario3.nombreUsuario,
            ObjectId.get()
    )

    UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class)
    EventoRepository eventoRepository = Mockito.mock(EventoRepository.class)
    UsuarioService usuarioService = new UsuarioService(usuarioRepository: usuarioRepository)
    EquipoService equipoService = new EquipoService(usuarioRepository: usuarioRepository, usuarioService: usuarioService)
    EventoService eventoService = new EventoService(usuarioService: usuarioService, eventoRepository: eventoRepository, equipoService:  equipoService)
    TareaService tareaService = new TareaService(usuarioService: usuarioService, eventoRepository:  eventoRepository, equipoService:  equipoService, eventoService:  eventoService)


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
        def usuarios = [usuario2, usuario]
        Mockito.when(usuarioRepository.saveAll(usuarios)).thenReturn(usuarios)
        equipo = equipoService.crearEquipo(equipo.nombre, equipo.lider, [usuario.nombreUsuario, usuario2.nombreUsuario] as String[])
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
        def tarea = new Tarea(
                1,
                "tarea",
                horaInicio,
                horaIFin,
                usuario
        )
        assert tarea.nombre == "tarea"
        assert tarea.horaFin == horaIFin
        assert tarea.horaInicio == horaInicio
        assert tarea.peso == 1
        assert tarea.estado == Estado.Pendiente

    }

    @Test
    void CrearTareaHoraInvalida() {
        GroovyAssert.shouldFail {
            def tarea = new Tarea(
                    1,
                    "tarea",
                    LocalTime.parse("22:20:13.1"),
                    LocalTime.parse("21:20:12.1"),
                    usuario
            )
        }
    }

    @Test
    void CrearTareaPesoInvalido() {
        GroovyAssert.shouldFail {
            def tarea = new Tarea(
                    1,
                    "tarea",
                    horaInicio,
                    horaIFin,
                    usuario,
                    - 1
            )
        }
    }

    @Test
    void CompletarTareaAsignadaUsuario() {
        def tarea = new Tarea(
                1,
                "tarea",
                horaInicio,
                horaIFin,
                usuario,
                evento
        )
        usuario.completarTarea(tarea)
        assert(tarea.estado == Estado.Completado)
    }

    @Test
    void CompletarTareaNoAsignadaEsInvalido() {
        def usuario2 = new Usuario(
                "user2",
                "pass"
        )
        def tarea = new Tarea(
                1,
                "tarea",
                horaInicio,
                horaIFin,
                usuario,
                evento
        )
        GroovyAssert.shouldFail {
            usuario2.completarTarea(tarea)
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


    @Test
    void AgregarTareaAEvento() {
        evento.addTarea(
                1,
                "tarea1",
                new LocalTime(1,1,1,1),
                new LocalTime(2,1,1,1),
                new String(nombreUsuario: "user1",contraseña: "pass"),
                usuario
        )
        evento.tareas.size() == 1
    }

    @Test
    void AgregarTareaInvalidaAEvento() {
        evento.addTarea(
                1,
                "tarea1",
                new LocalTime(1,1,1,1),
                new LocalTime(2,1,1,1),
                new String(nombreUsuario: "user1",contraseña: "pass"),
                usuario
        )
        GroovyAssert.shouldFail {
            evento.addTarea(
                    1,
                    "tarea1",
                    new LocalTime(1,1,1,1),
                    new LocalTime(2,1,1,1),
                    new String(nombreUsuario: "user1",contraseña: "pass"),
                    usuario
            )
        }
    }


}
