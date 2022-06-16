import groovy.test.GroovyAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class EquipoTest {
    def usuario
    def usuario2
    def usuario3
    @BeforeEach
    void setUp() {
        usuario = new Usuario(nombreUsuario:  "user1", contraseña:  "pass")
        usuario2 =new Usuario(nombreUsuario: "usuario2", contraseña: "123")
        usuario3 =new Usuario(nombreUsuario: "usuario3", contraseña: "123")
    }


    @Test
    void CrearEquipoValido() {
        def equipoNuevo = usuario.crearNuevoEquipo("trabajo")

        assert(equipoNuevo.getNombre() == "trabajo")
        assert(equipoNuevo.getMiembros().size() == 1)
        assert(equipoNuevo.getLider() == usuario)
    }

    @Test
    void AgregarMiembroAEquipo() {

        def equipo = usuario.crearNuevoEquipo("trabajo")
        equipo.agregarMiembro(usuario2)

        assert(equipo.getMiembros().size() == 2)
        assert(equipo.getMiembros().contains(usuario))
        assert(equipo.getMiembros().contains(usuario2))

    }

    @Test
    void AgregarMiembroYaExistenteFalla() {
        def equipo = usuario.crearNuevoEquipo("trabajo")
        GroovyAssert.shouldFail {
            equipo.agregarMiembro(usuario)
        }
        equipo.agregarMiembro(usuario2)
        GroovyAssert.shouldFail {
            equipo.agregarMiembro(usuario2)
        }
        assert(equipo.getMiembros().size() == 2)
        assert(equipo.getMiembros().contains(usuario))
        assert(equipo.getMiembros().contains(usuario2))
    }

    @Test
    void AgregarEventoAEquipoSiendoLiderEsValido(){
        def equipo = usuario.crearNuevoEquipo("trabajo")
        equipo.agregarMiembro(usuario2)
        def fecha = new LocalDate(2022,07,01)
        def evento = new Evento("eventoequipo",fecha,equipo,usuario)

        evento.addTarea(
                1,
                "tarea1",
                new LocalTime(1,1,1,1),
                new LocalTime(2,1,1,1),
                usuario,
                usuario
        )

        assert(evento.getTareas().size() == 1)


    }

    @Test
    void AgregarEventoAEquipoSinSerLiderEsInvalido(){
        def equipo = usuario.crearNuevoEquipo("trabajo")
        equipo.agregarMiembro(usuario2)
        def fecha = new LocalDate(2022,07,01)
        GroovyAssert.shouldFail {
            def evento = new Evento("eventoequipo", fecha, equipo, usuario2)
        }
    }

    @Test
    void AgregarTareaAEventoSinSerLiderEsInvalido(){
        def equipo = usuario.crearNuevoEquipo("trabajo")
        equipo.agregarMiembro(usuario2)
        def fecha = new LocalDate(2022,07,01)
        def evento = new Evento("eventoequipo", fecha, equipo, usuario)


        GroovyAssert.shouldFail {
            evento.addTarea(
                    1,
                    "tarea1",
                    new LocalTime(1,1,1,1),
                    new LocalTime(2,1,1,1),
                    usuario2,
                    usuario2
            )
        }
    }

    @Test
    void AsignarTareaAMiembroSinSerLiderEsInvalido(){
        def equipo = usuario.crearNuevoEquipo("trabajo")
        equipo.agregarMiembro(usuario2)
        def fecha = new LocalDate(2022,07,01)
        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
        def tarea = evento.addTarea(
                1,
                "tarea1",
                new LocalTime(1,1,1,1),
                new LocalTime(2,1,1,1),
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
        def fecha = new LocalDate(2022,07,01)
        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
        def tarea = evento.addTarea(
                1,
                "tarea1",
                new LocalTime(1,1,1,1),
                new LocalTime(2,1,1,1),
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
        def fecha = new LocalDate(2022,07,01)
        def evento = new Evento("eventoequipo", fecha, equipo, usuario)
        def tarea = evento.addTarea(
                1,
                "tarea1",
                new LocalTime(1,1,1,1),
                new LocalTime(2,1,1,1),
                usuario,
                usuario
        )

        tarea.setAsignado(usuario, usuario2)
        assert(tarea.getAsignado() == usuario2)
    }

}
