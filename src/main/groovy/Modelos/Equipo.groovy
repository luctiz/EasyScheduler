package Modelos

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed

class Equipo extends Entity {
    @Indexed
    public String nombre
    public String lider

    Equipo(nombre, String creador, ObjectId _id) {
        this.nombre = nombre
        this.lider = creador
        super._id = _id
    }

    Equipo() {

    }
}
