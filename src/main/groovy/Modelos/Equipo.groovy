package Modelos

import org.springframework.data.mongodb.core.index.Indexed

class Equipo extends Entity {
    @Indexed
    public String nombre
    public String lider

    Equipo(nombre, String creador) {
        this.nombre = nombre
        this.lider = creador
        super
    }

    Equipo() {

    }
}
