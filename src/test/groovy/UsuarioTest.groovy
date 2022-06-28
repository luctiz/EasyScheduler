import Controladores.UsuarioController
import Modelos.Usuario
import Repositorios.EquipoRepository
import Repositorios.UsuarioRepository
import Servicios.EquipoService
import Servicios.UsuarioService
import groovy.test.NotYetImplemented
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import groovy.test.GroovyAssert
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration


@RunWith(MockitoJUnitRunner.class)
class UsuarioTest {


    @Mock
    private static EquipoRepository equipoRepository
    @Mock
    private static UsuarioRepository usuarioRepository
    private static EquipoService equipoService
    private static UsuarioService usuarioService


    static usuario = new Usuario(
            nombreUsuario: "usuario",
            contraseña: "123"
    )

    @BeforeAll
    static void setUp() {
        equipoService = new EquipoService(usuarioRepository: usuarioRepository, equipoRepository: equipoRepository)
        usuarioService = new UsuarioService(usuarioRepository: usuarioRepository, equipoService: equipoService)
        usuarioService.crearUsuario(usuario)
    }


    @Test
    void CrearUsuarioValido() {
        def user = usuarioService.getUsuario(usuario.NombreUsuario)
        assert(user.getNombreUsuario() == "usuario")
        assert(user.getContraseña() == "123")
    }

    @NotYetImplemented
    @Test
    void CrearUsuarioNoValido() {
        var usuario = new Usuario(
                nombreUsuario: "usuario",
                contraseña: "123"
        )
        GroovyAssert.shouldFail {
            var usuario2 = new Usuario(
                    nombreUsuario: "usuario",
                    contraseña: "123"
            )

        }

    }

    @Test
    void UsuarioNuevoTieneEquipoPrivadoPersonal(){
        var usuario = new Usuario(
                nombreUsuario: "usuario",
                contraseña: "123"
        )
        assert(usuario.getEquipoPrivado().getNombre() == "Privado")
    }
}
