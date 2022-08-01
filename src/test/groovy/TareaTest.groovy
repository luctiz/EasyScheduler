import Modelos.Equipo
import Modelos.Estado
import Modelos.Evento
import Modelos.Tarea
import Modelos.Usuario
import groovy.test.GroovyAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import java.time.LocalDate
import java.time.LocalTime

class TareaTest {

    LocalTime horaInicio
    LocalTime horaIFin
    String usuario
    Evento evento
    Equipo equipo

    @BeforeEach
    void setup() {
        horaInicio = LocalTime.parse("22:20:13.1")
        horaIFin = LocalTime.parse("21:20:12.1")
        usuario = new Usuario(
                "user1",
                "pass"
        )
        equipo = usuario.getEquipoPrivado()
        evento = new Evento(
                "evento1",
                LocalDate.parse("2022-07-1"),
                equipo,
                usuario
        )
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
