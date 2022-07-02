package Modelos

import org.springframework.data.mongodb.core.mapping.Document

@Document("usuarios")
class Usuario extends Entity {
    public String nombreUsuario
    public String contraseña
    public Equipo[] equipos = []

    Usuario(String nombreUsuario, String contraseña) {
        this.nombreUsuario = nombreUsuario
        this.contraseña = contraseña
    }

}
