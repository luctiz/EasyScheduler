import org.junit.jupiter.api.Test
import static groovy.test.GroovyAssert.shouldFail


class UsuarioTest {
    @Test
    void CrearUsuarioValido() {
        var usuario = new Usuario(
            nombreUsuario: "usuario",
            contraseña: "123"
        )

        assert(usuario.getNombreUsuario() == "usuario")
        assert(usuario.getContraseña() == "123")
    }

    @Test
    void CrearUsuarioNoValido() {
        var usuario = new Usuario(
                nombreUsuario: "usuario",
                contraseña: "123"
        )
        shouldFail {
            var usuario2 = new Usuario(
                    nombreUsuario: "usuario",
                    contraseña: "123"
            )

        }

    }
}
