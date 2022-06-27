package Modelos

import org.springframework.data.mongodb.core.mapping.Document


@Document("equipos")
class Equipo extends Entity {

    String Nombre
    Usuario Lider
    Usuario[] Miembros = [] // contiene al lider

    Equipo(nombre, Usuario creador){
        super
        this.id = nombre + "_" + creador.NombreUsuario
        this.Nombre = nombre
        this.Lider = creador
        Miembros = Miembros + creador
    }
}
