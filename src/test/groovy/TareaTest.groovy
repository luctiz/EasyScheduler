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
    void TestSetup() {
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
}
