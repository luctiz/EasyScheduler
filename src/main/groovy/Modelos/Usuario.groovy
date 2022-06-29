package Modelos

import org.springframework.data.mongodb.core.mapping.Document

@Document("usuarios")
class Usuario extends Entity {
    String NombreUsuario
    String Contraseña
    Equipo[] Equipos = []

    Usuario(String nombreUsuario, String contraseña) {
        NombreUsuario = nombreUsuario
        Contraseña = contraseña
    }

}
