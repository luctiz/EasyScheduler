import Modelos.Usuario
import Repositorios.EquipoRepository
import Repositorios.UsuarioRepository
import Servicios.EquipoService
import Servicios.UsuarioService
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import groovy.test.GroovyAssert
import org.mockito.Mockito



class UsuarioTest {


    private static UsuarioRepository usuarioRepository
    private static EquipoService equipoService
    private static UsuarioService usuarioService


    static usuario = new Usuario(
            "usuario",
            "123"
    )

    @BeforeAll
    static void setUp() {
        usuarioRepository = Mockito.mock(UsuarioRepository.class)
        equipoService = new EquipoService(usuarioRepository:  usuarioRepository, usuarioService:  usuarioService)
        usuarioService = new UsuarioService(usuarioRepository: usuarioRepository, equipoService: equipoService)
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario)
        usuarioService.crearUsuario(usuario)
    }


    @Test
    void CrearUsuarioValido() {
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario.NombreUsuario)).thenReturn(usuario)
        def user = usuarioService.getUsuario(usuario.NombreUsuario)
        assert(user.NombreUsuario == "usuario")
        assert(user.Contraseña == "123")
        assert(user.equipos.size() > 0)
    }

    @Test
    void CrearUsuarioNoValido() {
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario.NombreUsuario)).thenReturn(usuario)
        GroovyAssert.shouldFail {
            var usuario2 = new Usuario(
                    "usuario",
                    "123"
            )
            usuarioService.crearUsuario(usuario2)
        }

    }

}
