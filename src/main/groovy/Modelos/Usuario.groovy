package Modelos

import org.springframework.data.mongodb.core.mapping.Document

@Document("usuarios")
class Usuario {
    String NombreUsuario
    String Contraseña
    Equipo EquipoPrivado
    Equipo[] equipos = []

}
