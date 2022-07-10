import Modelos.Equipo
import Modelos.Evento
import Modelos.Usuario
import Servicios.DuplicarEvento
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import groovy.test.GroovyAssert

import java.time.LocalDate
import java.time.LocalTime;

class EventoTest {
    Equipo equipo
    String usuario
    Evento evento
    LocalDate fecha

    @BeforeEach
    void setUp() {
        usuario = new Usuario(nombreUsuario:  "user1", contrasenia:  "pass")
        equipo = usuario.getEquipoPrivado()
        fecha = LocalDate.parse("2022-07-01")
        evento = new Evento("evento", fecha, equipo, usuario)
    }


    @Test
    void CrearEvento() {
        assert evento.nombreFecha.contains(evento.nombre + fecha.format("yyyyMMdd"))
        assert evento.tareas.size() == 0
    }

    @Test
    void CrearEventoInvalido() {
        GroovyAssert.shouldFail {
            fecha =  new LocalDate(2021,07,01)
            evento = new Evento("evento", fecha, equipo, usuario)
        }
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

    @Test
    void duplicarEventos() {
        evento.addTarea(
                1,
                "tarea1",
                new LocalTime(1, 1, 1, 1),
                new LocalTime(2, 1, 1, 1),
                new String(nombreUsuario: "user1", contraseña: "pass"),
                usuario
        )
        def user2 = new String(nombreUsuario: "user2", contraseña: "pass")
        def duplicado = DuplicarEvento.DuplicarEventos(evento, user2, user2.getEquipoPrivado())
        assert duplicado.tareas.size() == 1
    }
}
