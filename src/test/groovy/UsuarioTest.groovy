import Modelos.Usuario

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
        usuarioService = new UsuarioService(usuarioRepository: usuarioRepository)
        equipoService = new EquipoService(usuarioRepository:  usuarioRepository, usuarioService:  usuarioService)
        Mockito.when(usuarioRepository.save(usuario)).thenReturn(usuario)
        usuarioService.crearUsuario(usuario)
    }


    @Test
    void CrearUsuarioValido() {
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario.nombreUsuario)).thenReturn(usuario)
        def user = usuarioService.getUsuario(usuario.nombreUsuario)
        assert(user.nombreUsuario == "usuario")
        assert(user.contraseña == "123")
        assert(user.equipos.size() > 0)
    }

    @Test
    void CrearUsuarioNoValido() {
        Mockito.when(usuarioRepository.findByNombreUsuario(usuario.nombreUsuario)).thenReturn(usuario)
        GroovyAssert.shouldFail {
            var usuario2 = new Usuario(
                    "usuario",
                    "123"
            )
            usuarioService.crearUsuario(usuario2)
        }

    }

}
