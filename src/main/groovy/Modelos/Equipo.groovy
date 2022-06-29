package Modelos

import org.springframework.data.mongodb.core.mapping.Document


@Document("equipos")
class Equipo extends Entity {

    String Nombre
    String Lider

    Equipo(nombre, String creador) {
        super
        this.Nombre = nombre
        this.Lider = creador
    }
}
