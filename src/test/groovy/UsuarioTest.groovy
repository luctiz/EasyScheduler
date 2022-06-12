import groovy.test.NotYetImplemented
import org.junit.jupiter.api.Test
import groovy.test.GroovyAssert;

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
}
