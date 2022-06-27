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

    LocalTime horaInicio;
    LocalTime horaIFin;
    Usuario usuario
    Evento evento
    Equipo equipo

    @BeforeEach
    void TestSetup() {
        horaInicio = new LocalTime(21,20,13, 1)
        horaIFin = new LocalTime(22,20,12, 1)
        usuario = new Usuario(
                nombreUsuario: "user1",
                contraseña: "pass"
        )
        equipo = usuario.getEquipoPrivado()
        evento = new Evento(
                "evento1",
                new LocalDate(2022, 07, 1),
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
                usuario,
                evento
        )
        assert tarea.Nombre == "tarea"
        assert tarea.HoraFin == horaIFin
        assert tarea.HoraInicio == horaInicio
        assert tarea.Peso == 1
        assert tarea.estado == Estado.Pendiente

    }

    @Test
    void CrearTareaHoraInvalida() {
        GroovyAssert.shouldFail {
            def tarea = new Tarea(
                    1,
                    "tarea",
                    new LocalTime(22,20,13, 1),
                    new LocalTime(21,20,12, 1),
                    usuario,
                    evento
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
                    evento,
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
                nombreUsuario: "user2",
                contraseña: "pass"
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
