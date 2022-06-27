package Modelos

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("usuarios")
class Usuario {
    public String NombreUsuario
    String Contraseña
    Equipo EquipoPrivado = new Equipo("Privado", this)
    Equipo[] equipos = []

}
