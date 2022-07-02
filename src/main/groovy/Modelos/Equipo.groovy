package Modelos

import org.springframework.data.mongodb.core.mapping.Document


@Document("equipos")
class Equipo extends Entity {

    public String nombre
    public String lider

    Equipo(nombre, String creador) {
        super
        this.nombre = nombre
        this.lider = creador
    }
}
